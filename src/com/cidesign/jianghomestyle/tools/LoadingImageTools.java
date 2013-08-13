package com.cidesign.jianghomestyle.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class LoadingImageTools
{
	public void loadingImage(ImageView view, String path)
	{
		new AsyncLoadImage(view).execute(path);
	}

	class AsyncLoadImage extends AsyncTask<String, Void, Bitmap>
	{
		private ImageView view;

		public AsyncLoadImage(ImageView view)
		{
			this.view = view;
		}

		@Override
		protected void onPreExecute()
		{
			// 第一个执行方法
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params)
		{
			return getImageBitmap(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bm)
		{
			if (bm != null && view != null)
			{
				view.setImageBitmap(bm);
			}
		}
	}

	public void loadingNativeImage(Activity ctx, ImageView view, String path)
	{
		new AsyncLoadNativeImage(ctx, view).execute(path);
	}

	class AsyncLoadNativeImage extends AsyncTask<String, Void, Bitmap>
	{
		private Activity ctx;
		private ImageView view;

		public AsyncLoadNativeImage(Activity ctx, ImageView view)
		{
			this.ctx = ctx;
			this.view = view;
		}

		@Override
		protected Bitmap doInBackground(String... params)
		{
			try
			{
				InputStream is = ctx.getAssets().open(params[0]);
				Bitmap bm = null;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 3;
				options.inTempStorage = new byte[12 * 1024]; // 12k
				bm = BitmapFactory.decodeStream(is, null, options);

				return bm;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (java.lang.OutOfMemoryError e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bm)
		{
			if (bm != null && view != null)
			{
				view.setImageBitmap(bm);
			}
		}
	}

	public static Bitmap getDetailImageBitmap(Context ctx,String path)
	{
		File file = new File(path);
		RandomAccessFile in = null;
		Bitmap bmp = null;
		try
		{
			in = new RandomAccessFile(file, "rws");
			final FileChannel channel = in.getChannel();
			final int fileSize = (int) channel.size();
			final byte[] testBytes = new byte[fileSize];
			final ByteBuffer buff = ByteBuffer.allocate(fileSize);
			final byte[] buffArray = buff.array();
			@SuppressWarnings("unused")
			final int buffBase = buff.arrayOffset();

			channel.position(0);
			channel.read(buff);
			buff.flip();
			buff.get(testBytes);
			bmp = Bitmap_process(buffArray);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (java.lang.OutOfMemoryError e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (bmp == null)
		{
			InputStream is = null;
			try
			{
				is = ctx.getAssets().open("thumb_default.png");
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inTempStorage = new byte[9 * 1024]; // 9k
				bmp = BitmapFactory.decodeStream(is, null, options);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}			
		}
		return bmp;
	}

	public static Bitmap getImageBitmap(String path)
	{
		// Allocate files and objects outside of timingoops
		File file = new File(path);
		RandomAccessFile in = null;
		Bitmap bmp = null;
		try
		{
			in = new RandomAccessFile(file, "rws");
			final FileChannel channel = in.getChannel();
			final int fileSize = (int) channel.size();
			final byte[] testBytes = new byte[fileSize];
			final ByteBuffer buff = ByteBuffer.allocate(fileSize);
			final byte[] buffArray = buff.array();
			@SuppressWarnings("unused")
			final int buffBase = buff.arrayOffset();

			channel.position(0);
			channel.read(buff);
			buff.flip();
			buff.get(testBytes);
			bmp = Bitmap_process(buffArray);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (java.lang.OutOfMemoryError e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return bmp;
	}

	public static Bitmap Bitmap_process(byte[] buffArray)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inDither = false; // Disable Dithering mode
		options.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		options.inInputShareable = true; // Which kind of reference will be used
											// to recover the Bitmap data after
											// being clear, when it will be used
											// in the future
		options.inTempStorage = new byte[32 * 1024]; // Allocate some temporal
														// memory for decoding

		options.inSampleSize = 7;

		Bitmap imageBitmap = BitmapFactory.decodeByteArray(buffArray, 0, buffArray.length, options);
		return imageBitmap;
	}

	public static Bitmap loadingNative(Context ctx, String path)
	{
		InputStream is = null;
		Bitmap bm = null;
		try
		{
			is = ctx.getAssets().open(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 3;
			options.inTempStorage = new byte[12 * 1024]; // 12k
			bm = BitmapFactory.decodeStream(is, null, options);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return bm;
	}
}
