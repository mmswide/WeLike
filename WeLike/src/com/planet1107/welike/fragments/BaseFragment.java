package com.planet1107.welike.fragments;

import com.planet1107.welike.connect.Connect;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	Connect sharedConnect;
	
	public void onActivityCreated (Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		sharedConnect = Connect.getInstance(getActivity());
	}
}
