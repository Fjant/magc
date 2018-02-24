var checkTimeOutXmlHttpRequest;
var forwardTimeOutLoginUrl = '';

//创建一个XMLHttpRequest对象
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
	    alert("无法生成XMLHttpRequest对象！");
	    return false;
	}else{
		var resp="";
		try{
			xmlHttp.open('POST',"<c:url value='/checkTimeOut.do'/>",false);
		    xmlHttp.onreadystatechange = function (){
				if (xmlHttp.readyState==4 && xmlHttp.status==200) {
				    resp=xmlHttp.responseText;
				}
			}; 
			xmlHttp.send(null);
		}catch(e){}
		if(resp!=""){
			var resps = resp.split("##");
			if(resps[0]=='0'){//no login
			    window.location=resps[1];
			}else{// login
			    return xmlHttp;
			}
		}else{
		    alert("服务器异常，请联系系统管理员！");
		    return false;
		}
	}

}

