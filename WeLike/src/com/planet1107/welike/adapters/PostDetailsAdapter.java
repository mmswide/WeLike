package com.planet1107.welike.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.planet1107.welike.R;
import com.planet1107.welike.connect.Comment;
import com.planet1107.welike.connect.Post;
import com.planet1107.welike.views.CommentListItem;
import com.planet1107.welike.views.PostListItem;

public class PostDetailsAdapter extends ArrayAdapter<Comment> {
    
	private final LayoutInflater mInflater;
	Post mPost;
    PostListItem postListItem;

    public PostDetailsAdapter(Context context, Post post) {
    	
        super(context, R.layout.list_item_comment);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPost = post;
    }

    public void setData(List<Comment> data) {
        
    	clear();
        if (data != null) {
            addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override 
    public View getView(final int position, View convertView, ViewGroup parent) {    
        
    	Comment comment = getItem(position);
        View view;
        if (position == 0) {
        	if (postListItem == null) {
        		postListItem =  (PostListItem) mInflater.inflate(R.layout.list_item_post, parent, false);
            	view = postListItem;
        	} else {
        		view = postListItem;
        	}
            postListItem.setPost(mPost);
        } else {
        	if (convertView == null || convertView instanceof PostListItem) {
            	view =  mInflater.inflate(R.layout.list_item_comment, parent, false);
            } else {
            	view = convertView;
            }
            if (view instanceof CommentListItem) {
            	((CommentListItem) view).setComment(comment);
            }
        }
        return view;
    }
}