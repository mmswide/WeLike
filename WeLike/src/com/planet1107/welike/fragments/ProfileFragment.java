package com.planet1107.welike.fragments;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.planet1107.welike.R;
import com.planet1107.welike.activities.EditProfileActivity;
import com.planet1107.welike.connect.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public void onResume () {
		
		super.onResume();
		((TextView)getActivity().findViewById(R.id.textViewProfileFollowers)).setText("followers " + String.valueOf(sharedConnect.getCurrentUser().userFollowersCount));
		((TextView)getActivity().findViewById(R.id.textViewProfileFollowing)).setText("following " + String.valueOf(sharedConnect.getCurrentUser().userFollowingCount));
		((TextView)getActivity().findViewById(R.id.textViewProfileUsername)).setText(String.valueOf(sharedConnect.getCurrentUser().userFullName));
		UrlImageViewHelper.setUrlDrawable(((ImageView)getView().findViewById(R.id.imageViewProfileUserImage)), sharedConnect.getCurrentUser().userAvatarPath);
		if (sharedConnect.getCurrentUser().userType == User.UserType.UserTypeCompany) {
			((TextView)getActivity().findViewById(R.id.textViewCompanyAddress)).setText("" + String.valueOf(sharedConnect.getCurrentUser().companyAddress));
			((TextView)getActivity().findViewById(R.id.textViewCompanyPhone)).setText("" + String.valueOf(sharedConnect.getCurrentUser().companyPhone));
			((TextView)getActivity().findViewById(R.id.textViewCompanyWebsite)).setText("" + String.valueOf(sharedConnect.getCurrentUser().companyWeb));
		} else {
			((TextView)getActivity().findViewById(R.id.textViewCompanyAddress)).setText("");
			((TextView)getActivity().findViewById(R.id.textViewCompanyPhone)).setText("");
			((TextView)getActivity().findViewById(R.id.textViewCompanyWebsite)).setText("");
		}
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.fragment_profile_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.action_edit) {
			menuItemEditOnClick();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void menuItemEditOnClick() {
		
		Intent editItent = new Intent(getActivity(), EditProfileActivity.class);
		startActivity(editItent);
	}
}
