jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var pageNo = 1;// 分页
var pagesize = 10;// 显示数
var path = localStorage.getItem("globalUrl");
var postPath = path + "/job/jobinfo!list.action";
$(function() {
	// 调用初始化数据
	ajaxresult();
	// 加载初始化数据
	function ajaxresult() {
		$('#joblist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
		   ajaxPost(postPath, {}, true);
	  }
   }

);

// 搜索
function ajaxSearch() {
	var name = $("#name").val();
	var statusId = $("#statusId").val();
	var paramArray = {
		search_name : name,
		search_statusId : statusId
		
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
				    			  +'<td style="width:20%;">' + (result[i].name == null ? "" : result[i].name) + '</td>' 
							      +'<td style="width:20%;">' + (result[i].group == null ? "" : result[i].group) + '</td>'							      
							      +'<td style="width:12%;">' + (result[i].cronExpression == null ? "" : result[i].cronExpression) + '</td>'
							      +'<td style="width:13%;">' + (result[i].argument == null ? "" : result[i].argument) + '</td>'
							      +'<td style="width:7%;">' + (result[i].isConcurrent == null ? "" : (result[i].isConcurrent == "0"?"否":"是")) + '</td>'
							      +'<td id="statusId'+result[i].id+'" style="width:8%;">' + (result[i].statusId == null ? "" : (result[i].statusId ==0 ?"停止":"运行")) + '</td>'
				    			  +'<td style="width:15%;">'
				     			  +	'<a onclick="startJob('+result[i].id+')">运行</a><span>|</span>'
				    			  +	'<a onclick="stopJob('+result[i].id+')">停止</a><span>|</span>'
				    			  +	'<a onclick="updateOrAdd('+result[i].id+')">编辑</a>'
				    			  +	'<span>|</span><a onclick="deleteJob('+result[i].id+')">删除</a>'
				    			  +'</td>'
				    			  +'</tr>';			    		
			    		  strtemp += temp;	    	 
						}
				} else {
				       strtemp += '<tr class="listTr"><td colspan="17">没有数据</td></tr>';
				        }
				strtemp +='</table>';
				$('#joblist').html(strtemp);
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
			var statusId = $("#statusId").val();
			var paramArray = {
				search_name : name,
				search_statusId : statusId,
				search_pageNo : pageNo,
				search_pageSize : pagesize
			};
			ajaxPost(postPath, paramArray, false);
			return false;
		}
	}, isRefresh);
}

//删除
function deleteJob(id){
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
		postPath = path+"/job/jobinfo!delete.action";
		var param = {search_ids:paramArray};
		$.post(postPath, param,
				function(date){
					if(date.retCode == 1){
						alert("删除成功");
						postPath = path+"/job/jobinfo!list.action";
						ajaxPost(postPath,{},true);
					}
				},"json");
		}
	} catch (e) {
		// TODO: handle exception
	}
}

//运行任务功能
function startJob(i){
	try {
		var statusId = $("#statusId"+i).text();
		if(statusId=="停止"){
			var returnValue = confirm("您确认要运行该功能吗？");
			if(returnValue){
				$.post(path+"/job/jobinfo!startJob.action",{search_id:i},
						function(date){
							  if(date){
								if(date.retCode == 1){
									alert("运行成功！");
									window.location.href='joblist.jsp';
								}else{
									alert("编辑模块不成功，错误信息是："+date.message+",请重新操作");
								}
							   }
							},"json");			
			}
		}else{
			alert("该功能已经在运行，不能执行该操作！");
		}
	} catch (e) {
		// TODO: handle exception
	}
}

//停止任务功能
function stopJob(i){
	try {
		var statusId = $("#statusId"+i).text();
		if(statusId=="运行"){
			var returnValue = confirm("您确认要停止该功能吗？");
			if(returnValue){
				$.post(path+"/job/jobinfo!stopJob.action",{search_id:i},
						function(date){
							  if(date){
								if(date.retCode == 1){
									alert("停止成功！");
									window.location.href='joblist.jsp';
								}else{
									alert("编辑模块不成功，错误信息是："+date.message+",请重新操作");
								}
							   }
							},"json");			
			}
		}else{
			alert("该功能已经停止，不能执行该操作！");
		}
	} catch (e) {
		// TODO: handle exception
	}
}
//跳转到新增界面
function updateOrAdd(i){
	if(i==0){		
		window.location.href='jobinfo.jsp';
	}else{
		window.location.href='jobinfo.jsp?search_id='+i;
	}
}




