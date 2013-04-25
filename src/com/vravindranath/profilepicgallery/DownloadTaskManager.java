package com.vravindranath.profilepicgallery;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class DownloadTaskManager {
	private ArrayList<AsyncTask<Friend, Integer, Bitmap>> mTaskList = null;
	
	public DownloadTaskManager() {
		mTaskList = new ArrayList<AsyncTask<Friend,Integer,Bitmap>>();
	}
	
	public void addTask(AsyncTask<Friend, Integer, Bitmap> task) {
		synchronized (mTaskList) {
			mTaskList.add(task);
		}
		task.execute();
	}
	
	public void removeTask(AsyncTask<Friend, Integer, Bitmap> task) {
		if(!task.isCancelled()) {
			task.cancel(true);
		}
		
		synchronized (mTaskList) {
			mTaskList.remove(task);
		}
	}
}
