<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!-- saved from url=(0040)http://128.232.9.199:9080/uams/login.jsp -->
<HTML><HEAD><TITLE>信诚人寿营销员管理系统</TITLE>
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="<c:url value='../js/ajax.js'/>"></script>
<script type="text/javascript" src="<c:url value='../js/menuTree.js'/>"></script>

<SCRIPT language="javascript">

<!--
	history.forward(1);

	function onKeyDown222() {
		if (((event.keyCode == 8) && ((event.srcElement.type != 'text' || event.srcElement.type == 'text'
				&& event.srcElement.readOnly)
				&& (event.srcElement.type != 'textarea' || event.srcElement.type == 'textarea'
						&& event.srcElement.readOnly) && (event.srcElement.type != 'password' || event.srcElement.type == 'password'
				&& event.srcElement.readOnly)))
				|| ((event.ctrlKey) && ((event.keyCode == 78) || (event.keyCode == 82)))
				|| (event.keyCode == 116)) {
			event.keyCode = 0;
			event.returnValue = false;
		}
	}
	document.onkeydown = onKeyDown222;

	if (self != top) {
		window.parent.document.location.href = document.location.href;
	}
	/*
	 function createXMLHttpRequest(){
	 var xmlHttp = false;
	 try {
	 xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	 } catch (e) {
	 try {
	 xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 } catch (e2) {
	 xmlHttp = false;
	 }
	 }
	 if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
	 xmlHttp = new XMLHttpRequest();
	 }
	 if (!xmlHttp && typeof XMLHttpRequest != 'undefined'){

	 }
	 return xmlHttp;
	 }
	 */
	function keydownLogin() {
		if (event.keyCode == 13) {
			doLogin();
		}
	}
	document.onkeydown = keydownLogin;
	function doLogin() {

		document.getElementsByName('userName')[0].value = (document
				.getElementsByName('userName')[0].value + "").toUpperCase();
		document.forms[0].submit();
		/*
		 document.all.result.innerText="";
		 document.all.msg.style.display="inline";//inline
		 var url = "<c:url value='/loginAction.do'/>";
		 var pars = "actionMethod=doLogin&soFlag=1&sso=FROMAPP&userName="+document.all.userName.value+"&password="+document.all.password.value;
		 var resmsg = "";
		 var ajax = createXMLHttpRequest();
		 if(!ajax){return;}
		 ajax.open("POST",url+"?"+pars,false);
		 ajax.onreadystatechange = 
		 function (){
		 if(ajax.readyState == 4){
		 if(ajax.status == 200){
		 resmsg = ajax.responseText;
		 }
		 }
		 } 
		 ajax.send(null); 
		 if (parseInt(resmsg) != 1){
		 switch ( parseInt(resmsg) ){
		 case -1:
		 document.all.result.innerText="账号已被停用!";
		 //new Effect.Appear("msg");
		 break;
		 case -2:
		 document.all.result.innerText="账号已被锁定!";
		 //new Effect.Appear("msg");
		 break;
		 case -3:
		 document.all.result.innerText="账号密码已过期!";
		 //new Effect.Appear("msg");
		 break;
		 case -4:
		 document.all.result.innerText="密码错误!";
		 //new Effect.Appear("msg");
		 break;
		 case -5:
		 document.all.result.innerText="账号不存在!";
		 //new Effect.Appear("msg");
		 break;
		 case -10:
		 document.all.result.innerText="账号密码输入错误3次，已被锁定!";
		 //new Effect.Appear("msg");
		 break;
		 case -99:
		 document.all.result.innerText="不能同时以两种方式登陆!";
		 //new Effect.Appear("msg");
		 break;		
		 case -100:
		 document.all.result.innerText="系统异常!";
		 //new Effect.Appear("msg");
		 break;	
		 }			
		 }else{
		 //document.location.href="welcome.jsp";
		 document.forms[0].submit();
		 }
		 */
	}
// -->
</SCRIPT>

<STYLE type=text/css>BODY {
	MARGIN: 0px
}
</STYLE>

<STYLE type=text/css>.style1 {
	COLOR: #990000
}
.STYLE5 {
	FONT-WEIGHT: bold; COLOR: #1f2c8a
}
.STYLE3 {
	COLOR: #ff7302
}
</STYLE>
<LINK href="<c:url value='/css/login.css'/>" type=text/css rel=stylesheet>
</HEAD>
<BODY text="#000000" bgColor="#ffffff" leftMargin="0" topMargin="0"  marginwidth="0" marginheight="0">


<form action="<c:url value="/uamsLogin/uamsAction!loginSSO.action"/>" method="post">
<input type="hidden" name="openUrl" value="${param.openUrl}"/>
<input type="hidden" name="menuId" value="${param.menuId}"/>
<TABLE class=head_right id=pageTop style="MARGIN: 0px" cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD class=head_left vAlign=bottom width="100%" height=0>
      <TABLE style="MARGIN: 0px" cellSpacing=0 cellPadding=0 width="100%"       bgColor=white border=0>
        <TBODY>
        <TR>
          <TD width=228 bgColor=white height=87 rowSpan=2><IMG 
            style="MARGIN: 0px" src="<c:url value='/img/head_left.jpg'/>" align=center 
            border=0> </TD>
          <TD 
          style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr=white, EndColorStr=#DDDDDD)" 
          noWrap align=middle height=57><IMG style="MARGIN: 0px" src="<c:url value='/img/system_name.gif'/>" align=center border=0>
          </TD>
          <TD noWrap width=100 bgColor=#dddddd ></TD>
        </TR>
        <TR>
          <TD bgColor=#f40500 height=30></TD>
          <TD width=200 bgColor=#f40500 align="right" valign="bottom"><font COLOR="white" STYLE="font-weight:bold">${versionNum }</font>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
        </TR>
        </TBODY>
       </TABLE>
     </TD>
   </TR>
   </TBODY>
