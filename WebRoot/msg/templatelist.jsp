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
	<link rel="stylesheet" type="text/css" href="../css/exportMsg.css">
	<script type='text/javascript' src='../js/jquery.min.js'></script>
	<script type="text/javascript" src="../js/common.js" ></script>
    <script type="text/javascript" src="../js/msg/templatelist.js"></script>
    <script type="text/javascript" src="../js/pgkk-kkpager/src/kkpager.js"></script>
    <link rel="stylesheet" href="../js/pgkk-kkpager/src/kkpager_orange.css" type="text/css"></link>
    <script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="../js/CalendarPlug-in/need/laydate.css" type="text/css"></link>
  </head>
  
  <body>
    <!-- 页面顶部 START -->
    <div class="SalesmanListTop">
        <p>
	        <span>模板名称</span><input type="text" id="name" maxlength="30"/>
	        <span>创建时间</span>
	        <input id="createStartTime" placeholder="请输入日期" class="laydate-icon" onclick="WdatePicker()">
	        <span>—</span>
	        <input id="createEndTime" placeholder="请输入日期" class="laydate-icon" onclick="WdatePicker()"> 
	        <button onclick="ajaxSearch()" style="cursor:pointer;">搜索</button>
        </p>
    </div>
    <!-- 页面顶部 END -->
    
    <!-- 中部内容区 START -->
    <div class="SalesmanListContent">
        <table class="titleTable">
            <tr class="topTr">
            	<td colspan="9">
            	    <div class="topTrRight">
            	        <a onclick="updateOrAdd(0)">新增消息</a>
            	        <a onclick="exportRrport()">导出消息</a>
            	        <a onclick="deleteMsg(-1)">删除</a>
            	    </div>
            	</td>
           	</tr>
            <tr class="titleTr">
            	<th style="width:5%;"></th>
                <th style="width:10%;">模板名称</th>
                <th style="width:10%;">创建者</th>
                <th style="width:10%;">创建时间</th>
                <th style="width:15%;">操作</th>
            </tr>
        </table>
        <div id="templatelist"></div>
    </div>
    <!-- 中部内容区 END -->
    <div class="z_div  z_importMember" id="z_exportMember">
		<h3>导出消息</h3>
		<div id="drtsy">正在生成，请稍等。。。</div>
		<div id="drtsy1">点击下载！</div>
		<div>
			<input type="button" style="width: 50px; height: 26px;" value="关闭" class="z_btn z_button2" onclick="closeExport()" />
		</div>
	</div>
    <!-- 分页 START -->
	<div style="width:100%;margin:0 auto;">
		<div id="kkpager"></div>
	</div>
    <!-- 分页 END -->
  </body>
</html>
