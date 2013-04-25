package com.vravindranath.profilepicgallery;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.vravindranath.profilepicgallery.DownloadTask.OnDownloadCompletedListener;

public class FriendlistAdapter extends BaseAdapter implements OnDownloadCompletedListener {

	private ArrayList<Friend> mFriendList = null; 
	private Context mContext = null;
//	private final String TAG = "FriendlistAdapter";
	private Session mSession = null;
	
	public FriendlistAdapter(Context context, Session session) {
		mContext = context;
		mFriendList = new ArrayList<Friend>();
		mSession  = session;
		
		Request.executeMyFriendsRequestAsync(mSession,
				new GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {
						//Fetch all Profile pic urls.
						for (GraphUser graphUser : users) {
							Friend friend = new Friend(graphUser.getId(), mSession);
							mFriendList.add(friend);
						}
						
						((MainActivity) mContext).onFriendlistFetched();
					}

				});
	}
	
	@Override
	public int getCount() {
		if(mFriendList == null) {
			return 0;
		}
		
		return mFriendList.size();
	}

	@Override
	public Friend getItem(int position) {
		if(mFriendList == null) {
			return null;
		}
		
		return mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.i(TAG, "getView position: " + position);
		View view = null;
		Bitmap profilePicBitmap = null;
		ImageView profilePicView = null;
		
		if(convertView == null) {
			view = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.gridview_cell, parent, false);
		} else {
			view = convertView;
		}
		
		profilePicView = (ImageView) view.findViewById(R.id.profile_pic_view);
		profilePicBitmap = mFriendList.get(position).getProfilePic(this);
		
		if(profilePicBitmap == null) {
			//Log.i(TAG, "profilePicBitmap is null");
			//getProfilePicUrl(mFriendList.get(position));
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.default_image);
			profilePicView.setImageDrawable(drawable);
			return view;
		}
		
		profilePicView.setImageBitmap(profilePicBitmap);
		return view;
	}
	
	public interface OnFriendlistFetchCallback {
		public void onFriendlistFetched();
	}

	@Override
	public void onDownloadCompleted() {
		notifyDataSetChanged();
	}
}
