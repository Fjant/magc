package com.xincheng.competence;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xincheng.encrypt.EncryptImpl;
import com.xincheng.encrypt.EncryptIntf;

public class CustomerMsgFilter implements Filter{
	
	Logger logger = LoggerFactory.getLogger(CustomerMsgFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = ((HttpServletRequest) request);
		HttpServletResponse res = ((HttpServletResponse) response);
		HttpSession session = req.getSession();
		String reqURL = req.getRequestURL().toString();
		
		EncryptIntf encript = new EncryptImpl("MSGCWX2016");
		try {
			String newToken = null;
			String token = req.getParameter("token");					 
		    newToken = encript.deCodeAES(token.trim());
		    int index = newToken.indexOf("@");
			int index1 = newToken.indexOf("@",newToken.indexOf("@")+1);
			String jobName = newToken.substring(0, index); 
			String userId = newToken.substring(index+1, index1);
			logger.info("访问成功，JobName:"+jobName+",访问账号："+userId+",访问时间："+new Date());
			if(newToken != null){
				req.getRequestDispatcher("/customer/customerMsglist.jsp").forward(request, response);
				return;
			}
		} catch (Exception e) {
			logger.error("您没有权限访问该功能！！");
			req.getRequestDispatcher("/jsp/NoPermission.jsp").forward(request, response);// 您无权使用该功能
			return;
		}
			
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
