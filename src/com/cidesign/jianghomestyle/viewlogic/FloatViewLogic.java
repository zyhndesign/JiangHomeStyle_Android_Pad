package com.cidesign.jianghomestyle.viewlogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.tools.FileOperationTools;
import com.cidesign.jianghomestyle.tools.MD5Tools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;

/**
 * 
* @Title: FloatViewLogic.java 
* @Package com.cidesign.jianghomestyle.viewlogic 
* @Description: complish pop window logic 
* @author liling  
* @date 2013��8��14�� ����10:36:44 
* @version V2.0
 */
public class FloatViewLogic
{
	private static final String TAG = FloatViewLogic.class.getSimpleName();

	private Activity activity;

	private View view; // top view

	private View floatView;
	private PopupWindow pw = null;
	private WebView detail_webview = null;
	private View overLayer;

	private int screen_width;
	private int screen_height;

	private WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;

	private ProgressBar webLoadingProgressBar = null;

	public FloatViewLogic(Activity activity, View view, int screen_width, int screen_height)
	{
		this.activity = activity;
		this.view = view;
		this.screen_height = screen_height;
		this.screen_width = screen_width;
	}

	public WindowManager createView()
	{
		// ��ȡWindowManager
		wm = (WindowManager) activity.getApplicationContext().getSystemService("window");
		// ����LayoutParams(ȫ�ֱ�������ز���
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;// �������ṩ���û���������������Ӧ�ó����Ϸ���������״̬������
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

		wmParams.gravity = Gravity.TOP; // �����������������Ͻ�
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		wmParams.x = 0;
		wmParams.y = 0;
		// �����������ڳ�������
		wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = PixelFormat.RGBA_8888;
		// ��ʼ�����ֲ����б�

		wm.addView(view, wmParams);
		return wm;
	}

	/**
	 * ������������ĸ������
	 * 
	 * @param view
	 * @param templateName
	 */
	public void createFloatWin(final View v, String templateName)
	{
		ContentEntity cEntity = (ContentEntity) v.getTag();
		if (cEntity != null)
		{
			if (cEntity.getDownloadFlag() == 1) //�Ѿ�������
			{				
				popWinWithContentEntity(cEntity);
			}
			else
			{
				//����
				new AsyncDownTask(cEntity).execute();
			}
		}
	}

	@SuppressLint("JavascriptInterface")
	public void popWinWithContentEntity(ContentEntity cEntity)
	{
		String url = StorageUtils.FILE_ROOT + cEntity.getServerID() + "/" + cEntity.getMain_file_path();
		overLayer = activity.findViewById(R.id.overLayer);
		overLayer.getBackground().setAlpha(160);
		overLayer.setVisibility(View.VISIBLE);
		LayoutInflater inflater = LayoutInflater.from(activity);

		floatView = inflater.inflate(R.layout.template1, null);

		pw = new PopupWindow(floatView, screen_width, screen_height);

		pw.setAnimationStyle(R.style.mydialog);

		pw.setFocusable(true);

		pw.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.black));

		ImageView iv = (ImageView) floatView.findViewById(R.id.backToMainPanel);
		iv.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (pw != null)
				{
					if (detail_webview.canGoBack())
					{
						detail_webview.goBack();
					}
					else
					{
						if (pw.isShowing())
						{
							pw.dismiss();
						}
					}
				}
			}
		});

		pw.setOnDismissListener(new OnDismissListener()// ��ʧ����
		{
			public void onDismiss()
			{
				overLayer.setVisibility(View.INVISIBLE);
			}

		});
		detail_webview = (WebView) floatView.findViewById(R.id.detail_webview);

		detail_webview.setHorizontalScrollBarEnabled(true);
		WebSettings ws = detail_webview.getSettings();
		// ws.setBuiltInZoomControls(true); // ������ʾ���Ű�ť
		// ws.setSupportZoom(true); // ֧������
		// ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		// ws.setDefaultTextEncodingName("utf-8"); // �����ı�����

		floatView.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (pw != null)
				{
					pw.dismiss();
				}
			}
		});

		webLoadingProgressBar = (ProgressBar) floatView.findViewById(R.id.webLoadingProgressBar);
		WebSettings webSettings = detail_webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		detail_webview.addJavascriptInterface(this, "OpenMedia");
		detail_webview.setWebChromeClient(m_chromeClient);
		detail_webview.setWebViewClient(new WebViewClient()
		{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});
		detail_webview.loadUrl("file://" + url);
		pw.showAsDropDown(view, 0, -view.getHeight());
	}
	
	private WebChromeClient m_chromeClient = new WebChromeClient()
	{
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			// TODO Auto-generated method stub
		}

		public void onProgressChanged(WebView view, int progress)
		{
			// setTitle("ҳ������У����Ժ�..." + progress + "%");
			// setProgress(progress * 100);

			if (progress == 100)
			{
				if (webLoadingProgressBar != null)
				{
					webLoadingProgressBar.setVisibility(View.INVISIBLE);
				}
			}
		}
	};
	
	class AsyncDownTask extends AsyncTask<Void, Void, Integer>
	{
		private final int SUCCESS = 1;
		private final int FAILURE = 0;
		private ContentEntity cEntity;
		private RuntimeExceptionDao<ContentEntity, Integer> dao = null;
		private DatabaseHelper dbHelper;
		
		public AsyncDownTask(ContentEntity cEntity)
		{
			this.cEntity = cEntity;
		}

		@Override
		protected void onPreExecute()
		{
			dbHelper = new DatabaseHelper(activity.getApplicationContext());
			dao = dbHelper.getContentDataDao();
		}

		@Override
		protected Integer doInBackground(Void... params)
		{
			String address = cEntity.getUrl();
			if (address != null && !address.equals(""))
			{
				File target = new File(StorageUtils.FILE_TEMP_ROOT + cEntity.getServerID() + ".zip");

				OutputStream output = null;
				try
				{
					URL url = new URL(address);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();

					InputStream input = conn.getInputStream();
					if (!target.exists())
					{
						target.createNewFile();// �½��ļ�
						output = new FileOutputStream(target);
						// ��ȡ���ļ�
						byte[] buffer = new byte[4 * 1024];
						int count = 0;
						while((count = input.read(buffer)) != -1)
				        {
							output.write(buffer, 0, count);
				        }
						
						output.flush();

						//У������ѹ������MD5ֵ
						
						if (cEntity.getMd5().equals(MD5Tools.getFileMD5String(target)))
						{
							// ���غ��ж��ļ����Ƿ����
							File file = new File(StorageUtils.FILE_ROOT + cEntity.getServerID());
							if (file.isDirectory())
							{
								Log.d(TAG, "ɾ���Ѿ����ڵ��ļ��У�" + cEntity.getServerID());
								StorageUtils.delete(file);
							}
												
							// ������Ϻ��ѹ�ļ�
							FileOperationTools.unZip(StorageUtils.FILE_TEMP_ROOT + cEntity.getServerID() + ".zip", StorageUtils.FILE_ROOT);
							
							// �������ݿ����Ϣ
							cEntity.setDownloadFlag(1);
							dao.update(cEntity);
							
						}
						else
						{
							Log.d(TAG, "�´���������������"+cEntity.getServerID());
						}
						StorageUtils.delete(target);
					}
					return SUCCESS;
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
					return FAILURE;
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return FAILURE;
				}
				finally
				{
					try
					{
						if (output != null)
						{
							output.close();
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			return FAILURE;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			if (result == SUCCESS)
			{
				popWinWithContentEntity(cEntity);
			}
		}
	}

}
