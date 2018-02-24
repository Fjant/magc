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

<% 
java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy"); 
java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("yyyy年M月d日 EEEE"); 
java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
String strYear = yearFormat.format(currentTime); 
String strTime = timeFormat.format(currentTime);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<title>信诚销售人员管理系统</title>
	<link rel="stylesheet" type="text/css" href="../css/master.css">
	<script type="text/javascript" src="../js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="../js/master.js"></script>
  </head>
  
  <body>
    <!-- 页面顶部 START -->
    <div id="masterTop" class="masterTop">
        <div class="masterTopLogo"></div>
        <div class="masterTopGrayBg"><i></i><u></u></div>
        <div class="masterTopText"></div>
    </div>
    <div id="masterTopInfo" class="masterTopInfo">
        <p><b>当前用户：</b><span>${sessionScope.UserSession == null ? "未知用户" : sessionScope.UserSession.getAgntNum()}</span><b>姓名：</b><span>【${sessionScope.UserSession == null ? "未知用户" : sessionScope.UserSession.getFullName()}】</span> <a href="../uamsLogin/uamsAction!doLogoff.action">退出系统</a><em>|</em><span><%=strTime%></span></p>
    </div>
    <!-- 页面顶部 END -->
    
    <!-- 页面中部 START -->
    <div id="masterMid" class="masterMid">
        <!-- 左边菜单栏START -->
        <div class="leftNav">
            <div class="leftNavTitle"><h2><i></i><span>后台系统管理菜单</span></h2></div>
            <div id="leftNavContent" class="leftNavContent">
            	<div class="leftNavSubitem">
            		<c:if test="${sessionScope.c_menuList != null && sessionScope.c_menuList.size() > 0}">
		                <c:forEach items="${sessionScope.c_menuList}" var="menu" varStatus="status">
		                	<h3 tid="menu1${status.index}"><i></i><span>${menu.name}</span></h3>
			                <ul id="menu1${status.index}">
			                	<c:forEach items="${menu.subMenus}" var="scp" varStatus="sta">
				                    <li><a onclick="showFrame('../${scp.url}')">${scp.name}</a></li>
			                	</c:forEach>
			                </ul>
		                </c:forEach>
	                </c:if>
	                <c:if test="${sessionScope.c_menuList == null || sessionScope.c_menuList.size() <= 0}">
	                		<h3 tid="menu1" ><i></i><span>权限检查</span></h3>
		                    <ul id="menu1">
		                        <li><a class="cur">联系管理员</a></li>
		                    </ul>
	                </c:if>
                </div>
            </div>
            <div class="leftNavBottom"></div>
        </div>
        <!-- 左边菜单栏 END -->
        
        <!-- 右边内容 START -->
        <div id="rightContent" class="rightContent">
            <div class="rightContentTitle"><h2><i></i><span>当前位置：后台管理系统  &gt 营销员基础平台管理 &gt 营销员管理</span></h2></div>
            <div class="rightContentBody">
                <iframe id="rightContentBodyIframe" src="../jsp/error.jsp" height="100%" width="100%"  frameborder="0"></iframe>
            </div>
        </div>
        <!-- 右边内容 END -->
        <div class="cc"></div>
    </div>
    <!-- 页面中部 END -->
    
    <!-- 页面底部 START -->
    <div id="masterBottom" class="masterBottom">
        <p>@<%=strYear%>信诚营销人员管理系统</p>
    </div>
    <!-- 页面底部 END -->
  </body>
</html>
