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
    <script type="text/javascript" src="../js/job/jobLoglist.js"></script>
    <script type="text/javascript" src="../js/pgkk-kkpager/src/kkpager.js"></script>
    <link rel="stylesheet" href="../js/pgkk-kkpager/src/kkpager_orange.css" type="text/css"></link>
    <script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="../js/CalendarPlug-in/need/laydate.css" type="text/css"></link>
  </head>
  
  <body>
    <!-- 页面顶部 START -->
    <div class="SalesmanListTop">
        <p>
	        <span>任务名称</span><input type="text" id="jobName" maxlength="30"/>
	        <span>运行结果</span>
		        <select id="result">
		        	<option value=""></option>
		        	<option value="0">失败</option>
		        	<option value="1">成功</option>
			     </select>
		     <span>发送时间</span>  
		     	<input id="startTime" placeholder="请输入开始日期" class="laydate-icon" onclick="WdatePicker()">
		     <span>-</span>
		        <input id="endTime" placeholder="请输入结束日期" class="laydate-icon" onclick="WdatePicker()">	         
	        <button onclick="ajaxSearch()" style="cursor:pointer;">搜索</button>
        </p>
    </div>
    <!-- 页面顶部 END -->
    
    <!-- 中部内容区 START -->
    <div class="SalesmanListContent">
        <table class="titleTable">
            <!-- <tr class="topTr">
            	<td colspan="10">
            	    <div class="topTrRight">
            	        <a onclick="updateOrAdd(0)">新增任务</a>
            	        <a onclick="deleteJob(-1)">删除</a>
            	    </div>
            	</td>
           	</tr> -->
            <tr class="titleTr">
                <th style="width:20%;">任务名称</th>
                <th style="width:10%;">总数量</th>
                <th style="width:10%;">成功数量</th>                
                <th style="width:15%;">开始时间</th>
                <th style="width:15%;">结束时间</th>
                <th style="width:10%;">运行结果</th>
                <!-- <th style="width:20%;">备注</th> -->
            </tr>
        </table>
        <div id="jobLoglist"></div>
    </div>
    <!-- 中部内容区 END -->
    <!-- 分页 START -->
	<div style="width:100%;margin:0 auto;">
		<div id="kkpager"></div>
	</div>
    <!-- 分页 END -->
  </body>
</html>
