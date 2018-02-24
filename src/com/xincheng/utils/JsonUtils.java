package com.xincheng.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xincheng.ajax.AjaxResData;

public class JsonUtils {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private static ObjectMapper mapper = new ObjectMapper();

	  public static void renderJsonResponse(HttpServletResponse paramHttpServletResponse, Object paramObject) {
	    try {
	      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      mapper.getSerializationConfig().setDateFormat(localSimpleDateFormat);
	      mapper.writeValue(paramHttpServletResponse.getWriter(), paramObject);
	      logger.debug("RenderJosnUtils.renderJson:" + mapper.writeValueAsString(paramObject));
	    }catch (IOException localIOException){
	      throw new IllegalArgumentException(localIOException);
	    }
	  }
	  public static void renderTextResponse(HttpServletResponse paramHttpServletResponse, Object paramObject) {
		    try {
		      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      mapper.getSerializationConfig().setDateFormat(localSimpleDateFormat);
		      mapper.writeValue(paramHttpServletResponse.getWriter(), paramObject);
		      logger.debug("RenderJosnUtils.renderJson:" + mapper.writeValueAsString(paramObject));
		    }catch (IOException localIOException){
		      throw new IllegalArgumentException(localIOException);
		    }
		  }

	  public static void renderText(Object paramObject){
		HttpServletResponse localHttpServletResponse = ServletActionContext.getResponse();
		localHttpServletResponse.setContentType("text/html;charset=UTF-8");
		localHttpServletResponse.setDateHeader("Expires", 1L);
		localHttpServletResponse.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		localHttpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");
		localHttpServletResponse.setHeader("Pragma", "no-cache");
		renderTextResponse(localHttpServletResponse, paramObject);
	  }
	  
	  public static void renderJson(Object paramObject){
			HttpServletResponse localHttpServletResponse = ServletActionContext.getResponse();
			localHttpServletResponse.setContentType("application/json;charset=UTF-8");
			setDisableCacheHeader(localHttpServletResponse);
		    renderJsonResponse(localHttpServletResponse, paramObject);
		  }
	  
	  

		
	  public static void setDisableCacheHeader(HttpServletResponse paramHttpServletResponse){
	    paramHttpServletResponse.setDateHeader("Expires", 1L);
	    paramHttpServletResponse.setContentType("text/x-json;charset=utf-8");
	    paramHttpServletResponse.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
	    paramHttpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");
	    paramHttpServletResponse.setHeader("Pragma", "no-cache");
	  }
	  
	  

}
