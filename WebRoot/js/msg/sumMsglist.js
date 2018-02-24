jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var path = localStorage.getItem("globalUrl");
var postPath = path + "/msg/sumMsg!list.action";
$(function() {
	// 调用初始化数据
	ajaxresult();
	// 加载初始化数据
	function ajaxresult() {
		$('#sumMsglist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
		   ajaxPost(postPath, {}, true);
	  }
   }

);

// 搜索
function ajaxSearch() {
	var sendStartTime = $("#sendStartTime").val();
	var sendEndTime = $("#sendEndTime").val();
	var paramArray = {
		search_sendStartTime : sendStartTime,
		search_sendEndTime : sendEndTime
		
	};
	ajaxPost(postPath, paramArray, true);
}

function ajaxPost(postPath, paramArray, isRefresh) {
	try {
		$.post(postPath, paramArray, function(date) {
			if (date) {
				var res = date["returnData"];	
				var ent = res.ent;
			    var mp = res.mp;
			    var totalNum = parseInt(ent.totalNum)+parseInt(mp.totalNum);
			    var successNum = parseInt(ent.successNum)+parseInt(mp.successNum);
			    var failNum = parseInt(ent.failNum)+parseInt(mp.failNum);
			    var armeNum = parseInt(ent.armeNum)+parseInt(mp.armeNum);
				var strtemp = '<table class="contentTable">';
				if(ent != null && mp != null) {				     
			    		 var temp= '<tr class="listTr">'		    			          			 
				    			 +'<td style="width:15%;">企业号消息</td>' 
							     +'<td style="width:14%;">' +ent.totalNum+ '</td>' 
							     +'<td style="width:14%;">' +ent.successNum+ '</td>'
			    		         +'<td style="width:14%;">' +ent.failNum+ '</td>'
			    		         +'<td style="width:14%;">' +ent.armeNum+ '</td>'
							     +'<td style="width:14%;">' +ent.successRate+ '</td>'
							     +'<td style="width:14%;">' +ent.failRate+ '</td>'
				    			 +'</td>'
				    			 +'</tr>'
				    			 +'<tr class="listTr">'
				    			 +'<td style="width:15%;">服务号消息</td>' 
				    			 +'<td style="width:14%;">' +mp.totalNum+ '</td>' 
				    			 +'<td style="width:14%;">' +mp.successNum+ '</td>'
				    			 +'<td style="width:14%;">' +mp.failNum+ '</td>'
				    			 +'<td style="width:14%;">' +mp.armeNum+ '</td>'
				    			 +'<td style="width:14%;">' +mp.successRate+ '</td>'
				    			 +'<td style="width:14%;">' +mp.failRate+ '</td>'
				    			 +'</td>'
				    			 +'</tr>'            
				    			 +'<tr class="listTr">'
				    			 +'<td style="width:15%;">总计</td>' 
				    			 +'<td style="width:14%;">' +totalNum+ '</td>' 
				    			 +'<td style="width:14%;">' +successNum+ '</td>'
				    			 +'<td style="width:14%;">' +failNum+ '</td>'
				    			 +'<td style="width:14%;">' +armeNum+ '</td>'
				    			 +'<td style="width:14%;">' +(totalNum == 0 ? "0%" : toPercent(successNum/totalNum))+ '</td>'
				    			 +'<td style="width:14%;">' +(totalNum == 0 ? "0%" :toPercent(failNum/totalNum))+ '</td>'
				    			 +'</td>'
				    			 +'</tr>';            
			    		strtemp += temp;	    	 
						
				} else {
				       strtemp += '<tr class="listTr"><td colspan="7">没有数据</td></tr>';
				        }
				strtemp +='</table>';
				$('#sumMsglist').html(strtemp);
			}
		}, "json");
	} catch (e) {
		// TODO: handle exception
	}
}

//百分数的转化
function toPercent(data){
    var strData = parseFloat(data)*10000;
    strData = Math.round(strData);
    strData/=100.00;
    var ret = strData.toString()+"%";
    return ret;
}

