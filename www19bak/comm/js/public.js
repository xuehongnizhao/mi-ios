function url(req){
	return "https://www.zhizhoumi.com/mi/s/m?r="+req ;
}
function surl(req){
	return "https://www.zhizhoumi.com/mi/"+req;
}
function alurl(req){
	return "https://www.zhizhoumi.com/mi/s/m?r="+req ;
}
function mi_exit(ind){
	//goBackView();
	window.history.go(ind);
}

function gotoMessage(){
	window.location.href = "message.action"	
}

function getValue(id){
	var _value = $(id).val();
	return trim(_value);
}
 //去左空格;   
function ltrim(s){     
    return s.replace(/(^\s*)/, "");  
 }   
 //去右空格;   
function rtrim(s){   
  return s.replace(/(\s*$)/, "");  
}   
 //去左右空格;   
 function trim(s){  
  return rtrim(ltrim(s));   
 }
 function checktoken(data){
	 if(data.loge == 0){
		 return true;
	 }else if(data.loge == 1){
		 window.location.href="login.html";
		 return false;
	 }else if(data.loge == 2){	 
		 window.location.href="login.html";
		 return false;
	 }
	 
 }
 
