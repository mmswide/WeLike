package com.planet1107.welike.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.planet1107.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;
import com.planet1107.welike.activities.MainActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.planet1107.welike.other.ChoosePictureUtility;

public class RegisterActivity extends Activity implements OnCheckedChangeListener, OnMapLongClickListener {

	private static final int SELECT_PHOTO = 100;

	String username;
	String password;
	String repassword;
	String email;
	Bitmap userAvatar;
	int userType;
	String fullName;
	String info;
	double latitude;
	double longitude;
	String companyAddress;
	String companyPhone;
	String companyWeb;
	
	EditText mEditTextLocation;
	EditText mEditTextUsername;
	EditText mEditTextPassword;
	EditText mEditTextFullName;
	EditText mEditTextEmail;
	EditText mEditTextInfo;
	EditText mEditTextCompanyAddress;
	EditText mEditTextCompanyPhone;
	EditText mEditTextCompanyWeb;
	EditText mEditTextRepassword;
	TextView mTextViewUserAvatar;
	ImageView mImageViewUserAvatar;
	RadioGroup mRadioGroupIndividualCompany;
	LinearLayout mLinearLayoutCompany;
	GoogleMap mMap;
	
	LatLng userCoordinate;
	UserRegisterTask mUserRegisterTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		mRadioGroupIndividualCompany = (RadioGroup)findViewById(R.id.radioGroupIndividialCompany);
		mRadioGroupIndividualCompany.setOnCheckedChangeListener(this);
		
		mLinearLayoutCompany = (LinearLayout)findViewById(R.id.linearLayoutCompany);
		
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);

        mEditTextLocation = (EditText) findViewById(R.id.editTextLocation);
        mEditTextUsername = (EditText) findViewById(R.id.editTextUsername);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextFullName = (EditText) findViewById(R.id.editTextFullName);
        mEditTextRepassword = (EditText) findViewById(R.id.editTextRepassword);
        mEditTextCompanyPhone = (EditText) findViewById(R.id.editTextCompanyPhone);
        mEditTextCompanyWeb = (EditText) findViewById(R.id.editTextCompanyWeb);
        mTextViewUserAvatar = (TextView) findViewById(R.id.textViewUserAvatar);
        mImageViewUserAvatar = (ImageView) findViewById(R.id.imageViewUserAvatar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if (checkedId == R.id.radio0) {
			mLinearLayoutCompany.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));
		} else if (checkedId == R.id.radio1) {
			mLinearLayoutCompany.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
	
	@Override    
	public void onMapLongClick(LatLng point) {
		
		mMap.addMarker(new MarkerOptions()
	        .position(point)
	        .title("You are here")           
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));  
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
			Address address = addresses.get(0);
			mEditTextLocation.setText(address.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void buttonRegisterOnClick(View v) {
		
		attemptRregister();
	}
	
	public void imageViewAvatarOnClick(View v) {
		
		ChoosePictureUtility.getInstance().choosePicture(this);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    
		super.onActivityResult(requestCode, resultCode, data); 
		switch (requestCode) { 
		    case ChoosePictureUtility.SELECT_PHOTO:
		    	ChoosePictureUtility.getInstance().onActivityResult(requestCode, resultCode, data);
		    	Bitmap bitmap = ChoosePictureUtility.getInstance().getPickedImage();
		    	if (bitmap != null) {
		    		userAvatar = bitmap;
		            mImageViewUserAvatar.setImageBitmap(userAvatar);
		    	}
                break;
		    }
	}
	
	public void attemptRregister() {
		
		if (mUserRegisterTask == null) {
			
			mEditTextLocation.setError(null);
			mEditTextUsername.setError(null);
			mEditTextPassword.setError(null);
			mEditTextFullName.setError(null);
			mEditTextEmail.setError(null);
			mEditTextCompanyPhone.setError(null);
			mEditTextCompanyWeb.setError(null);
			
			username = mEditTextUsername.getText().toString();
			password = mEditTextPassword.getText().toString();
			repassword = mEditTextRepassword.getText().toString();
			email = mEditTextEmail.getText().toString();
			userType = mRadioGroupIndividualCompany.getCheckedRadioButtonId() == R.id.radio0 ? 1 : 2;
			fullName = mEditTextFullName.getText().toString();
			latitude = userCoordinate != null ? userCoordinate.latitude : 0;
			longitude = userCoordinate != null ? userCoordinate.longitude : 0;
			companyPhone = mEditTextCompanyPhone.getText().toString();
			companyWeb = mEditTextCompanyWeb.getText().toString();

			boolean cancel = true;
			View focusView = null;

			if (TextUtils.isEmpty(password)) {
				mEditTextPassword.setError(getString(R.string.error_field_required));
				focusView = mEditTextPassword;
			} else if (password.length() < 4) {
				mEditTextPassword.setError(getString(R.string.error_invalid_password));
				focusView = mEditTextPassword;
			} else if (TextUtils.isEmpty(username)) {
				mEditTextUsername.setError(getString(R.string.error_field_required));
				focusView = mEditTextUsername;
			} else if (TextUtils.isEmpty(fullName)) {
				mEditTextFullName.setError(getString(R.string.error_field_required));
				focusView = mEditTextFullName;
			} else if (TextUtils.isEmpty(email)) {
				mEditTextEmail.setError(getString(R.string.error_field_required));
				focusView = mEditTextEmail;
			} else if (userAvatar == null) {
				mTextViewUserAvatar.setError(getString(R.string.error_field_required));
				focusView = mTextViewUserAvatar;
			} else if (!password.equals(repassword)) {
				mEditTextRepassword.setError("Password and repassword doesn't match");
				focusView = mEditTextRepassword;
			} else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
				mEditTextEmail.setError("Email is not in valid format");
				focusView = mEditTextEmail;
			} else {
				cancel = false;
				mUserRegisterTask = new UserRegisterTask();
				mUserRegisterTask.execute();
			}

			if (cancel) {
				focusView.requestFocus();
			}
		}
	}

	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog mLoadingDialog = new ProgressDialog(RegisterActivity.this);

		@Override
	    protected void onPreExecute() {
	    	
	    	mLoadingDialog.setMessage("Registring...");
	    	mLoadingDialog.show();
	    }
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			User user = Connect.getInstance(getBaseContext()).registerUser(username, password, email, userAvatar, userType, fullName, info, latitude, longitude, companyAddress, companyPhone, companyWeb);
			if (user != null && user.userEmail != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			
			mUserRegisterTask = null;
			mLoadingDialog.dismiss();

			if (success) {
				Intent main = new Intent(RegisterActivity.this, MainActivity.class);
				startActivity(main);
			}
		}

		@Override
		protected void onCancelled() {
			
			mUserRegisterTask = null;
			mLoadingDialog.dismiss();

		}
	}
}
