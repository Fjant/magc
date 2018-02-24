<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%>
<%@page import="java.io.*"%> 
<%@ page language="java" import="java.util.*,net.sf.ezmorph.bean.MorphDynaBean" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="../css/messageinfo.css">
	<script type='text/javascript' src='../js/jquery.min.js'></script>
	<script type="text/javascript" src="../js/common.js" ></script>
	<script type="text/javascript" src="../js/msg/templateiofo.js"></script>
  </head>
  
  <body>
    
    <!-- 中部内容区 START -->
    <div class="EditBox">
        <input type="hidden" name="search_id" id="search_id" />
		<p>
			<b style="display:block;float:left;">*</b>
			<span style="display:block;float:left;">模板名称：</span>
			<input style="display:block;float:left;width: 180px;height:25px;" type="text" id="name" maxlength="20"/>
		</p>
		<div class="cc"></div>
		<p>
			<b style="display:block;float:left;width:10px;height:30px;">*</b>
			<span style="display:block;float:left;">模板内容：</span>
			<textarea style="display:block;float:left;margin:0;width: 180px;font-size:13px;" placeholder="输入字数不能超过500!"  id="content"  rows="10" cols="32" onKeyDown="if (this.value.length>500){value=value.substr(0,500);}"></textarea>
		</p>
		<div class="cc"></div>
		<p style="margin-top:20px;">
			<button onclick="subform()">保存</button>
		</p>
        
        
    </div>
    <!-- 中部内容区 END -->
  </body>
</html>
