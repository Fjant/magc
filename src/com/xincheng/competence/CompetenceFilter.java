package com.xincheng.competence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sun.misc.BASE64Encoder;

import com.icitic.model.Menu;
import com.icitic.model.User;
import com.icitic.service.LoginManager;
import com.icitic.service.LoginManagerImpl;
import com.icitic.service.OrgnizationManager;
import com.icitic.service.OrgnizationManagerImpl;
import com.icitic.service.UserManger;
import com.icitic.service.UserMangerImpl;
import com.xincheng.config.SystemConfig;
import com.xincheng.login.model.UserSession;
import com.xincheng.utils.Encrypt;
import com.xincheng.utils.EncryptImpl;

public class CompetenceFilter implements Filter {
	private static Log log = LogFactory.getLog(CompetenceFilter.class);
	private final String SYSID = "MSGC";
	private SystemConfig systemConfig;
	private List<String> ignoreAuthUris;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

		HttpServletRequest req = ((HttpServletRequest) request);
		HttpServletResponse res = ((HttpServletResponse) response);
		HttpSession session = req.getSession();
		String reqURL = req.getRequestURL().toString();
		if (ignoreAuthUris != null && ignoreAuthUris.contains(((HttpServletRequest) request).getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}

		String contexIdentifer = ((HttpServletRequest) request).getSession().getId() + "-" + Thread.currentThread().getName();
		String sso = request.getParameter("sso");// FROMAPP
		Encrypt encript = new EncryptImpl();

		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			log.info("权限过滤器   Request参数: " + paraName + " = " + request.getParameter(paraName));
		}

		log.info("权限过滤 ################# 请求路径 = " + reqURL);
		log.info("权限过滤 ################# sso = " + sso);

