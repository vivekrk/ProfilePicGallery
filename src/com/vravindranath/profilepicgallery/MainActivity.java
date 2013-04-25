package com.vravindranath.profilepicgallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.vravindranath.profilepicgallery.FriendlistAdapter.OnFriendlistFetchCallback;

public class MainActivity extends Activity implements OnFriendlistFetchCallback {

	private final String TAG = "ProfilePicGallery";
	private FriendlistAdapter mFriendlistAdapter = null;
	private GridView mGridView;
	
	private ProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGridView = (GridView) findViewById(R.id.gridView);
		mProgressDialog = ProgressDialog.show(this, "Login", "Logging in...");
		Session.openActiveSession(this, true, new StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if(session.isOpened()) {
					Log.i(TAG, "Session is opened");
					mFriendlistAdapter = new FriendlistAdapter(MainActivity.this, session);
					mGridView.setAdapter(mFriendlistAdapter);
				}
			}
		});
		
//		PackageInfo info;
//		try {
//		    info = getPackageManager().getPackageInfo("com.vravindranath.profilepicgallery", PackageManager.GET_SIGNATURES);
//		    for (Signature signature : info.signatures) {
//		        MessageDigest md;
//		        md = MessageDigest.getInstance("SHA");
//		        md.update(signature.toByteArray());
//		        String something = new String(Base64.encode(md.digest(), 0));
//		        //String something = new String(Base64.encodeBytes(md.digest()));
//		        Log.e(TAG, something);
//		    }
//		} catch (NameNotFoundException e1) {
//		    Log.e("name not found", e1.toString());
//		} catch (NoSuchAlgorithmException e) {
//		    Log.e("no such an algorithm", e.toString());
//		} catch (Exception e) {
//		    Log.e("exception", e.toString());
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public void onFriendlistFetched() {
		mProgressDialog.dismiss();
		mFriendlistAdapter.notifyDataSetChanged();
	}
}
