package com.planet1107.welike.activities;

import com.planet1107.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private UserLoginTask userLoginTask = null;

	private String username;
	private String password;

	private EditText editTextUsername;
	private EditText editTextPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("Login");

		username = getIntent().getStringExtra(EXTRA_EMAIL);
		editTextUsername = (EditText) findViewById(R.id.editTextEmail);
		editTextUsername.setText(username);

		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});


		findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void attemptLogin() {
		
		if (userLoginTask == null) {
			editTextUsername.setError(null);
			editTextPassword.setError(null);
			username = editTextUsername.getText().toString();
			password = editTextPassword.getText().toString();

			boolean cancel = false;
			View focusView = null;

			if (TextUtils.isEmpty(password)) {
				editTextPassword.setError(getString(R.string.error_field_required));
				focusView = editTextPassword;
				cancel = true;
			} else if (password.length() < 4) {
				editTextPassword.setError(getString(R.string.error_invalid_password));
				focusView = editTextPassword;
				cancel = true;
			} else if (TextUtils.isEmpty(username)) {
				editTextUsername.setError(getString(R.string.error_field_required));
				focusView = editTextUsername;
				cancel = true;
			}

			if (cancel) {
				focusView.requestFocus();
			} else {
				userLoginTask = new UserLoginTask();
				userLoginTask.execute();
			}
		}
	}

	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog mLoadingDialog = new ProgressDialog(LoginActivity.this);

		@Override
	    protected void onPreExecute() {
	    	
	    	mLoadingDialog.setMessage("Signing in...");
	    	mLoadingDialog.show();
	    }
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			User user = Connect.getInstance(getBaseContext()).loginUser(username, password);
			if (user != null && user.userEmail != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			
			userLoginTask = null;
			mLoadingDialog.dismiss();

			if (success) {
				Intent main = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(main);
			} else {
				editTextPassword.setError(getString(R.string.error_incorrect_password));
				editTextPassword.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			
			userLoginTask = null;
			mLoadingDialog.dismiss();
		}
	}
}
