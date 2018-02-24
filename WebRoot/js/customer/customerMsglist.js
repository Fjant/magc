jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var pageNo = 1;// 分页
var newPageNo = pageNo;
var pagesize = 10;// 显示数
var path = localStorage.getItem("globalUrl");
var postPath = path + "/customer/customerMsg!list.action";
$(function() {
	// 调用初始化数据
	ajaxresult();
	// 加载初始化数据
	function ajaxresult() {
		$('#customerMsglist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
		   ajaxPost(postPath, {}, true);
	  }
   }

);

//显示（隐藏）模块详情
function showDetails(i){
	if($("#details"+i).html() == "详情"){
		$("#detailsTr"+i).show(); 
		$("#details"+i).html("收起"); 
	}else{
		$("#detailsTr"+i).hide(); 
		$("#details"+i).html("详情"); 
	}
}

// 搜索
function ajaxSearch() {
	var customerNo = $("#customerNo").val();
	var billNo = $("#billNo").val();
	var secuityNo = $("#secuityNo").val();
	var statusId = $("#statusId").val();
	var sendStartTime = $("#sendStartTime").val();
	var sendEndTime = $("#sendEndTime").val();
	if(sendStartTime != "" && sendEndTime != ""){
		if(sendStartTime>sendEndTime){
			alert("开始时间不能大于结束时间，请正确输入！");
			return;
		}
	}
	var paramArray = {
			search_customerNo : customerNo,
			search_billNo : billNo,
			search_secuityNo : secuityNo,
			search_statusId : statusId,
			search_sendStartTime : sendStartTime,
			search_sendEndTime : sendEndTime	
	};
	ajaxPost(postPath, paramArray, true);
}

function ajaxPost(postPath, paramArray, isRefresh) {
	try {
		$.post(postPath, paramArray, function(date) {
			if (date) {
				var result = date["result"];
				var totalCount = date["totalCount"];
				$("#totalCount").html(totalCount+'条');
				var strtemp = '<table class="contentTable">';
				if(result != null && result.length > 0) {
					for (var i = 0; i < result.length; i++) {
			    		 var temp = '<tr class="listTr">'		    		          			 
			    			 +'<td style="width:5%;">'+((newPageNo-1)*10+i+1)+'</td>' 
				    			 +'<td style="width:10%;">' + (result[i].customerNo == null ? "" : result[i].customerNo) + '</td>' 
							     +'<td style="width:10%;">' + (result[i].billNo == null ? "" : result[i].billNo) + '</td>'
							     +'<td style="width:15%;">' + (result[i].sendTime == null ? "" : result[i].sendTime) + '</td>'
			    		         +'<td style="width:20%;">保单状态通知（续期信息推送）</td>'
				    			 +'<td style="width:10%;">'
				    			 +	'<a onclick="showDetails('+i+')" id="details'+i+'">详情</a>'
				    			 +'</td>'
				    			 +'<td style="width:10%;">' + (result[i].statusId == null ? "" : (result[i].statusId == 1 ?"已发送":"未发送")) + '</td>'
				    			 +'<td style="width:10%;">' + (result[i].sendResult == null ? "" : result[i].sendResult) + '</td>'
				    			 +'<td style="width:10%;">' + (result[i].remark == null ? "" : result[i].remark) + '</td>'
				    			 +'</tr>'
				    			 +'<tr id="detailsTr'+i+'" class="detailsTr" style="display: none;">'
				    			 +	'<td colspan="11">'
				    			 +		'<div class="detailsTrCenter">'
				    			 +			'<p>微信推送内容：'+'<br/>保单状态通知<br/>'+(result[i].sendTime == null ? "" : result[i].sendTime.substring(5,7)+'月'+result[i].sendTime.substring(8,10)+'日<br/>')
				    			 +(result[i].content == null ? "" : result[i].content).replace('{"first":"','').replace('\\n\\n','<br/>').replace('","time":"','<br/>截止时间：')
				    			   .replace('","state":"', '<br/><br/>保单状态：').replace('","remark":"', '<br>').replace('"}', '').replace('\\n', '')+'</p>'
				    			 +		'</div>'
				    			 +	'</td>'
				    			 +'</tr>';			    		
			    		strtemp += temp;	    	 
						}
				} else {
				       strtemp += '<tr class="listTr"><td colspan="17">没有数据</td></tr>';
				        }
				strtemp +='</table>';
				$('#customerMsglist').html(strtemp);
				// 调用分页
				Paging(date,isRefresh);
			}
		}, "json");
	} catch (e) {
		// TODO: handle exception
	}
}

function Paging(date, isRefresh) {
	// 生成分页
	// 有些参数是可选的，比如lang，若不传有默认值
	kkpager.generPageHtml({

		pno : date["pageNo"],
		// 总页码
		total : date["totalPages"],
		// 总数据条数
		totalRecords : date["totalCount"],
		mode : 'click',// 默认值是link，可选link或者click
		click : function(pageNo) {
			// do something
			// 手动选中按钮
			this.selectPage(pageNo);
			var customerNo = $("#customerNo").val();
			var billNo = $("#billNo").val();
			var secuityNo = $("#secuityNo").val();
			var statusId = $("#statusId").val();
			var sendStartTime = $("#sendStartTime").val();
			var sendEndTime = $("#sendEndTime").val();
			    newPageNo = pageNo;
			var paramArray = {
					search_customerNo : customerNo,
					search_billNo : billNo,
					search_secuityNo : secuityNo,
					search_statusId : statusId,
					search_sendStartTime : sendStartTime,
					search_sendEndTime : sendEndTime,
				    search_pageNo : pageNo,
				    search_pageSize : pagesize
			};
			ajaxPost(postPath, paramArray, false);
			return false;
		}
	}, isRefresh);
}

function exportRrport() {
	var userId = $("#userId").val();
	var userName = $("#userName").val();
	var sendType = $("#sendType").val();
	var statusId = $("#statusId").val();
	var sendStartTime = $("#sendStartTime").val();
	var sendEndTime = $("#sendEndTime").val();
	var html = $('#customerMsglist').html();
	var paramArray = {
		search_userId : userId,
		search_userName : userName,
		search_sendType : sendType,
		search_statusId : statusId,
		search_sendStartTime : sendStartTime,
		search_sendEndTime : sendEndTime
		
	};
	$("#z_exportMember").show();
	$("#drtsy").show();
	$("#drtsy1").hide();
	$("#customerMsglist").hide();
	try {
		$.doPostAsync(path + "/customer/customerMsg!exportCustomerMsg.action", paramArray, 
		  function(data) {
			if (data && data != null && $.trim(data).length > 0) {
				var filePath = data;
				var fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length);

				var temp = '<a target="_blank" href="' + filePath + '">' + fileName + '</a>（右键另存为）';
				$("#drtsy1").html(temp);
				$("#drtsy").hide();
				$("#drtsy1").show();
			}
		});

	} catch (e) {
		// TODO: handle exception
	}

}

function closeExport() {
	$("#z_exportMember").hide();
	$("#customerMsglist").show();
}

