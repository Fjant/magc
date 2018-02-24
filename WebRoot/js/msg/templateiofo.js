jsDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var curPath=window.document.location.href;  
var path = localStorage.getItem("globalUrl");
$(function(){
	var search_id = getUrlParam("search_id");
	//初始化页面
	if(search_id != null){
		//初始化更新数据
		ajaxtemplateById(search_id);
	}
});

//根据ID，获取要修改的模块信息
function ajaxtemplateById(search_id){
	try {
		$.post(path+"/msg/template!getTemplateById.action", {search_id:search_id},
				function(date){
					if(date){
							var returnData = date["returnData"];
							$("#search_id").val(search_id);
							$("#name").val(returnData.name);
							$("#content").val(returnData.content);
						}
					},"json");
	} catch (e) {
		// TODO: handle exception
	}
}


function subform(){
	if(checkSaveprizeInfo()){	
	var search_id = $("#search_id").val();
	var name = $("#name").val();
	var content = $("#content").val();
	var mode = "save";
	if($.trim(search_id) != ""){
		mode = "update";
	}
	try {
		$.post(path+"/msg/template!"+mode+".action",
				{search_id:search_id,
				 search_name:name,
				 search_content:content
				},
				function(date){
					  if(date){
						if(date.retCode == 1){
							if(mode == "save"){
								alert("新增成功");
							}else{
								alert("编辑成功");
							}
							window.location.href='templatelist.jsp';
						}else{
							alert("编辑模块不成功，错误信息是："+date.message+",请重新操作");
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
	var content = $("#content").val();
	//开始检查
	if(name == ""){
		alert("模板名称不能为空，请输入！");
		return false;
	}
	if(content == ""){
		alert("模板内容不能为空，请输入！");
		return false;
	}else{
		if(content.length>500){
			alert("字数不能超过500字！");
			return false;
		}
	}
	return true;
}


