package com.xincheng.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

public class Struts2Utils {
	public static HttpSession getSession() {
	    return ServletActionContext.getRequest().getSession();
	  }

	  public static HttpSession getSession(boolean bool){
	    return ServletActionContext.getRequest().getSession(bool);
	  }

	  public static Object getSessionAttribute(String key){
	    HttpSession localHttpSession = getSession(false);
	    return localHttpSession != null ? localHttpSession.getAttribute(key) : null;
	  }

	  public static HttpServletRequest getRequest(){
	    return ServletActionContext.getRequest();
	  }

	  public static String getParameter(String key){
	    return getRequest().getParameter(key);
	  }

	  public static HttpServletResponse getResponse(){
	    return ServletActionContext.getResponse();
	  }
}
