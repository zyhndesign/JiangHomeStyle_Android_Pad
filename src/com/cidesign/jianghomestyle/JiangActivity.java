package com.cidesign.jianghomestyle;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class JiangActivity extends Activity
{
	protected int screenWidth;
	protected int screenHeight;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}
}