</TABLE>
         
<TABLE cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
  <TR>
    <TD height=3></TD></TR>
  <TR>
    <TD bgColor=#e6eaed height=2></TD></TR>
  <TR>
    <TD>&nbsp;</TD></TR></TBODY></TABLE>

<TABLE cellSpacing="0" cellPadding="8" width="100%">
  <TBODY>
  <TR>
    <TD vAlign="top" width="250" height="422">
      <TABLE height="179" cellSpacing="0" cellPadding="0" width="100%">
        <TBODY>
        <TR>
          <TD width="5%" height="177">&nbsp;</TD>
          <TD vAlign="top" width="95%">
            <TABLE cellSpacing="0" cellPadding="0" width="1003%">
              <TBODY>
              <TR>
                <TD vAlign="top" background="<c:url value='/img/dl_di.png'/>" height="153">
                  <TABLE cellSpacing=0 cellPadding=0 width=200>
                    <TBODY>
                    <TR>
                      <TD colSpan="2" height="24">&nbsp;</TD></TR>
                    <TR>
                      <TD height=20>&nbsp;</TD>
                      <TD class="txt_grey1" width="94%">用户名：</TD></TR>
                    <TR>
                      <TD width="6%" height=25>&nbsp;</TD>
                      <TD class="txt_grey1">
                      <input type="text" autocomplete="off"
                      style="text-transform:uppercase;BORDER-RIGHT: #c8c8c8 1px solid; BORDER-TOP: #c8c8c8 1px solid; BORDER-LEFT: #c8c8c8 1px solid; WIDTH: 182px; BORDER-BOTTOM: #c8c8c8 1px solid; BACKGROUND-COLOR: #ffffff" 
                      size="25" 
                      value="" 
                      name="userName" 
                      onkeydown='keydownLogin()'
                      
                      /></TD></TR>
                      <script>document.getElementsByName('userName')[0].focus();</script>
                    <TR>
                      <TD height=19>&nbsp;</TD>
                      <TD class="txt_grey1" height="19">密&nbsp;&nbsp;码：</TD></TR>
                    <TR>
                      <TD height=23>&nbsp;</TD>
                      <TD class=txt_grey1 height=23>
                      <input type="password" autocomplete="off"
                      style="BORDER-RIGHT: #c8c8c8 1px solid; BORDER-TOP: #c8c8c8 1px solid; BORDER-LEFT: #c8c8c8 1px solid; WIDTH: 182px; BORDER-BOTTOM: #c8c8c8 1px solid; BACKGROUND-COLOR: #ffffff" 
                      size="25" 
                      value="" 
                      name="password"
                      onkeydown='keydownLogin()'/></TD></TR>
                    <TR>
                      <TD height=40>&nbsp;</TD>
                      <TD height=40>
                        <TABLE cellSpacing=0 cellPadding=0 width="100%">
                          <TBODY>
                          <TR>
                            <TD class=txt_red width="59%">
                                <!--  <DIV id="msg" style="DISPLAY: none" align=center>-->
                                    <SPAN id="result">${loginErrMsg}</SPAN>
                                    
                                <!-- </DIV>-->
                            </TD>
                            <TD align=right width="41%">
                               <!-- <A onclick="javascript:doLogin();" href="#" id="submitA">
	                                <IMG  height="18" src="<c:url value='/images/login/battom_dl.png'/>" width="41" border="0" >
                                </A> --> 
                            <input type="button" style="border:none;background-image:url(../img/battom_dl.png) ;width:40px" value="" onclick="doLogin()"/> 
                            
                            </TD>
                          </TR>
                          </TBODY>
                        </TABLE>
                      </TD>
                    </TR>
                  </TBODY>
                </TABLE>
              </TD>
            </TR>
            </TBODY>
          </TABLE>
        </TD>
      </TR>
      </TBODY>
    </TABLE>
    </TD>
    
    <TD vAlign="top" width="694">
      <TABLE cellSpacing="0" cellPadding="0" width="684">
        <TBODY>
        <TR>
          <TD vAlign="top" align="left" colSpan="3"><IMG height="34"
            src="<c:url value='/img/xtsm.png'/>" width="684"></TD></TR>
        <TR>
          <TD class="txt_black" vAlign="middle" align="left" colSpan="3"
            height="76">本系统集成消息管理、任务管理、活动统计等功能。</TD></TR>
        <TR>
          <TD vAlign="top" align="left" colSpan="3" height="69"><IMG height="227" 
            src="<c:url value='/img/xtsm1.png'/>" width="684"></TD></TR>
        <TR>
          <TD vAlign="top" align="left" width="17" height="25"><IMG height="29" 
            src="<c:url value='/img/corner3.jpg'/>" width="17"></TD>
          <TD width="671" 
          background="<c:url value='/img/corner_line2.jpg'/>" >&nbsp;</TD>
          <TD width="17"><IMG height=29 src="<c:url value='/img/corner4.jpg'/>" 
            width="17"></TD></TR></TBODY></TABLE></TD></TR></TBODY>
</TABLE>

            
<TABLE cellSpacing="0" cellPadding="0" width="100%">
    <TBODY>
    <TR>
	    <TD align="center" bgColor="#6699ff" height="35">&nbsp;&nbsp;
		    <SPAN class="bai_zi">COPYRIGHT © 2008 信诚人寿保险版权所有</SPAN>
		    <SPAN class="bai_zi">&nbsp;&nbsp;</SPAN>
	    </TD>
    </TR>
    </TBODY>
</TABLE>
</form>
<SCRIPT language="javascript">
funcNameNoStatus('');
</SCRIPT>
</BODY>
</HTML>
