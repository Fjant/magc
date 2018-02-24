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
	
	<link rel="stylesheet" type="text/css" href="../css/messagelist.css">
	<script type='text/javascript' src='../js/jquery.min.js'></script>
	<script type="text/javascript" src="../js/common.js" ></script>
    <script type="text/javascript" src="../js/msg/sumMsglist.js"></script>
    <script type="text/javascript" src="../js/pgkk-kkpager/src/kkpager.js"></script>
    <link rel="stylesheet" href="../js/pgkk-kkpager/src/kkpager_orange.css" type="text/css"></link>
    <script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="../js/CalendarPlug-in/need/laydate.css" type="text/css"></link>
  </head> 
  <body>
    <!-- 页面顶部 START -->
    <div class="SalesmanListTop">
        <p>
	        <span>发送时间</span>
	        <input id="sendStartTime" placeholder="请输入日期" class="laydate-icon" onclick="WdatePicker()">
	        <span>—</span>
	        <input id="sendEndTime" placeholder="请输入日期" class="laydate-icon" onclick="WdatePicker()"> 
	        <button onclick="ajaxSearch()" style="cursor:pointer;">搜索</button>
        </p>
    </div>
    <!-- 页面顶部 END -->
    
    <!-- 中部内容区 START -->
    <div class="SalesmanListContent">
        <table class="titleTable">
            <tr class="titleTr">
                <th style="width:15%;">消息类型名称</th>
                <th style="width:14%;">消息总数量</th>
                <th style="width:14%;">发送成功数量</th>
                <th style="width:14%;">发送失败数量</th>
                <th style="width:14%;">待发送数量</th>
                <th style="width:14%;">成功率</th>
                <th style="width:14%;">失败率</th>      
            </tr>
        </table>
        <div id=sumMsglist></div>
    </div>
    <!-- 中部内容区 END -->
  </body>
</html>