		try {

			if (reqURL.indexOf("uamsAction") != -1 || reqURL.indexOf("login.jsp") != -1) {
				req.getRequestDispatcher("/uamsLogin/uamsAction!init.action").forward(request, response);
				return;
			}
			String referer = req.getHeader("Referer");
			log.info("来源路径  >>>>>  referer = " + referer);
			// 如果是来自单点登录,进行登录验证
			if (referer != null && referer.indexOf(systemConfig.getPara("SsoUrl")) != 1 && sso != null && encript.decrypt(sso).equals("fromSSO")) {
				log.info("来自单点登录  >>>>>  登录验证");
				uamsLogin(request, response, chain, req, res, session, sso);
			}
			// 访问customerMsglist.jsp不需要通过权限过滤
			if (reqURL.indexOf("customerMsglist.jsp") != -1) {
				chain.doFilter(request, response);
				return;
			}

			// 本系统没有登录
			if (session.getAttribute("isLogin") == null || session.getAttribute("isLogin").toString().equals("false")) {

				// if from SSO(含有 "sso" 参数，值解密后为 "fromSSO")
				if (sso != null && encript.decrypt(sso).equals("fromSSO")) {
					uamsLogin(request, response, chain, req, res, session, sso);

				} else {
					// formSSO(req, res, path);
					if (session.getAttribute("ssoFlag") == null || session.getAttribute("ssoFlag").equals("true")) {
						log.info("权限过滤 ################# 没有登录   >>>>>>>> 前往单点登录");
						// log.info("权限过滤 ################# 没有登录   >>>>>>>> 前往本系统登录");
						formSSO(req, res);
					} else {
						log.info("权限过滤 ################# 没有登录   >>>>>>>> 前往本系统登录");
						req.getRequestDispatcher("/uamsLogin/uamsAction!init.action").forward(request, response);
						return;
					}
				}
			} else {
				// 已经登录

				if (reqURL.indexOf("uamsAction") != -1) {
					chain.doFilter(request, response);
					return;
				} else {
					if (pageCheck(req, session)) {
						log.info("权限过滤 ################# 已经登录   >>>>>>>> 通过权限验证");
						chain.doFilter(request, response);
						return;
					} else {
						log.info("权限过滤 ################# 已经登录   >>>>>>>> 没有通过权限验证");
						log.warn(contexIdentifer + ": Requesting " + req.getRequestURI() + " without permission!");
						req.getRequestDispatcher("/jsp/NoPermission.jsp").forward(request, response);// 您无权使用该功能
						return;
					}
				}
			}

		} catch (Exception ex) {
			log.error("权限过滤 ################# 异常   >>>>>>>> " + ex.getMessage());
			ex.printStackTrace();
			req.getRequestDispatcher("/jsp/error.jsp").forward(request, response);// forward
																					// error
																					// page
			return;
		}
	}

	private void uamsLogin(ServletRequest request, ServletResponse response, FilterChain chain, HttpServletRequest req, HttpServletResponse res, HttpSession session, String sso) throws Exception, IOException, ServletException {
		Enumeration nm = session.getAttributeNames();
		while (nm.hasMoreElements()) {
			session.removeAttribute(nm.nextElement() + "");
		}
		String userid = req.getParameter("userid");
		String password = req.getParameter("password");
		int loginFlag = 0;
		if (userid != null && !"".equals(userid.trim()) && password != null && !"".equals(password.trim())) {
			// EncryptImpl类的deCodeAES（）
			EncryptImpl e = new EncryptImpl();
			userid = e.deCodeAES(userid);
			log.info("解密后的                      userid = " + userid);
			LoginManager loginManager = new LoginManagerImpl();
			loginFlag = loginManager.logOnWithSSO(SYSID, userid, password, req);
			log.info("单点登录   调用loginManager.logOnWithSSO 返回 loginFlag = " + loginFlag);
			log.info("登录验证返回    loginFlag = " + loginFlag);
			if (loginFlag == 1) {
				// login success
				List menuList = (List) loginManager.getMenus(req);// 菜单列表
				Menu mn;
				Competence c_menu = null;
				List<Competence> c_menuList = new ArrayList<Competence>();
				for (int i = 0; i < menuList.size(); i++) {
					mn = (Menu) menuList.get(i);
					String id = mn.getId(); // 菜单id
					String highid = mn.getHighid(); // 上级id
					String menuname = mn.getMenuname(); // 菜单名称
					int menuorder = mn.getMenuorder(); // 排序号
					String url = mn.getUrl(); // 菜单URL
					log.info("单点登录   调用loginManager.getMenus(request) 返回  菜单id = " + id + " 上级id = " + highid + " 菜单名称   = " + menuname + " 排序号  = " + menuorder + " 菜单URL = " + url);
					if (c_menu == null || highid == null || "1".equals(highid)) {
						c_menu = new Competence();
						c_menu.setId(id);
						c_menu.setName(menuname);
						c_menu.setUrl(url);
						c_menu.setSubMenus(new ArrayList<Competence>());
						c_menu.setOrder(menuorder);
						c_menuList.add(c_menu);
					} else if (!"1".equals(highid)) {
						for (Competence c : c_menuList) {
							String c_id = c.getId();
							if (highid.equals(c_id)) {
								Competence subMemu = new Competence();
								subMemu.setName(menuname);
								subMemu.setUrl(url);
								c.getSubMenus().add(subMemu);
							}
						}
					}
				}

				Collections.sort(c_menuList);

				for (Competence item : c_menuList) {
					sortMemu(item);
				}

				UserManger userManger = new UserMangerImpl();
				User user = userManger.getUser(userid);

				String orgIds = OrgUtils.getSimpleSubOrgnizations(user.getOrgnizationid());
				if (orgIds == null || "".equals(orgIds.trim())) {
					OrgnizationManager orgn = new OrgnizationManagerImpl();
					List orgnList = orgn.getOrgnizations();
					OrgUtils.setSimpleCache(orgnList);
				}

				UserSession userSession = new UserSession();
				userSession.setAgntNum(user.getId());
				userSession.setFullName(user.getChinesename());
				log.info("单点登录   登录用户 组织ID = " + user.getOrgnizationid());
				session.setAttribute("UserSession", userSession);
				session.setAttribute("userManger", user);
				session.setAttribute("menuList", menuList);
				session.setAttribute("c_menuList", c_menuList);
				session.setAttribute("ssoFlag", "false");
				session.setAttribute("sso", sso);
				session.setAttribute("isLogin", "true");
				session.setAttribute("setUserID", loginManager.getUserKeyInfo(req).getId());// AMSContext
																							// initial
																							// id
				log.info(c_menuList);
				req.getRequestDispatcher("/jsp/Master.jsp").forward(req, res);
				return;
			}
		}
		loginErr(req, res, session, sso, loginFlag);
	}

	private void loginErr(HttpServletRequest req, HttpServletResponse res, HttpSession session, String sso, int loginFlag) throws ServletException, IOException {
		if (loginFlag != 1) {
			String loginErrMsg = "";
			switch (loginFlag) {
			case 0:
				loginErrMsg = "网络异常！";
				break;
			case -1:
				loginErrMsg = "账号已被停用！";
				break;
			case -2:
				loginErrMsg = "账号已被锁定！";
				break;
			case -3:
				loginErrMsg = "账号密码已过期！";
				break;
			case -4:
				loginErrMsg = "密码错误！";
				break;
			case -5:
				loginErrMsg = "账号不存在！";
				break;
			case -6:
				loginErrMsg = "账号无权限！";
				break;
			case -10:
				loginErrMsg = "账号密码输入错误3次，已被锁定！";
				break;
			}
			session.setAttribute("loginErrMsg", loginErrMsg);
			req.getRequestDispatcher("/jsp/login.jsp?sso=" + sso).forward(req, res);
		}
	}

	private void sortMemu(Competence parentItem) {

		if (parentItem == null || parentItem.getSubMenus() == null || parentItem.getSubMenus().size() <= 1)
			return;

		Collections.sort(parentItem.getSubMenus());

		for (Competence subItem : parentItem.getSubMenus()) {
			sortMemu(subItem);
		}
	}

	/**
	 * 判断是否有权限
	 * 
	 * @param req
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private boolean pageCheck(HttpServletRequest req, HttpSession session) {// check
																			// url
																			// .do
		// 请求URL
		String reqURL = req.getServletPath();
		if (reqURL.indexOf("Master") != -1 || reqURL.indexOf("error") != -1) {
			return true;
		}

		List<Menu> menuList = (List<Menu>) session.getAttribute("menuList");
		if (menuList == null) {
			return false;
		}

		for (Menu m : menuList) {
			String url = m.getUrl();
			if (("/" + url).equals(reqURL)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 转向到SSO系统的登录模块
	 * 
	 * @param request
	 * @param response
	 * @param path
	 * @throws IOException
	 */
	private void formSSO(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BASE64Encoder base64 = new BASE64Encoder();
		String desturl = systemConfig.getPara("DestUrl");
		log.info("请求SSO的    desturl = " + desturl);
		String en_path = base64.encodeBuffer(desturl.getBytes());
		forward(request, response, systemConfig.getPara("SsoUrl") + "?desturl=" + en_path);
		return;
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Prama", "no-cache");
		response.sendRedirect(url);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		ApplicationContext application = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		systemConfig = ((SystemConfig) application.getBean("systemConfig"));

		String ignoreAuthUris = config.getInitParameter("ignoreAuthUris");
		this.ignoreAuthUris = java.util.Arrays.asList(ignoreAuthUris.split(","));
	}

}
