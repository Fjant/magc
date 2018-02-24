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
    <script type="text/javascript" src="../js/customer/customerMsglist.js"></script>
    <script type="text/javascript" src="../js/pgkk-kkpager/src/kkpager.js"></script>
    <link rel="stylesheet" href="../js/pgkk-kkpager/src/kkpager_orange.css" type="text/css"></link>
    <script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="../js/CalendarPlug-in/need/laydate.css" type="text/css"></link>
  </head>
  
  <body>
    <!-- 页面顶部 START -->
    <div class="SalesmanListTop">
    		<p>
			<table class="tableContent">
			    <tr class="trContent">
				    <th>客户号</th>
				    <th><input type="text" id="customerNo" maxlength="30"/></th>
				    <th>触发保单号</th>
				    <th><input type="text" id="billNo" maxlength="30"/></th>
				    <th>证件号</th>
				    <th><input type="text" id="secuityNo" maxlength="30"/></th>
				    <th>处理状态</th>
					<th>
						<select id="statusId">
			        		<option value="">全部</option>
			        		<option value="1">已发送</option>
			        		<option value="0">未发送</option>
	        	       </select>
					</th>
			    </tr>
			    <tr class="trContent">
					    <th>查询日期</th>  
					    <th><input id="sendStartTime" placeholder="请输入开始日期" class="laydate-icon" onclick="WdatePicker()"></th>  
					    <th>—</th>  
					    <th><input id="sendEndTime" placeholder="请输入结束日期" class="laydate-icon" onclick="WdatePicker()"></th>  
					    <th><button onclick="ajaxSearch()" style="cursor: pointer;">搜索</button></th>  
			    </tr>
		    </table> 
		</p>
    </div>
    <!-- 页面顶部 END -->
    
    <!-- 中部内容区 START -->
    <div class="SalesmanListContent">
        <table class="titleTable">
            <tr class="topTr">
            	<td colspan="9">
            	    <div class="topTrLeft">
            	        <span >消息总计:</span>
            	        <span id="totalCount"></span>
            	    </div>
            	</td>
           	</tr>
            <tr class="titleTr">
                <th style="width:5%;">序号</th>
                <th style="width:10%;">客户号</th>
                <th style="width:10%;">触发保单号</th>
                <th style="width:15%;">推送时间</th>
                <th style="width:20%;">服务类型</th>
                <th style="width:10%;">微信推送内容</th>
                <th style="width:10%;">处理状态</th>
                <th style="width:10%;">接口响应代码</th>
                <th style="width:10%;">接口响应描述</th>
            </tr>
        </table>
        <div id="customerMsglist"></div>
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
    <div><p  style=" padding-left: 20px;">备注:信息发送不成功,接口响应代码的说明请点击<a href="http://mp.weixin.qq.com/wiki/10/6380dc743053a91c544ffd2b7c959166.html" target="_blank">查询</a>，查询地址若有变化，请在搜索引擎搜索“微信公众平台开发者文档”，查询“开始前必读”->“接口返回码说明"</p></div>
  </body>
</html>
