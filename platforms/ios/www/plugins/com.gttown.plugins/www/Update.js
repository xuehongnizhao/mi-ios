cordova.define("com.gttown.plugins.Update", function(require, exports, module) { var exec = require('cordova/exec');

updateFromServer = function(url, serverVersion, successCallback, errorCallback){
  exec(successCallback, errorCallback, "UpdateApp", "downloadApk", [url, serverVersion]);
}

module.exports = updateFromServer;

});
