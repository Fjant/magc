jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
localStorage.setItem("globalUrl", "/msgc");//本地
SCH = 0;// 屏幕高度
SCW = 0;// 屏幕宽度
$(function() {
	getSCSize();
	initMyAlert();
	initMyConfirm();
	$.extend({
		doPost : function(url, data) {
			try {

				var returnData;
				$.ajax({
					type : 'POST',
					url : url,
					data : data,
					async : false,
					success : function(response) {
						if (response.returnData) {
							returnData = response.returnData;
						} else {
							returnData = "";
						}
					},
					dataType : "json"
				});
				return returnData;
			} catch (e) {
				if (jsDebugMode == 1) {
					myAlert(e.name + ":" + e.message);
				}
			}
		}
	});

	$.extend({
		doPostAsync : function(url, data, success) {
			$.ajax({
				type : 'POST',
				url : url,
				data : data,
				async : true,
				dataType : "json",
				success : function(response) {
					var returnData;
					if (response.returnData) {
						returnData = response.returnData;
					} else {
						returnData = "";
					}

					success(returnData);
				},
				error : function(e) {
					myAlert(e.name + ":" + e.message);
				}
			});
		}
	});

	$.extend({
		mygetJson : function(url, data) {
			try {
				var returnData;
				$.ajax({
					type : 'GET',
					url : url,
					data : data,
					async : false,
					success : function(response) {
						if (response.returnData) {
							returnData = response.returnData;
						} else {
							returnData = "";
						}
					},
					dataType : "json"
				});
				return returnData;
			} catch (e) {
				if (jsDebugMode == 1) {
					myAlert(e.name + ":" + e.message);
				}
			}
		}
	});

});

/********************* 自定义的弹框函数 *********************/
function initMyAlert()
{
	try{
		var myAlertBg = "<div id='myAlertBg' style='position:fixed;display:none;z-index:9999;width:100%;height:100%;background:#666;filter:alpha(opacity=50);-khtml-opacity: 0.5;opacity: 0.5;'></div>";
		var myAlertDialog = "<div id='myAlertDialog' style='position:fixed;display:none;z-index:10000;width:70%;margin-left:15%;margin-top:35%;background:#fff;border:5px solid rgba(0,0,0, 0.1);border-radius:5px;'>"
			+"<div class='myAlertDialogTitle' style='background:#9D9D9D;height:40px;width:100%;'><p style='height:40px;line-height:40px;'><b style='display:block;float:left;color:#fff;font-size:16px;margin-left:15px;'>温馨提示</b></p></div>"
			+"<p id='myAlertContent' style='width:100%;margin:20px auto;line-height:1.5em;font-size:1.25em;color:#333;text-align:center;'>测试一下</p>"
			+"<p style='height:30px;width:100%;padding:10px 0 20px 0;text-align:center;'><span onclick='closeMyAlert();'; style='display:inline-block;width:100px;height:30px;line-height:30px;font-size:1.25em;text-align:center;background:#f64d48;border-radius:5px;color:#fff;'>确定</span></p>"
			+"</div>";
		$("body").prepend(myAlertBg + myAlertDialog);	
	}
	catch(e)
    {
    	if(jsDebugMode == 1){
    	    myAlert(e.name + ":" + e.message);
    	}
    }
}

function myAlert(showText)
{
	try{
		$("#myAlertContent").html(showText);
		$("#myAlertDialog").css({
			"margin-top":(SCH - $("#myAlertDialog").height())/2
		});
		$("#myAlertBg").show();
		$("#myAlertDialog").show();
	}
	catch(e)
    {
    	if(jsDebugMode == 1){
    	    myAlert(e.name + ":" + e.message);
    	}
    }
}

function closeMyAlert()
{
	try{
		$("#myAlertBg").hide();
		$("#myAlertDialog").hide();
	}
	catch(e)
    {
    	if(jsDebugMode == 1){
    	    myAlert(e.name + ":" + e.message);
    	}
    }
}

function initMyConfirm()
{
	try{
		var myConfirmBg = "<div id='myConfirmBg' style='position:fixed;display:none;z-index:9999;width:100%;height:100%;background:#666;filter:alpha(opacity=50);-khtml-opacity: 0.5;opacity: 0.5;'></div>";
		var myConfirmDialog = "<div id='myConfirmDialog' style='position:fixed;display:none;z-index:10000;width:70%;margin-left:15%;margin-top:35%;background:#fff;border:5px solid rgba(0,0,0, 0.1);border-radius:5px;'>"
			+"<div class='myConfirmDialogTitle' style='background:#9D9D9D;height:40px;width:100%;'><p style='height:40px;line-height:40px;'><b style='display:block;float:left;color:#fff;font-size:16px;margin-left:15px;'>温馨提示</b></p></div>"
			+"<p id='myConfirmContent' style='width:100%;margin:20px auto;line-height:1.5em;font-size:1.25em;color:#333;text-align:center;'>测试一下</p>"
			+"<p style='height:30px;width:100%;padding:10px 0 20px 0;text-align:center;'><span id='myConfirmTrue' style='display:inline-block;width:100px;height:30px;line-height:30px;font-size:1.25em;text-align:center;background:#f64d48;border-radius:5px;color:#fff;'>确定</span><span onclick='myConfirmFalse();'; style='display:inline-block;width:100px;height:30px;line-height:30px;font-size:1.25em;text-align:center;background:#eee;border-radius:5px;color:#333;margin-left:20px;'>取消</span></p>"
			+"</div>";
		$("body").prepend(myConfirmBg + myConfirmDialog);	
	}
	catch(e)
    {
    	if(jsDebugMode == 1){
    	    myAlert(e.name + ":" + e.message);
    	}
    }
}

function myConfirm(showText,success)
{
	try{
		$("#myConfirmContent").html(showText);
		$("#myConfirmDialog").css({
			"margin-top":(SCH - $("#myConfirmDialog").height())/2
		});
		$("#myConfirmBg").show();
		$("#myConfirmDialog").show();
		$("#myConfirmTrue").bind("click",function()
		{
			try{
				$("#myConfirmBg").hide();
				$("#myConfirmDialog").hide();
				success();
			}
			catch(e)
		    {
		    	if(jsDebugMode == 1){
		    	    myAlert(e.name + ":" + e.message);
		    	}
		    }
			
		});
	}
	catch(e)
    {
    	if(jsDebugMode == 1){
    	    myAlert(e.name + ":" + e.message);
    	}
    }
}

function myConfirmFalse()
{
	try{
		$("#myConfirmBg").hide();
		$("#myConfirmDialog").hide();
		return false;
	}
	catch(e)
    {
    	if(jsDebugMode == 1){
    	    myAlert(e.name + ":" + e.message);
    	}
    }
}

function getSCSize() {
    if (window.innerHeight) {
        SCH = window.innerHeight;
    } else if (document.body.clientHeight) {
        SCH = document.body.clientHeight;
    }
    if (window.innerWidth) {
        SCW = window.innerWidth;
    } else if (document.body.clientWidth) {
        SCW = document.body.clientWidth;
    }
}
