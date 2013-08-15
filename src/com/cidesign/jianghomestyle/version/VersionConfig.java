package com.cidesign.jianghomestyle.version;

import com.cidesign.jianghomestyle.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * [{"appname":"jtapp12","apkname":"jtapp-12-updateapksamples.apk","verName":1.0
 * .1,"verCode":2}]
 * 
 * @author liling
 * 
 */
public class VersionConfig
{
	private static final String TAG = VersionConfig.class.getName();

	public static final String UPDATE_SERVER = "http://lotusprize.com/xinjiang/app/update/";
	public static final String UPDATE_APKNAME = "JiangHomeStyle.apk";
	public static final String UPDATE_VERJSON = "ver.json";
	public static final String UPDATE_SAVENAME = "JiangHomeStyle.apk";

	public static int getVerCode(Context context)
	{
		int verCode = -1;
		try
		{
			verCode = context.getPackageManager().getPackageInfo("com.cidesign.jianghomestyle", 0).versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return verCode;
	}

	public static String getVerName(Context context)
	{
		String verName = "";
		try
		{
			verName = context.getPackageManager().getPackageInfo("com.cidesign.jianghomestyle", 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return verName;
	}

	public static String getAppName(Context context)
	{
		String verName = context.getResources().getText(R.string.app_name).toString();
		return verName;
	}
}
