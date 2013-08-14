package com.cidesign.jianghomestyle;

import java.io.IOException;

import com.androidquery.AQuery;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.http.ArticalOperation;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.version.VersionUpdate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * @Title: SplashActivity.java
 * @Package com.cidesign.jianghomestyle
 * @Description: app start activity, complish initialize the database and new
 *               version check from server
 * @author liling
 * @date 2013年8月14日 上午10:05:30
 * @version V2.0
 */

public class SplashActivity extends Activity
{
	private DatabaseHelper dbHelper;

	private AQuery aq;
	private ArticalOperation aOper = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splash);
		dbHelper = new DatabaseHelper(this);
		aq = new AQuery(this);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		aOper = new ArticalOperation(this, aq, dbHelper);
		
		
		if (StorageUtils.isSDCardPresent())
		{
			try
			{
				StorageUtils.mkdir();

				aOper.getArticleInfo();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			//check the version
			VersionUpdate vUpdate = new VersionUpdate(this.getApplicationContext(), SplashActivity.this, aq);
			vUpdate.getServerVerCode();
		}
		else
		{
			Toast.makeText(this, "请插入SD卡，该应用需要SD卡保存数据！", Toast.LENGTH_LONG).show();
		}
	}
}
