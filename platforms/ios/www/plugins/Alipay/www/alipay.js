cordova.define("Alipay.Alipay", function(require, exports, module) { var exec = require('cordova/exec');

function ALIPAY(){
	
};

ALIPAY.prototype.pay = function(arg,success,fail) {
        exec(success, fail, "Alipay", "mipay", [arg]);
};

var Alipay = new ALIPAY();

module.exports = Alipay;

});
