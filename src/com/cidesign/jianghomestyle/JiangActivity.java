package com.cidesign.jianghomestyle;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
* @Title: JiangActivity.java 
* @Package com.cidesign.jianghomestyle 
* @Description: super class of all activity, get the screen size 
* @author liling  
* @date 2013年8月14日 上午10:01:40 
* @version V2.0
 */

public class JiangActivity extends Activity
{
	protected int screenWidth;
	protected int screenHeight;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//get the screen width and height, and the screen orientation is landscape
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}
}
