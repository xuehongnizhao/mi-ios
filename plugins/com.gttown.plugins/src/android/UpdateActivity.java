package com.gttown.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xt.rov.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

public class UpdateActivity {
	public Activity activity = null;
	public Notification nf = null;
	public NotificationManager nfMag = null;
	public RemoteViews remoteViews = null;
	String tickerText = "download is begin";
	int notificationId = 0;
	File downloadFile = null;//I put apk in download file.
	
	public UpdateActivity(Activity activity){
		this.activity = activity;
		nf = new Notification();
		nfMag = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		remoteViews = new RemoteViews(activity.getPackageName(), R.layout.update_app);
		nf.contentView = remoteViews;
		try {
			Global.currVersion = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
			Global.apkName = (String)activity.getApplicationInfo().loadLabel(activity.getPackageManager()) + ".apk";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		getFile();
	}
	
	/*
	 * compare the app current version with version from server.
	 * return: if true do update.
	 */
	public boolean checkVersion(){
		if(Global.serverVersion > Global.currVersion) return false;
		else return true;
	}
	
	/*
	 * download the apk from server.
	 */
	public void download() throws IOException{
		if(checkVersion()) return;
		
		InputStream in = null;
		FileOutputStream out = null;
		HttpURLConnection conn = null;
		int timeout = 5000;
		int apkSize = 0;
		int downloadSize = 0;
		
		try{
			URL url = new URL(Global.downloadUrl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestProperty("User-Agent", "PacificHttpClient");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			in = conn.getInputStream();
			out = new FileOutputStream(downloadFile, false);
			apkSize = conn.getContentLength();
			
			byte[] buffer = new byte[1024 * 100];
			int readSize = 0;
			addNotification(Global.BEGIN, 0);
			while((readSize = in.read(buffer)) > 0){
				out.write(buffer, 0, readSize);
				downloadSize += readSize;
				addNotification(Global.LOADING, (int)(downloadSize*100/apkSize));
			}
			addNotification(Global.COMPLETE, 0);
		}finally{
			if(conn != null) conn.disconnect();
			if(in != null) in.close();
			if(out != null) out.close();
		}
	}
	
	/*
	 * add the notification.
	 */
	public void addNotification(int status, int percent){
		switch(status){
			case Global.BEGIN:
				nf.icon = R.drawable.icon;
				nf.tickerText = tickerText;
				nfMag.notify(notificationId, nf);
				break;
			case Global.LOADING:
				remoteViews.setProgressBar(R.id.notificationProgress, 100, percent, false);
				nfMag.notify(notificationId, nf);
				break;
			case Global.COMPLETE:
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(downloadFile), "application/vnd.android.package-archive");
				activity.startActivity(intent);
				nfMag.cancel(notificationId);
				break;
		}
	}
	
	/*
	 * the file is used for save apk. I put it in download file.
	 */
	public void getFile(){
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		if(!dir.exists()) dir.mkdir();
		downloadFile = new File(dir, Global.apkName);
	}
	
}
