document
		.write('<link rel="stylesheet" href="comm/amazeui/css/amazeui.min.css" />');
document.write('<link rel="stylesheet" href="comm/amazeui/css/app.css">');
document
		.write('<link rel="stylesheet" href="css/mobiscroll.custom-2.5.0.min.css">');
document.write('<script src="comm/amazeui/js/jquery.min.js"></script>');
document.write('<script src="comm/amazeui/js/amazeui.min.js"></script>');
document.write('<script src="comm/js/ar_syscore.js"></script>');
document.write('<script src="comm/js/public.js"></script>');
document.write('<script src="comm/js/template.js"></script>');
document
		.write('<script src="comm/js/mobiscroll.custom-2.5.0.min.js"></script>');
document.write('<script type="text/javascript" src="js/Toast.js"></script>');
document.write('<script type="text/javascript" src="cordova.js"></script>');

function getParameter(param) {
	var query = window.location.search;// 获取URL地址中？后的所有字符
	var iLen = param.length;// 获取你的参数名称长度
	var iStart = query.indexOf(param);// 获取你该参数名称的其实索引
	if (iStart == -1)// -1为没有该参数
		return "";
	iStart += iLen + 1;
	var iEnd = query.indexOf("&", iStart);// 获取第二个参数的其实索引
	if (iEnd == -1)// 只有一个参数
		return query.substring(iStart);// 获取单个参数的参数值
	return query.substring(iStart, iEnd);// 获取第二个参数的值
}

function showtoast(msg){
	new Toast({context : $('body'),message : msg}).show();
}

function stopBubble(e) {
	var evt = e || window.event;
	evt.stopPropagation ? evt.stopPropagation() : (evt.cancelBubble = true);
}

function showCarCount(){
	var scc  = window.localStorage.getItem('shopcar-count');
	if(scc == null || typeof(scc) == 'undefined' || scc == "" ){
		scc = 0;
		window.localStorage.setItem('shopcar-count',0);
	}
	if(scc > 99){
		$('#shopcar-count').html("99+");
	}else{
		$('#shopcar-count').html(scc);
	}
}

// Handle the back button    //      
function onBackKeyDownMain() {      
	window.location.href = "main.html";
}



function initPush() {

}
