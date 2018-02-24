isDebugMode = 1;// 1为jsdebug模式，页面会alert出异常信息
var pageNo = 1; //分页
var pagesize = 10;//显示数
var path = localStorage.getItem("globalUrl");
var postPath = path + "/job/joblog!list.action";
$(function(){
	// 调用初始化数据
	ajaxresult();
	// 加载初始化数据
	function ajaxresult() {
	//	$('#jobLoglist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
		   ajaxPost(postPath, {}, true);
	  }
});

//搜索
function ajaxSearch() {
	var jobName = $("#jobName").val();
	var result = $("#result").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var paramArray = {
		search_jobName : jobName,
		search_result : result,
		search_startTime : startTime,
		search_endTime : endTime	
	};
	ajaxPost(postPath, paramArray, true);
}

function ajaxPost(postPath, paramArray, isRefresh){
	$('#jobLoglist').html("<p style='padding:5px;font-size:13px;'>正在加载中...</p>");
	try {		
		$.post(postPath, paramArray, function(data){
			if(data){
				var result = data["result"];
				var strtemp = '<table class="contentTable">';
				if(result != null && result.length > 0){
					for(var i=0;i<result.length;i++){					
						var temp = '<tr class="listTr">'	
							+'<td style="width:20%;">' + (result[i].jobName == null ? "" : result[i].jobName) + '</td>' 
							+'<td style="width:10%;">' + (result[i].totalNumber == null ? "" : result[i].totalNumber) + '</td>'							      
							+'<td style="width:10%;">' + (result[i].successNumber == null ? "" : result[i].successNumber) + '</td>'
							+'<td style="width:15%;">' + (result[i].startTime == null ? "" : result[i].startTime) + '</td>'
							+'<td style="width:15%;">' + (result[i].endTime == null ? "" : result[i].endTime) + '</td>'
							+'<td style="width:10%;">' + (result[i].result == null ? "" : (result[i].result == "0"?"失败":"成功")) + '</td>'
							+'</tr>';
						strtemp+=temp;
					}
				}else{
					 strtemp += '<tr class="listTr"><td colspan="8">没有数据</td></tr>';
				}
				strtemp +='</table>';
				$("#jobLoglist").html(strtemp);
				// 调用分页
				Paging(data,isRefresh);
			}
		},"json");
	} catch (e) {
		// TODO: handle exception
	}
}

function Paging(data, isRefresh) {
	// 生成分页
	// 有些参数是可选的，比如lang，若不传有默认值
	kkpager.generPageHtml({

		pno : data["pageNo"],
		// 总页码
		total : data["totalPages"],
		// 总数据条数
		totalRecords : data["totalCount"],
		mode : 'click',// 默认值是link，可选link或者click
		click : function(pageNo) {
			// do something
			// 手动选中按钮
			this.selectPage(pageNo);
			var jobName = $("#jobName").val();
			var result = $("#result").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var paramArray = {
				search_jobName : jobName,
				search_result : result,
				search_startTime : startTime,
				search_endTime : endTime,
				search_pageNo : pageNo,
				search_pageSize : pagesize
			};
			ajaxPost(postPath, paramArray, false);
			return false;
		}
	}, isRefresh);
}
