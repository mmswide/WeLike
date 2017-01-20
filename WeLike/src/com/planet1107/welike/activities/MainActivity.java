package com.planet1107.welike.activities;

import java.util.Locale;

import com.planet1107.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.fragments.NearbyFragment;
import com.planet1107.welike.fragments.NewPostFragment;
import com.planet1107.welike.fragments.PopularFragment;
import com.planet1107.welike.fragments.ProfileFragment;
import com.planet1107.welike.fragments.TimelineFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	ActionBar actionBar;
	int actionBarSelectedTabPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		String tabBarIcons[] = {"tabbar_timeline", "tabbar_popular", "tabbar_newpost", "tabbar_nearby", "tabbar_profile"};
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			Tab newTab = actionBar.newTab();
			newTab.setText(mSectionsPagerAdapter.getPageTitle(i));
			
			Resources resources = getResources();
			int resourceId = resources.getIdentifier(tabBarIcons[i], "drawable", getPackageName());			
			newTab.setIcon(resources.getDrawable(resourceId));
			newTab.setTabListener(this);
			actionBar.addTab(newTab);
		}
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		actionBar.setSelectedNavigationItem(actionBarSelectedTabPosition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		
		if (tab.getPosition() == 2) {
    		Intent newPost = new Intent(MainActivity.this, NewPostActivity.class);
    		startActivity(newPost);
		} else {
			mViewPager.setCurrentItem(tab.getPosition());
			actionBarSelectedTabPosition = tab.getPosition();
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0) {
				return new TimelineFragment();
			} else if (position == 1) {
				return new PopularFragment();
			} else if (position == 2) {
				return new NewPostFragment();
			} else if (position == 3) {
				return new NearbyFragment();
			} else {
				return new ProfileFragment();
			}
		}

		@Override
		public int getCount() {

			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
				case 3:
					return getString(R.string.title_section4).toUpperCase(l);
				case 4:
					return getString(R.string.title_section5).toUpperCase(l);
			}
			return null;
		}
	}

	public void textViewFollowersOnClick(View v) {
		
		Connect sharedConnect = Connect.getInstance(this);
		Intent followersIntent = new Intent(this, FollowersActivity.class);
		followersIntent.putExtra("userID",sharedConnect.getCurrentUser().userID);
		startActivity(followersIntent);
	}
	
	public void textViewFollowingOnClick(View v) {
		
		Connect sharedConnect = Connect.getInstance(this);
		Intent followingIntent = new Intent(this, FollowingActivity.class);
		followingIntent.putExtra("userID",sharedConnect.getCurrentUser().userID);
		startActivity(followingIntent);
	}

	public void buttonSearchUsersOnClick(View v) {
	
		Intent searchIntent = new Intent(this, SearchActivity.class);
		startActivity(searchIntent);
	}
	
	public void buttonLogoutOnClick(View v) {
		
		Connect sharedConnect = Connect.getInstance(this);
		sharedConnect.getCurrentUser().deleteFromDisk(this);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
