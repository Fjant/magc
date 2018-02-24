jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var pageNo = 1;// 分页
var pagesize = 10;// 显示数
var path = localStorage.getItem("globalUrl");
var postPath = path + "/msg/template!list.action";
$(function() {
	// 调用初始化数据
	ajaxresult();
	// 加载初始化数据
	function ajaxresult() {
		$('#templatelist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
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
	var name = $("#name").val();
	var createStartTime = $("#createStartTime").val();
	var createEndTime = $("#createEndTime").val();
	var paramArray = {
		search_name : name,
		search_createStartTime : createStartTime,
		search_createEndTime : createEndTime
		
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
				    			  +'<td style="width:10%;">' + (result[i].name == null ? "" : result[i].name) + '</td>' 
							      +'<td style="width:10%;">' + (result[i].createBy == null ? "" : result[i].createBy) + '</td>' 
							      +'<td style="width:10%;">' + (result[i].createTime == null ? "" : result[i].createTime) + '</td>'
				    			  +'<td style="width:15%;">'
				     			  +	'<a onclick="updateOrAdd('+result[i].id+')">编辑</a><span>|</span>'
				    			  +	'<a onclick="showDetails('+i+')" id="details'+i+'">详情</a>'
				    			  +	'<span>|</span><a onclick="deleteMsg('+result[i].id+')">删除</a>'
				    			  +'</td>'
				    			  +'</tr>'
				    			  +'<tr id="detailsTr'+i+'" class="detailsTr" style="display: none;">'
				    			  +	'<td colspan="9">'
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
				$('#templatelist').html(strtemp);
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
			var name = $("#name").val();
			var createStartTime = $("#createStartTime").val();
			var createEndTime = $("#createEndTime").val();
			var paramArray = {
				search_name : name,
				search_createStartTime : createStartTime,
				search_createEndTime : createEndTime,
				search_pageNo : pageNo,
				search_pageSize : pagesize
			};
			ajaxPost(postPath, paramArray, false);
			return false;
		}
	}, isRefresh);
}

//跳转到新增编辑页面
function updateOrAdd(i){
	if(i==0){		
		window.location.href='templateinfo.jsp';
	}else{		
		window.location.href='templateinfo.jsp?search_id='+i;
	}
}
//删除
function deleteMsg(id){
	try {
		var returnValue = confirm("删除是不可恢复的，您确认要删除吗？");
		if(returnValue){		
			var paramArray = new Array();
			if(id != -1){
				paramArray.push(id);
			}else{
				var $check_boxes = $('input[type=checkbox][name=check]:checked');  
				if($check_boxes.length<=0){ alert('您未勾选，请勾选！');return;   }  
				$check_boxes.each(function(){  
					paramArray.push($(this).val());  
				});
		}
		postPath = path+"/msg/template!delete.action";
		var param = {search_ids:paramArray};
		$.post(postPath, param,
				function(date){
					if(date.retCode == 1){
						alert("删除成功");
						postPath = path+"/msg/template!list.action";
						ajaxPost(postPath,{},true);
					}
				},"json");
		}
	} catch (e) {
		// TODO: handle exception
	}
}

function exportRrport() {
	var name = $("#name").val();
	var createStartTime = $("#createStartTime").val();
	var createEndTime = $("#createEndTime").val();
	var paramArray = {
		search_name : name,
		search_createStartTime : createStartTime,
		search_createEndTime : createEndTime	
	};
	$("#z_exportMember").show();
	$("#drtsy").show();
	$("#drtsy1").hide();
	$("#agentDailyReportlist").hide();
	try {
		$.doPostAsync(path + "/msg/template!exportTemplate.action", paramArray, 
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
