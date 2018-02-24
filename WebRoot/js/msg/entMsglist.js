jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var pageNo = 1;// 分页
var pagesize = 10;// 显示数
var path = localStorage.getItem("globalUrl");
var postPath = path + "/msg/entMsg!list.action";
$(function() {
	// 调用初始化数据
	ajaxresult();
	// 加载初始化数据
	function ajaxresult() {
		$('#entMsglist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
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
	var userId = $("#userId").val();
	var userName = $("#userName").val();
	var sendType = $("#sendType").val();
	var statusId = $("#statusId").val();
	var sendStartTime = $("#sendStartTime").val();
	var sendEndTime = $("#sendEndTime").val();
	var paramArray = {
		search_userId : userId,
		search_userName : userName,
		search_sendType : sendType,
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
				var strtemp = '<table class="contentTable">';
				if(result != null && result.length > 0) {
					for (var i = 0; i < result.length; i++) {
			    		 var temp = '<tr class="listTr">'		    
				    			 +'<td style="width:5%;"><input type="checkbox" name="check" value="'+result[i].id+'"/></td>'			          			 
				    			 +'<td id="userId'+result[i].id+'" style="width:10%;">' + (result[i].userId == null ? "" : result[i].userId) + '</td>' 
							     +'<td style="width:8%;">' + (result[i].userName == null ? "" : result[i].userName) + '</td>' 
							     +'<td style="width:10%;">' + (result[i].templateId == null ? "" : result[i].templateId) + '</td>'
			    		         +'<td style="width:10%;">' + (result[i].typeId == null ? "":( result[i].typeId == 1 ? "续期缴费":"生日提醒")) + '</td>'
							     +'<td id="statusId'+result[i].id+'" style="width:7%;">' + (result[i].statusId == null ? "" : (result[i].statusId == 1 ?"已发送":"未发送")) + '</td>'
							     +'<td style="width:10%;">' + (result[i].sendResult == null ? "" : result[i].sendResult) + '</td>'
							     +'<td style="width:10%;">' + (result[i].sendType == null ? "" : (result[i].sendType == 1 ? "直接发送":"队列发送")) + '</td>'
							     +'<td style="width:10%;">' + (result[i].resendTimes == null ? "" : result[i].resendTimes) + '</td>'
							     +'<td style="width:10%;">' + (result[i].sendTime == null ? "" : result[i].sendTime) + '</td>'
				    			 +'<td style="width:10%;">'
				    			 +	'<a onclick="showDetails('+i+')" id="details'+i+'">详情</a>'
				    			 +	'<span>|</span><a onclick="deleteMsg('+result[i].id+')">删除</a>'
				    			 +'</td>'
				    			 +'</tr>'
				    			 +'<tr id="detailsTr'+i+'" class="detailsTr" style="display: none;">'
				    			 +	'<td colspan="11">'
				    			 +		'<div class="detailsTrCenter">'
				    			 +			'<p>消息内容：'+(result[i].content == null ? "" : result[i].content)+'</p>'
				    			 +		'</div>'
				    			 +	'</td>'
				    			 +'</tr>';			    		
			    		strtemp += temp;	    	 
						}
				} else {
				       strtemp += '<tr class="listTr"><td colspan="17">没有数据</td></tr>';
				        }
				strtemp +='</table>';
				$('#entMsglist').html(strtemp);
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
			var userId = $("#userId").val();
			var userName = $("#userName").val();
			var sendType = $("#sendType").val();
			var statusId = $("#statusId").val();
			var sendStartTime = $("#sendStartTime").val();
			var sendEndTime = $("#sendEndTime").val();
			var paramArray = {
				search_userId : userId,
				search_userName : userName,
				search_sendType : sendType,
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

//删除
function deleteMsg(id){
	try {
		var returnValue = confirm("删除是不可恢复的，您确认要删除吗？");
		if(returnValue){
			var userId = $("#userId"+id).text();
			var statusId = $("#statusId"+id).text();
			if(statusId== "已发送"){
				alert("您要删除的消息【工号："+userId+"】已经发送，不能够删除!");
			}else{
				var userIds1 = '';
				var userIds2 = '';
				var a = 0;
				var b = 0;
				var paramArray = new Array();
				if(id != -1){
					paramArray.push(id);
				}else{
					var $check_boxes = $('input[type=checkbox][name=check]:checked');  
					if($check_boxes.length<=0){ alert('您未勾选，请勾选！');return;   }  
					$check_boxes.each(function(){
						var i = $(this).val();
						var userId = $("#userId"+i).text();
						var statusId = $("#statusId"+i).text();
						if(statusId== "已发送"){
							userIds1 += userId+'|';
							a++;
						}else{
							userIds2 += userId+'|';
							paramArray.push(i);
							b++;
						}
					});
				}
				if((id=="undefined"||id==null||id<=0)&&b==0){
					alert("您要删除的消息【工号："+userIds1+"】已经发送，不能够删除!");
				}else{		
					postPath = path+"/msg/entMsg!delete.action";
					var param = {search_ids:paramArray};
					$.post(postPath, param,
							function(date){
						if(date.retCode == 1){
							if(a > 0){
								alert("您要删除的消息【工号："+userIds2+"】删除成功,【工号："+userIds1+"】已经使用,不能够删除!");
							}else{					
								alert("删除成功");
							}
							postPath = path+"/msg/entMsg!list.action";
							ajaxPost(postPath,{},true);
						}else{
							alert("删除失败");
						}
					},"json");
				}
			}
		}
	} catch (e) {
		// TODO: handle exception
	}
}

function exportRrport() {
	var userId = $("#userId").val();
	var userName = $("#userName").val();
	var sendType = $("#sendType").val();
	var statusId = $("#statusId").val();
	var sendStartTime = $("#sendStartTime").val();
	var sendEndTime = $("#sendEndTime").val();
	var html = $('#agentDailyReportlist').html();
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
	$("#agentDailyReportlist").hide();
	try {
		$.doPostAsync(path + "/msg/entMsg!exportEntMsg.action", paramArray, 
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
	$("#agentDailyReportlist").show();
}

