package com.xincheng.login.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.icitic.model.Menu;
import com.icitic.model.User;
import com.icitic.service.AuthorityManager;
import com.icitic.service.AuthorityManagerImpl;
import com.icitic.service.LoginManager;
import com.icitic.service.LoginManagerImpl;
import com.icitic.service.OrgnizationManager;
import com.icitic.service.OrgnizationManagerImpl;
import com.icitic.service.UserManger;
import com.icitic.service.UserMangerImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.xincheng.competence.Competence;
import com.xincheng.competence.OrgUtils;
import com.xincheng.config.SystemConfig;
import com.xincheng.login.model.UserSession;
import com.xincheng.utils.Encrypt;
import com.xincheng.utils.EncryptImpl;
import com.xincheng.web.BaseAction;

@Namespace("/uamsLogin")
@Action(value = "uamsAction")
@Results({ @Result(name = BaseAction.SUCCESS, location = "/jsp/Master.jsp"), @Result(name = "login", location = "/jsp/login.jsp"), @Result(name = "ssoLoginFaild", location = "/jsp/SSOLoginFaild.jsp") })
public class UamsLoginAction extends ActionSupport {

	Logger log = LoggerFactory.getLogger(UamsLoginAction.class);
	private static final long serialVersionUID = -1906403035132726404L;

	private final String SYSID = "MSGC";
	
	private static String SsoUrl = SystemConfig.getPara("SsoUrl");

	/**
	 * 进登陆页面
	 */
	public String init() {
		HttpServletRequest request = ServletActionContext.getRequest();
		// get version info
		String versionNum = "";
		File fin = null;
		BufferedReader in = null;
		try {
			fin = new File(ServletActionContext.getServletContext().getRealPath("/version.txt"));
			if (fin.exists() && fin.canRead()) {
				in = new BufferedReader(new FileReader(fin));
				String tempStr = "";
				while ((tempStr = in.readLine()) != null) {
					versionNum += tempStr;
				}
				in.close();
			}
		} catch (Exception e) {
			versionNum = "Error in getting Version Number";
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		}
		request.getSession().setAttribute("versionNum", versionNum);
		return "login";// new ActionForward("/login.jsp?from=1",false);//

	}

