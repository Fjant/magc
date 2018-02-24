jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var curPath=window.document.location.href;  
var path = localStorage.getItem("globalUrl");
var search_id = null;
$(function(){
	search_id = getUrlParam("search_id");
	//初始化页面
	if(search_id != null){		
		//初始化更新数据
		ajaxtempleById(search_id);
	}
});

//根据ID，获取要修改的信息
function ajaxtempleById(search_id){
	try {
		$.post(path+"/job/jobinfo!getById.action", {search_id:search_id},
				function(date){
					if(date){
							var returnData = date["returnData"];				
							$("#search_id").val(search_id); 
							$("#name").val(returnData.name); 
							$("#group").val(returnData.group); 
							$("#classPath").val(returnData.classPath);
							$("#method").val(returnData.method);
							$("#argument").val(returnData.argument);
							$("#cronExpression").val(returnData.cronExpression);
							$("#incTypeId").val(returnData.incTypeId);
							$("#isConcurrent").children("option[value='"+returnData.isConcurrent+"']").attr("selected", true);
							$("#statusId").children("option[value='"+returnData.statusId+"']").attr("selected", true);
							$("#remark").val(returnData.remark);					
						}
					},"json");
	} catch (e) {
		// TODO: handle exception
	}
}

function subform(){
	if(checkSaveprizeInfo()){	
	var name = $("#name").val();
	var group = $("#group").val();
	var classPath = $("#classPath").val();
	var method = $("#method").val();
	var argument = $("#argument").val();
	var cronExpression = $("#cronExpression").val();
	var isConcurrent = $("#isConcurrent").val();
	var incTypeId = $("#incTypeId").val();
	var statusId = $("#statusId").val();
	var remark = $("#remark").val();
	var mode = "save";
	if($.trim(search_id) != ""){
		mode = "update";
	}
	try {
		$.post(path+"/job/jobinfo!"+mode+".action",
				{search_name:name,
				 search_group:group,
				 search_classPath:classPath,
				 search_method:method,
				 search_argument:argument,
				 search_cronExpression:cronExpression,
				 search_isConcurrent:isConcurrent,
				 search_incTypeId:incTypeId,
				 search_statusId:statusId,
				 search_remark:remark,
				 search_id:search_id
				},
				function(data){
					  if(data){
						if(data.retCode == "1"){
							if(mode == "save"){
								alert("新增成功");
							}else{
								alert("编辑成功");
							}
							window.location.href='joblist.jsp';	
						}else if (data.retCode == "-1") {
							alert("保存失败，" + data.message + ",请修改后重新保存。");
						}else if(data.retCode == "2"){
							alert("保存失败，" + data.message + ",请修改后重新保存。");
						}else {
							alert("保存失败，错误信息是：" + data.message + ",请重新操作");
						}
					   }
					},"json");
	} catch (e) {
		// TODO: handle exception
	}
	
}}

//检查要保存的数据
function checkSaveprizeInfo(){
	//获取需要保存的数据
	var name = $("#name").val();
	var group = $("#group").val();
	var classPath = $("#classPath").val();
	var method = $("#method").val();
//	var argument = $("#argument").val();
	var cronExpression = $("#cronExpression").val();
	var incTypeId = $("#incTypeId").val();
	var remark = $("#remark").val();
	//开始检查
	if(name == ""){
		alert("任务名称不能为空，请输入！");
		return false;
	}
	if(group == ""){
		alert("任务组不能为空，请输入！");
		return false;
	}
	if(classPath == ""){
		alert("接口类不能为空，请输入！");
		return false;
	}
	if(method == ""){
		alert("接口函数不能为空，请输入！");
		return false;
	}
	if(cronExpression == ""){
		alert("运行时间不能为空，请输入！");
		return false;
	}
	
	if(method == ""){
		alert("接口函数不能为空，请输入！");
		return false;
	}
	
	if(incTypeId == ""){
		alert("增量类型不能为空，请输入！");
		return false;
	}else{
		var reg = /^\+?[1-9][0-9]*$/;
		if(! reg.test(incTypeId)){
			alert("增量类型只能为正整数，请重新输入！");
			return false;
		}
	}
	
	if(remark != "" && remark.length>100){
		alert("字数不能超过100字！");
		return false;
	}
	
	return true;
}


