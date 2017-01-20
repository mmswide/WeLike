package com.planet1107.welike.fragments;

import com.planet1107.welike.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewPostFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_newpost, container, false);
		return rootView;
	}
}
