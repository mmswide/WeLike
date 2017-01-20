package com.planet1107.welike.fragments;

import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.planet1107.welike.R;
import com.planet1107.welike.adapters.TimelineAdapter;
import com.planet1107.welike.connect.Post;
import com.planet1107.welike.loaders.TimelineLoader;

import android.support.v4.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v4.app.LoaderManager;

public class TimelineFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Post>>, OnRefreshListener, OnScrollListener {

    TimelineAdapter mTimelineAdapter;
    TimelineLoader mTimelineLoader;
    PullToRefreshLayout mPullToRefreshLayout;
    ListView mListViewTimeline;
    ProgressBar mProgressBarLoading;
    TextView mTextViewNoItems;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_timeline, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
	    mPullToRefreshLayout = (PullToRefreshLayout) getActivity().findViewById(R.id.ptr_layout);
	    ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);
	  
	    mProgressBarLoading = (ProgressBar) getActivity().findViewById(R.id.progressBarLoading);
	    mTextViewNoItems = (TextView) getActivity().findViewById(R.id.textViewNoItems);

        mTimelineAdapter = new TimelineAdapter(getActivity());

        mListViewTimeline = (ListView) getActivity().findViewById(R.id.listViewTimeline);
        mListViewTimeline.setEmptyView(mProgressBarLoading);
        mListViewTimeline.setAdapter(mTimelineAdapter);
        mListViewTimeline.setOnScrollListener(this);
        mListViewTimeline.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        
        getLoaderManager().initLoader(0, null, this);
	}

	@Override
    public Loader<List<Post>> onCreateLoader(int id, Bundle args) {
        
        mTimelineLoader = new TimelineLoader(getActivity(), mListViewTimeline);
		return mTimelineLoader;
    }

	@Override
	public void onLoadFinished(Loader<List<Post>> arg0, List<Post> data) {
        
		mTimelineAdapter.setData(data);
		mTimelineAdapter.notifyDataSetChanged();
		mPullToRefreshLayout.setRefreshComplete();
		mTextViewNoItems.setVisibility(View.VISIBLE);
		mProgressBarLoading.setVisibility(View.INVISIBLE);
		mListViewTimeline.setEmptyView(mTextViewNoItems);
	}

	@Override
	public void onLoaderReset(Loader<List<Post>> arg0) {

        mTimelineAdapter.setData(null);
	}

	@Override
	public void onRefreshStarted(View view) {
		
		getLoaderManager().destroyLoader(0);
        getLoaderManager().initLoader(0, null, this);
        mTextViewNoItems.setVisibility(View.INVISIBLE);
		mProgressBarLoading.setVisibility(View.VISIBLE);
		mListViewTimeline.setEmptyView(mProgressBarLoading);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		if (firstVisibleItem + visibleItemCount >= totalItemCount && visibleItemCount != 0) {
			if (mTimelineLoader.loadMore() && !mTimelineLoader.loading()) {
				mTimelineLoader.onContentChanged();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
}
