package com.planet1107.welike.activities;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.planet1107.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EditProfileActivity extends Activity {

	private static final int SELECT_PHOTO = 100;
	Bitmap userAvatar;
	ImageView mImageViewUserAvatar;
	EditText mEditTextEditUsername;
	EditText mEditTextEditEmail;
	EditText mEditTextEditFullName;
	EditText mEditTextLocation;
	EditText mEditTextCompanyPhone;
	EditText mEditTextCompanyWeb;
	LinearLayout mLinearLayoutCompany;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		setTitle("Edit Profile");

		Connect sharedConnect = Connect.getInstance(this);
		mImageViewUserAvatar = (ImageView) findViewById(R.id.imageViewProfileUserImage);
		mEditTextLocation = (EditText) findViewById(R.id.editTextLocation);
        mEditTextCompanyPhone = (EditText) findViewById(R.id.editTextCompanyPhone);
        mEditTextCompanyWeb = (EditText) findViewById(R.id.editTextCompanyWeb);
        mLinearLayoutCompany = (LinearLayout)findViewById(R.id.linearLayoutCompany);
        mEditTextEditEmail = (EditText) findViewById(R.id.editTextEditEmail);
        mEditTextEditFullName = (EditText) findViewById(R.id.editTextEditFullName);
        if (sharedConnect.getCurrentUser().userType == User.UserType.UserTypeCompany) {
        	mLinearLayoutCompany.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        } else {
        	mLinearLayoutCompany.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));
        }
        
		mEditTextEditEmail.setText(sharedConnect.getCurrentUser().userEmail);
		mEditTextEditFullName.setText(sharedConnect.getCurrentUser().userFullName);
		mEditTextCompanyWeb.setText(sharedConnect.getCurrentUser().companyWeb);
		mEditTextCompanyPhone.setText(sharedConnect.getCurrentUser().companyPhone);
		mEditTextLocation.setText(sharedConnect.getCurrentUser().companyAddress);
		UrlImageViewHelper.setUrlDrawable(mImageViewUserAvatar, sharedConnect.getCurrentUser().userAvatarPath);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.action_save) {
			menuItemSaveOnClick();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void menuItemSaveOnClick() {
		
		new AsyncTask<Void, Void, User>() {

			private ProgressDialog mLoadingDialog = new ProgressDialog(EditProfileActivity.this);

			@Override
		    protected void onPreExecute() {
		    	
		    	mLoadingDialog.setMessage("Saving changnes...");
		    	mLoadingDialog.show();
		    }
			
			@Override
			protected User doInBackground(Void... params) {
				
				Connect sharedConnect = Connect.getInstance(EditProfileActivity.this);
				int userType;
				if (sharedConnect.getCurrentUser().userType == User.UserType.UserTypeCompany) {
					userType = 2;
				} else {
					userType = 1;
				}
				return sharedConnect.updateUser(sharedConnect.getCurrentUser().userID, mEditTextEditEmail.getText().toString(), userAvatar, userType, mEditTextEditFullName.getText().toString(), sharedConnect.getCurrentUser().userInfo, sharedConnect.getCurrentUser().latitude, sharedConnect.getCurrentUser().longitude, mEditTextLocation.getText().toString(), mEditTextCompanyPhone.getText().toString(), mEditTextCompanyWeb.getText().toString());
			}
			
			protected void onPostExecute(User results) {
		         
				mLoadingDialog.dismiss();
		     }
			
		}.execute();
	}
	
	public void imageViewEditProfileOnClick(View v) {
		
		Intent intentPhotoPicker = new Intent(Intent.ACTION_PICK);
		intentPhotoPicker.setType("image/*");
		startActivityForResult(intentPhotoPicker, SELECT_PHOTO);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    
		super.onActivityResult(requestCode, resultCode, data); 
		switch (requestCode) { 
		    case SELECT_PHOTO:
		        if (resultCode == RESULT_OK) {  
		            Uri selectedImage = data.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};
		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();
		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String filePath = cursor.getString(columnIndex);
		            cursor.close();
		            userAvatar = BitmapFactory.decodeFile(filePath);
		            mImageViewUserAvatar.setImageBitmap(userAvatar);
		        }
                break;
		    }
	}

}
