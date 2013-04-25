package com.vravindranath.profilepicgallery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadTask extends AsyncTask<Friend, Integer, Bitmap> {

	private Friend mFriend = null;
	private String TAG = "DownloadTask";
	private OnDownloadCompletedListener mDownloadCompletedListener = null;
	
	@Override
	protected Bitmap doInBackground(Friend... params) {
		Bitmap bitmap = null;
		String url = null;
		
		mFriend = params[0];
		
		url = mFriend.getProfilePicUrl();
		
		if(url == null) {
			Log.i(TAG, "url is null");
			return null;
		}
		
		Log.e(TAG, "Download started...");
		// Start the download
		URL imageUrl;

		try {
			imageUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) imageUrl
					.openConnection();
			connection.connect();
			InputStream is = connection.getInputStream();
			bitmap = saveImageAsBitmap(is);
			bitmap = ImageHelper.getRoundBitmap(bitmap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}
	
	public void setDownloadCompletedListener(OnDownloadCompletedListener listener) {
		mDownloadCompletedListener = listener;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		mFriend.setProfilePicBitmap(result);
		if (mDownloadCompletedListener != null) {
			mDownloadCompletedListener.onDownloadCompleted();
			Log.e(TAG, "Download Completed...");
		}
	}
	
	private Bitmap saveImageAsBitmap(InputStream is) {
		Bitmap bmp = null;
		bmp = BitmapFactory.decodeStream(is);
		return bmp;
	}
	
	public interface OnDownloadCompletedListener {
		public void onDownloadCompleted();
	}
}