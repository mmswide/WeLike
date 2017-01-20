package com.planet1107.welike.activities;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.planet1107.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	int mUserID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setTitle("Profile");
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    mUserID = extras.getInt("userID");
		}
		
		new AsyncTask<Void, Void, User>() {

			@Override
			protected User doInBackground(Void... params) {

				Connect sharedConnect = Connect.getInstance(ProfileActivity.this);
				return sharedConnect.getUser(mUserID);
			}
			
			@Override
			protected void onPostExecute(User user) {
				
				((TextView)findViewById(R.id.textViewProfilePreviewFollowers)).setText("followers " + String.valueOf(user.userFollowersCount));
				((TextView)findViewById(R.id.textViewProfilePreviewFollowing)).setText("following " + String.valueOf(user.userFollowingCount));
				((TextView)findViewById(R.id.textViewProfilePreviewUsername)).setText(String.valueOf(user.userFullName));
				((TextView)findViewById(R.id.textViewAddress)).setText(String.valueOf(user.companyAddress));
				((TextView)findViewById(R.id.textViewWeb)).setText(String.valueOf(user.companyWeb));
				((TextView)findViewById(R.id.textViewPhone)).setText(String.valueOf(user.companyPhone));
				UrlImageViewHelper.setUrlDrawable(((ImageView)findViewById(R.id.imageViewProfilePreviewUserImage)), user.userAvatarPath);
			}
		}.execute();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.profile_menu, menu);
		return true;
	}

	public void textViewFollowersOnClick(View v) {
		
		Intent followersIntent = new Intent(this, FollowersActivity.class);
		followersIntent.putExtra("userID", mUserID);
		startActivity(followersIntent);
	}
	
	public void textViewFollowingOnClick(View v) {
		
		Intent followingIntent = new Intent(this, FollowingActivity.class);
		followingIntent.putExtra("userID", mUserID);
		startActivity(followingIntent);
	}
	
}
