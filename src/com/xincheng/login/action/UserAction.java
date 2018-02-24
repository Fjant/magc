package com.xincheng.login.action;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xincheng.ajax.AjaxResData;
import com.xincheng.competence.Competence;
import com.xincheng.login.model.User;
import com.xincheng.login.service.UserService;
import com.xincheng.utils.JsonUtils;

@Namespace("/login")
@Action(value = "login")
@Results({@Result(name="success", type="json")})
public class UserAction extends ActionSupport{
	@Autowired
	private UserService userService;
	private static final long serialVersionUID = 4898670276152595700L;
	
	@SuppressWarnings("unused")
	public String login(){
		AjaxResData ret = new AjaxResData();
		String userName = ServletActionContext.getRequest().getParameter("userName");
		String password = ServletActionContext.getRequest().getParameter("password");
		User user = userService.findUserByLogin(userName);
		if(user==null){
			
		}
		JsonUtils.renderJson(user);
		return null;
		
	}
	/**
	 * 测试账户:60000079
	 * 密码:56781234
	 * @return
	 */
	@Action(value = "login2", results = { @Result(name = SUCCESS, location = "/jsp/Master_Competence.jsp") }) 
	public String userLogin(){
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();
		
		ArrayList<Competence> list = new ArrayList<Competence>();
		Competence c_1 = new Competence();
		c_1.setId("1");
		c_1.setName("营销员管理");
		c_1.setUrl(null);
		HashMap<String,String> surlMap_1 = new HashMap<String,String>();
		surlMap_1.put("准客户管理", "/customer/customerlist.jsp");
		surlMap_1.put("活动管理", "/shedule/shedulelist.jsp");
		surlMap_1.put("公告管理", "/notice/noticelist.jsp");
		//c_1.setSurlMap(surlMap_1);
		
		Competence c_2 = new Competence();
		c_2.setId("2");
		c_2.setName("数据引擎");
		c_2.setUrl(null);
		HashMap<String,String> surlMap_2 = new HashMap<String,String>();
		surlMap_2.put("提醒模板管理", "/temple/templeList.jsp");
		//c_2.setSurlMap(surlMap_2);
		
		Competence c_3 = new Competence();
		c_3.setId("3");
		c_3.setName("微信后台管理");
		c_3.setUrl(null);
		HashMap<String,String> surlMap_3 = new HashMap<String,String>();
		surlMap_3.put("消息管理", "/message/messagelist.jsp");
		//c_3.setSurlMap(surlMap_3);
		
		list.add(c_1);
		list.add(c_2);
		list.add(c_3);
		
		session.setAttribute("c_list", list);
		
		return SUCCESS;
	}

}
