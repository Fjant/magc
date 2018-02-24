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
    <script type="text/javascript" src="../js/msg/entMsglist.js"></script>
    <script type="text/javascript" src="../js/pgkk-kkpager/src/kkpager.js"></script>
    <link rel="stylesheet" href="../js/pgkk-kkpager/src/kkpager_orange.css" type="text/css"></link>
    <script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="../js/CalendarPlug-in/need/laydate.css" type="text/css"></link>
  </head> 
  <body>
    <!-- 页面顶部 START -->
    <div class="SalesmanListTop">
        <p>
	        <span>营销员工号</span><input type="text" id="userId" maxlength="30"/>
	        <span>营销员姓名</span><input type="text" id="userName" maxlength="16"/>
	        <span>发送类型</span>
	           <select id="sendType">
		        	<option value=""></option>
		        	<option value="">全部</option>
		        	<option value="1">直接发送</option>
		        	<option value="2">队列发送</option>
		        </select>
	        <span>消息状态</span>
		        <select id="statusId">
		        	<option value=""></option>
		        	<option value="">全部</option>
		        	<option value="0">未发送</option>
		        	<option value="1">已发送</option>
		        </select>
	        <br>
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
            <tr class="topTr">
            	<td colspan="11">
            	    <div class="topTrRight">
            	        <a onclick="exportRrport()">导出消息</a>
            	        <a onclick="deleteMsg(-1)">删除</a>
            	    </div>
            	</td>
           	</tr>
            <tr class="titleTr">
            	<th style="width:5%;"></th>
                <th style="width:10%;">营销员工号</th>
                <th style="width:8%;">营销员姓名</th>
                <th style="width:10%;">模板编号</th>
                <th style="width:10%;">消息类型</th>
                <th style="width:7%;">消息状态</th>      
                <th style="width:10%;">发送结果</th>
                <th style="width:10%;">发送类型</th>
                <th style="width:10%;">重复发送次数</th>
                <th style="width:10%;">发送时间</th>
                <th style="width:10%;">操作</th>
            </tr>
        </table>
        <div id="entMsglist"></div>
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
