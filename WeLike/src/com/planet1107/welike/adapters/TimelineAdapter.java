package com.planet1107.welike.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.planet1107.welike.R;
import com.planet1107.welike.connect.Post;
import com.planet1107.welike.views.PostListItem;

public class TimelineAdapter extends ArrayAdapter<Post> {
    
	private final LayoutInflater mInflater;

    public TimelineAdapter(Context context) {
    	
        super(context, R.layout.list_item_post);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Post> data) {
        
    	clear();
        if (data != null) {
            addAll(data);
            notifyDataSetChanged();
        }
    }
    
    @Override 
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        Post post = getItem(position);
        PostListItem view;
        if (convertView == null) {
        	view =  (PostListItem) mInflater.inflate(R.layout.list_item_post, parent, false);
        } else {
        	view = (PostListItem) convertView;
        }
        view.setPost(post);
        return view;
    }
}