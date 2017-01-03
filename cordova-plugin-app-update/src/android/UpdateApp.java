package com.gttown.plugins;

import java.io.IOException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class UpdateApp extends CordovaPlugin{
	public static final String ACTION_DOWNLOAD_APK = "downloadApk";
	UpdateActivity updateActivity = null;
	CallbackContext callbackContext = null;
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callback) throws JSONException{
		if(ACTION_DOWNLOAD_APK.equals(action)){
			callbackContext = callback;
			Global.downloadUrl = args.getString(0);
			Global.serverVersion = args.getInt(1);
			updateActivity = new UpdateActivity(cordova.getActivity());
			new Thread(runnable).start();
		}
		return false;
	}
	
	Runnable runnable = new Runnable(){
		@Override
		public void run() {
			try{
				updateActivity.download();
				callbackContext.success();
			}catch(IOException e){
				callbackContext.error("download error");
				e.printStackTrace();
			}
		}
	};
}
