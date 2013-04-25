package com.vravindranath.profilepicgallery;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.vravindranath.profilepicgallery.DownloadTask.OnDownloadCompletedListener;

public class Friend {
	private String mUserId = null;
	private String mProfilePicUrl = null;
	private Bitmap mProfilePic = null;
	private String TAG = "Friend";
	private int mPosition = 0;
	
//	private boolean mDownloadStarted = false;
	private Session mSession = null;
	
	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public Friend(String userId, Session session) {
		mUserId = userId;
		mSession  = session;
	}
	
	public void setProfilePicUrl(String url) {
		//Log.i(TAG , "url set: " + url);
		mProfilePicUrl = url;
	}
	
	public void setProfilePicBitmap(Bitmap profilePic) {
		mProfilePic = profilePic;
	}

	public String getId() {
		return mUserId;
	}

	public String getProfilePicUrl() {
		return mProfilePicUrl;
	}
	
	private void fetchProfilePicUrl(final OnDownloadCompletedListener listener) {
		String graphPath = getId() + "/picture";
		
		Bundle params = new Bundle();
		params.putString("type", "square");
		params.putBoolean("redirect", false);
		params.putInt("width", 100);
		params.putInt("height", 100);
		
		Request.Callback callback = new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				//Log.i(TAG, "onCompleted...");
				FacebookRequestError error = response.getError();

				if (error != null) {
					Log.e(TAG, error.getErrorType() + ": " + error.getErrorMessage());
					return;
				}

				JSONObject friendData = response.getGraphObject().getInnerJSONObject();

				try {
					String url = friendData.getJSONObject("data").getString("url");
					if (url != null) {
						setProfilePicUrl(url);
						DownloadTask dTask = new DownloadTask();
						dTask.setDownloadCompletedListener(listener);
						dTask.execute(Friend.this);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Request request = new Request(mSession, graphPath, params,
				HttpMethod.GET, callback);
		RequestAsyncTask requestTask = new RequestAsyncTask(request);
		requestTask.execute();
	}

	public Bitmap getProfilePic(OnDownloadCompletedListener listener) {
		
		if(mProfilePicUrl == null) {
			fetchProfilePicUrl(listener);
		}
		
		return mProfilePic;
	}
}
