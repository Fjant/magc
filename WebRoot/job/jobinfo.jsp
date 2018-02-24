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
	<script type="text/javascript" src="../js/job/jobinfo.js"></script>
	<script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
  </head>
  
  <body>
  <!-- 编辑录入区域START -->
    <div class="EditBox">
    		<input type="hidden" name="search_id" id="search_id"/> 
        <p><b>*</b><span>任务名称：</span><input style="width:180px;height:25px;" type="text" id="name"/></p>              
        <p><b>*</b><span>任务组：</span><input style="width:180px;height:25px;" type="text" id="group"/></p>                                   
        <p><b>*</b><span>类名：</span><input style="width:180px;height:25px;" type="text" id="classPath"/></p>                                   
        <p><b>*</b><span>方法名：</span><input style="width:180px;height:25px;" type="text" id="method"/></p>                                   
        <p><b></b><span>方法参数：</span><input style="width:180px;height:25px;" type="text" id="argument"/>(多个参数用逗号[,]隔开)</p>    
        <p><b>*</b><span>运行时间：</span><input style="width:180px;height:25px;" type="text" id="cronExpression"/></p> 
        <p><b>*</b><span>增量类型：</span><input style="width:180px;height:25px;" type="text" id="incTypeId"/></p>                               
        <div class="cc"></div>
        <p><b style="display:block;float:left;width:10px;height:30px;"></b>
           <span style="display:block;float:left;">并行任务：</span>    
					<select  style="width: 180px;height:25px;display:block;float:left;" id="isConcurrent">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
        </p>                              
        <div class="cc"></div> 
        <p><b style="display:block;float:left;width:10px;height:30px;">*</b>
           <span style="display:block;float:left;">任务状态：</span>     
					<select  style="width: 180px;height:25px;display:block;float:left;" id="statusId">
						<option value="0">停止</option>
						<option value="1">运行</option>
					</select>
        </p>                                          
        <div class="cc"></div>
         <p><b style="display:block;float:left;width:10px;height:30px;"></b><span style="display:block;float:left;">备注：</span>
                <textarea style="display:block;float:left;margin:0;width:180px; font-size:13px;" id="remark" placeholder="输入字数不能超过100字!" name="content" rows="5" cols="32" onKeyDown="if (this.value.length>100){value=value.substr(0,100);}"></textarea>                        
        </p>
        <div class="cc"></div>
        <p style="margin-top:20px;"><button onclick="subform()">保存</button></p>
        
    </div>
    <!-- 编辑录入区域END -->
  </body>
</html>
