//1.在当前View中打开新的HTML
//openWithView("","shangpin.html","{\"gid:\"23010\",\"attrib\":\"ZS\"}")
//2.新的View中打开新的HTML
//openWithView("commView","index.html","\"gid:\"23010\",\"attrib\":\"ZS\"")
//3.打开指定View，使用App.xml中配置的默认页或当前页
//openWithView("commView","")




// arg ("key":"val","k2":"v2")
function setPersistent(arg) {
	
	var req = "{\"request\":{\"requestid\":\"3\",\"callback\":\"\"},\"parameter\":{"+arg+"}}}";
	window.jsInvoke.invokeLocalStorePersistent(req);
	
}

// arg ("key":"val","k2":"v2")
function setTemporary(){
	var req = "{\"request\":{\"requestid\":\"4\",\"callback\":\"\"},\"parameter\":{\"arg\":{"+arg+"}}}";
	window.jsInvoke.invokeLocalStoreTemporary(req);
}

//
function getPersistent(callback,eventID) {
	var size = arguments.length;
	var req = "{\"request\":{\"requestid\":\""+eventID+"\",\"callback\":\""+callback+"\"},\"parameter\":{\"channel\":\"Pers\",";
	for(var i=2;i<size;i++){
		req += "\""+arguments[i]+"\":\"\",";
	}
	req = req.substr(0, req.length -1);
	
	req+="}}";
	
	window.jsInvoke.invokeGetLocalStore(req);
	
}
function getTemporary(callback,eventID) {

	var size = arguments.length;
	var req = "{\"request\":{\"requestid\":\""+eventID+"\",\"callback\":\""+callback+"\"},\"parameter\":{\"channel\":\"Temp\",";
	for(var i=2;i<size;i++){
		req += "\""+arguments[i]+"\":\"\",";
	}
	req = req.substr(0, req.length -1);
	
	req+="}}";
	
	window.jsInvoke.invokeGetLocalStore(req);
}

// 注册触控事件
function regEvent(eventName,callback){
	var req  ="{\"request\":{\"requestid\":\"5\",\"callback\":\""+callback+"\"},\"parameter\":{\""+eventName+"\":\""+callback+"\"}}";
	window.jsInvoke.invokeRegistLocalEvent(req);
}

// 取消注册事件
function unRegEvent(eventName) {
	var req  ="{\"request\":{\"requestid\":\"6\",\"callback\":\"\"},\"parameter\":{\""+eventName+"\":\"\"}}";
	window.jsInvoke.invokeUnRegistLocalEvent(req);
	 
}



// 扫条码
function barcode(callback) {
	var req = "{\"request\":{\"requestid\":\"7\",\"callback\":\""+callback+"\"},\"parameter\":{}}";
	window.jsInvoke.invokeBarcode(req);
	
}

// 支付
function payment(){
	
}
//联系人
function contacts(callback){
	window.jsInvoke.invokeGetContact("{\"request\":{\"requestid\":\"10\",\"callback\":\""+callback+"\"},\"parameter\":{}}");
	
}
//返回上一页
function goBackView() {
	
	var req = "{\"request\":{\"requestid\":\"2\",\"callback\":\"\"},\"parameter\":{}}";
	window.jsInvoke.invokeBackViewPage(req);
	
}

function sendNotification(){
	var req = "{\"request\":{\"requestid\":\"2\",\"callback\":\"\"},\"parameter\":{}}";
	window.jsInvoke.invokeOrderSuccessNotification(req);
}
//arg("name":"","intro":"","price":"","orderid":"")
function pay(callback,arg){
	var req = "{\"request\":{\"requestid\":\"2\",\"callback\":\""+callback+"\"},\"parameter\":{"+arg+"}}";
	window.jsInvoke.invokePay(req);
}

