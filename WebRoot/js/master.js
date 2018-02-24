$(function(){
	initSize();
	initLeftNav();
	window.onresize = function () {
	    initSize();
	}; 
});

SCH = 0;//屏幕高度
SCW = 0;//屏幕宽度
function initSize(){
	try{
		 getSCSize();
		 $(".masterTop").css({
			 width:SCW
		 });
		 
		 $(".masterTopInfo").css({
			 width:SCW
		 });
		 
		 $("#leftNavContent").css({
			 height:(SCH - $("#masterTop").height() - $("#masterTopInfo").height() - $("#masterBottom").height() - 10 - 16 - 42)
		 });
		 
		 $("#rightContent").css({
			 width:(SCW - $(".leftNav").width() - 10 - 12 - 20)
		 });

		 $(".rightContentBody").css({
			 height:$("#leftNavContent").height()
		 });
		 
		 $(".leftNavBottom").css({
			 bottom:0
		 });
		 
	}catch(e){}
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

function initLeftNav(){
	try{
		$(".leftNavSubitem h3").click(function() {
			if($(this).hasClass("open"))
			{
				$(this).removeClass("open");
			}else
		    {
				$(this).addClass("open");
			}
			$("#"+$(this).attr("tid")).toggle(300);
		});
		
		$(".leftNavSubitem ul li a").click(function(){
			if(!$(this).hasClass("cur")){
				$(".leftNavSubitem ul li a").removeClass("cur");
				$(this).addClass("cur");
				var currentPostion = "当前位置：后台管理系统  > ";
				currentPostion += $(this).parent().parent().parent().children("h3").children("span").html() + " > ";
				currentPostion += $(this).html();
				$(".rightContentTitle h2 span").html(currentPostion);
			}
		});
		
		$(".leftNavSubitem h3:eq(0)").click();
		$(".leftNavSubitem ul li a:eq(0)").click();
		$(".rightContentTitle h2 span").html("当前位置：后台管理系统  > " + $(".leftNavSubitem h3:eq(0) span").html() + " > " + $(".leftNavSubitem ul li a:eq(0)").html());

	}catch(e){}
}

function showFrame(frame){
	$("#rightContentBodyIframe").attr("src",frame);
}