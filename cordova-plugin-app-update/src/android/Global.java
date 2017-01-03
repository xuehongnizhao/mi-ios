package com.gttown.plugins;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;

public class Global extends Application{
	public static int currVersion = 0;
	public static int serverVersion = 0; //the version's type is int.
	public static String downloadUrl = "";
	public static String apkName = "";
	public static final int BEGIN = 0;//"download is begin";
	public static final int LOADING = 1;//"download...";
	public static final int COMPLETE = 2;//"click to install";
}
