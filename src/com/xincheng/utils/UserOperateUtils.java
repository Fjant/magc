package com.xincheng.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.xincheng.login.model.UserSession;

public class UserOperateUtils {
	private static final Logger logger = LoggerFactory.getLogger(UserOperateUtils.class);

	public static void removeUserFromSession() {
		Struts2Utils.getSession().removeAttribute("UserSession");
	}

	public static UserSession getUser() {
		Object session = Struts2Utils.getSession().getAttribute("UserSession");
		return session == null ? null : (UserSession) session;
	}

	public static String getUserId() {
		UserSession user = getUser();
		if (user != null) {
			return user.getAgntNum();
		}
		return null;
	}
}
