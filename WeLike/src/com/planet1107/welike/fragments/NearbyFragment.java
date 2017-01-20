package com.planet1107.welike.fragments;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.planet1107.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.support.v4.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearbyFragment extends BaseFragment implements OnConnectionFailedListener, LocationListener, LoaderCallbacks<List<User>> {

	LocationClient locationClient;
	Location lastLocation;
	private GoogleMap mMap;

	private static View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.fragment_nearby, container, false);
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    }
	    return view;
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		setHasOptionsMenu(true);
		mMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
		locationClient = new LocationClient(getActivity(), new GooglePlayServicesClient.ConnectionCallbacks() {
			
			@Override
			public void onDisconnected() {
				
			}
			
			@Override
			public void onConnected(Bundle connectionHint) {
				LocationRequest locationRequest = LocationRequest.create();
				locationClient.requestLocationUpdates(locationRequest, NearbyFragment.this);
			}
		}, this);
		locationClient.connect();
		mMap.setMyLocationEnabled(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.nearby, menu);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
	}

	@Override
	public void onLocationChanged(Location location) {

	    if (lastLocation == null || location.distanceTo(lastLocation) > 1000) {
	        getLoaderManager().initLoader(0, null, this);
	        lastLocation = location;
	    }
	}
	
	public class NearbyLoader extends AsyncTaskLoader<List<User>> {

	    List<User> users;

	    public NearbyLoader(Context context) {
	        
	    	super(context);
	    }

	    @Override 
	    public List<User> loadInBackground() {
	    	Connect sharedConnect = Connect.getInstance(getActivity());
	    	Location lastLocation = NearbyFragment.this.lastLocation;
	       ArrayList<User> users = sharedConnect.getUsersAround(lastLocation.getLatitude(), lastLocation.getLongitude(), 10000, 1, 20);
	       return users;
	    }

	    @Override
	    public void deliverResult(List<User> users) {
	        
	    	if (isReset()) {
	            if (users != null) {
	                onReleaseResources(users);
	            }
	        }
	        List<User> oldUsers = users;
	        this.users = users;

	        if (isStarted()) {
	            super.deliverResult(users);
	        }

	        if (oldUsers != null) {
	            onReleaseResources(oldUsers);
	        }
	        
	    }

	    @Override 
	    protected void onStartLoading() {
	        
	    	if (users != null) {
	            deliverResult(users);
	        }

	        if (takeContentChanged() || this.users == null) {
	            forceLoad();
	        }
	    }

	    @Override 
	    protected void onStopLoading() {

	    	cancelLoad();
	    }

	    @Override 
	    public void onCanceled(List<User> users) {
	        
	    	super.onCanceled(users);
	        onReleaseResources(users);
	    }

	    @Override 
	    protected void onReset() {
	    
	    	super.onReset();
	        onStopLoading();
	        if (this.users != null) {
	            onReleaseResources(this.users);
	            this.users = null;
	        }
	    }

	    protected void onReleaseResources(List<User> users) {

	    }
	}

	@Override
	public Loader<List<User>> onCreateLoader(int arg0, Bundle arg1) {

		return null;
	}

	@Override
	public void onLoadFinished(Loader<List<User>> arg0, List<User> users) {
		
		for (User user : users) {
			MarkerOptions options = new MarkerOptions();
			options.position(new LatLng(user.latitude, user.longitude));
			mMap.addMarker(options);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<User>> arg0) {
		
	}
}