	/**
	 * 进主页面
	 */
	public String doWelcome(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if ("true".equals(session.getAttribute("isLogin"))) {
			session.setAttribute("loginErrMsg", "");
			return SUCCESS;
		} else {
			return init();
		}
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public String loginSSO() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Enumeration nm = session.getAttributeNames();
		while (nm.hasMoreElements()) {
			session.removeAttribute(nm.nextElement() + "");
		}
		try {

			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				log.info("本系统登录   Request参数: " + paraName + " = " + request.getParameter(paraName));
			}

			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			String sso = request.getParameter("sso");// FROMAPP
			if (session.getAttribute("sso") != null && !session.getAttribute("sso").toString().equals(sso)) {
				// can not login in different way
				request.setAttribute("loginErrMsg", "不能同时以两种方式登陆！");
				return init();
			}
			LoginManager loginManager = new LoginManagerImpl();
			int loginFlag = 0;
			boolean loginWithSSO = false;
			Encrypt encript = new EncryptImpl();

			if (sso != null && encript.decrypt(sso).equals("fromSSO")) {
				log.info("LoginAction --> doLogin --> login with SSO");
				// decrypt sso parameter
				sso = encript.decrypt(sso);
				if (userName == null || userName.equals("")) {
					// if user name is null, set loginFlag=-5
					userName = "";
					loginFlag = -5;
				} else if (password == null || password.equals("")) {
					// if password is null, set loginFlag=-4
					password = "";
					loginFlag = -4;
				} else {
					// decode user name
					userName = encript.deCodeAES(userName);
					loginFlag = loginManager.logOnWithSSO(SYSID, userName, password, request);
					log.info("本系统登录   调用loginManager.logOn 返回 loginFlag = " + loginFlag);
				}
				loginWithSSO = true;
			} else {
				log.info("LoginAction --> doLogin --> login with APP");
				// if request not come from SSO
				loginFlag = loginManager.logOn(SYSID, userName, password, request);
				log.info("本系统登录   调用loginManager.logOn 返回 loginFlag = " + loginFlag);
			}
			log.info("----> doLogin sso = " + sso + "   userName = " + userName + "   loginFlag = " + loginFlag);

			if (loginFlag == 1) {
				// login success
				List fnLsSession = loginManager.getAuthority(request);

				List menuList = (List) loginManager.getMenus(request);// 菜单列表
				Menu mn;
				Competence c_menu = null;
				List<Competence> c_menuList = new ArrayList<Competence>();
				String c_highid = null;
				HashMap<String, String> surlMap = null;
				boolean isSave = false;
				for (int i = 0; i < menuList.size(); i++) {
					mn = (Menu) menuList.get(i);
					String id = mn.getId(); // 菜单id
					String highid = mn.getHighid(); // 上级id
					String menuname = mn.getMenuname(); // 菜单名称
					int menuorder = mn.getMenuorder(); // 排序号
					String url = mn.getUrl(); // 菜单URL
					log.info("本系统登录   调用loginManager.getMenus(request) 返回  菜单id = " + id + " 上级id = " + highid + " 菜单名称   = " + menuname + " 排序号  = " + menuorder + " 菜单URL = " + url);
					if (c_menu == null || highid == null || "1".equals(highid)) {
						c_menu = new Competence();
						c_menu.setId(id);
						c_menu.setName(menuname);
						c_menu.setUrl(url);
						c_menu.setSubMenus(new ArrayList<Competence>());
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

				// get the right info of corporation and channel
				/*AuthorityManager authManager = new AuthorityManagerImpl();
				Hashtable ht = authManager.getDataVisit(request, userName, SYSID);

				List<String> csoList = new ArrayList<String>();// CSO列表
				if (ht != null) {
					if (ht.size() == 0) {
						log.info("本系统登录   调用authManager.getDataVisit(request, userName, SYSID) >  ht = 无数据");
						loginFlag = -6;
						return loginErr(session, loginFlag, loginWithSSO);
					}
					for (Iterator itr = ht.keySet().iterator(); itr.hasNext();) {
						String key = (String) itr.next();
						Object value = ht.get(key);
						log.info("本系统登录   调用authManager.getDataVisit(request, userName, SYSID) >  itr.next() > key = " + key + " & ht.get(key) value = " + value);
					}

					String[] sa = (String[]) ht.get("corpration");
					if (sa != null && sa.length > 0) {
						for (String s : sa) {
							log.info("本系统登录   调用authManager.getDataVisit(request, userName, SYSID) >  ht.get(corpration) > String[] = " + s);
							int i = s.indexOf("|");
							String cso = s.substring(0, i);
							log.info("本系统登录   调用authManager.getDataVisit(request, userName, SYSID) >  ht.get(corpration) > cso = " + cso);
							csoList.add(cso);
						}
					}
				} else {
					log.info("本系统登录   调用authManager.getDataVisit(request, userName, SYSID) 返回          null ");
				}*/
				// UserKeyInfo user = loginManager.getUserKeyInfo(request);
				UserManger userManger = new UserMangerImpl();
				User user = userManger.getUser(userName);

				String orgIds = OrgUtils.getSimpleSubOrgnizations(user.getOrgnizationid());
				if (orgIds == null || "".equals(orgIds.trim())) {
					OrgnizationManager orgn = new OrgnizationManagerImpl();
					List orgnList = orgn.getOrgnizations();
					OrgUtils.setSimpleCache(orgnList);					
					orgIds = OrgUtils.getSimpleSubOrgnizations(user.getOrgnizationid());
				}

				
				UserSession userSession = new UserSession();
				userSession.setAgntNum(user.getId());
				userSession.setFullName(user.getChinesename());
				log.info("本系统登录   登录用户 组织ID = "+user.getOrgnizationid());
				session.setAttribute("UserSession", userSession);
				session.setAttribute("orgIds", orgIds);
				session.setAttribute("userManger", user);
			//	session.setAttribute("csoList", csoList);
				session.setAttribute("menuList", menuList);
				session.setAttribute("c_menuList", c_menuList);
				session.setAttribute("ssoFlag", "false");
				session.setAttribute("sso", sso);
				session.setAttribute("isLogin", "true");
				session.setAttribute("setUserID", loginManager.getUserKeyInfo(request).getId());// AMSContext
																								// initial
																								// id

			} else {
				return loginErr(session, loginFlag, loginWithSSO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doWelcome(request);
	}

	private String loginErr(HttpSession session, int loginFlag, boolean loginWithSSO) {
		// login fail
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
		session.setAttribute("ssoFlag", "true");
		// if(loginWithSSO){
		// return "ssoLoginFaild";
		// }else{
		return init();
		// }
	}

	/*
	 * 切换分公司
	 */
	public void doChange(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String newBrch = request.getParameter("newBrch");
		session.setAttribute("CurrentBran", newBrch);
		session.setAttribute("setCompany", newBrch.substring(0, 1));// AMSContext
																	// initial
																	// company
		session.setAttribute("setBranch", newBrch.substring(1, 3));// AMSContext
																	// initial
																	// branch
		session.setAttribute("loginErrMsg", "");
		response.getOutputStream().print("1");
		response.getOutputStream().close();
	}

	/*
	 * 退出
	 */

	public void doLogoff() {
		log.info("正在退出.......");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("isLogin", "false");
		session.setAttribute("loginErrMsg", "");
		session.setAttribute("UserSession", null);
		session.removeAttribute("csoList");
		session.removeAttribute("menuList");
		session.removeAttribute("c_menuList");
		session.removeAttribute("ssoFlag");
		session.removeAttribute("sso");
		session.removeAttribute("setUserID");

		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.sendRedirect(SsoUrl);
		} catch (IOException e) {

			try {
				response.sendRedirect("/jsp/login.jsp");
			} catch (IOException e1) {
			}
		}
	}

	/**
	 * 获取CSO列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> loadCSO() {
		List<String> csoList = null;
		try {

			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			csoList = (List<String>) session.getAttribute("csoList");

		} catch (Exception e) {
			log.error("获取CSO列表异常 = " + e.getMessage());
			e.printStackTrace();
		}
		return csoList;
	}
}
