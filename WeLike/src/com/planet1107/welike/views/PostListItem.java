package com.planet1107.welike.views;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.planet1107.welike.R;
import com.planet1107.welike.activities.CommentsActivity;
import com.planet1107.welike.activities.LikesActivity;
import com.planet1107.welike.activities.ProfileActivity;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.Post;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PostListItem extends RelativeLayout implements OnClickListener {

	public Post mPost;
	Connect sharedConnect;
	Button buttonLike;
	Button buttonLikes;
	Button buttonComment;
	
	public PostListItem(Context context) {
		
		super(context);
	}

	public PostListItem(Context context, AttributeSet attrs) {
	
		super(context, attrs);
	}

	public PostListItem(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
	}
	
	@Override
	public void onClick(final View button) {
		
		if (button == buttonLike) {
			if (!mPost.likedThisPost) {
				buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_liked, 0, 0, 0);
			} else {
				buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_like, 0, 0, 0);
			}
		} else if (button == buttonLikes) {
			Intent likesIntent = new Intent(PostListItem.this.getContext(), LikesActivity.class);
			likesIntent.putExtra("postID",mPost.postID);
			PostListItem.this.getContext().startActivity(likesIntent);
		} else if (button == buttonComment) {
			Intent commentIntent = new Intent(PostListItem.this.getContext(), CommentsActivity.class);
			commentIntent.putExtra("postID",mPost.postID);
			PostListItem.this.getContext().startActivity(commentIntent);
		} else {
			
		}
		
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				
				Connect sharedConnect = Connect.getInstance(getContext());
				if (button == buttonLike) {
					if (!mPost.likedThisPost) {
						buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_liked, 0, 0, 0);
						return sharedConnect.likePost(mPost.postID);
					} else {
						buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_like, 0, 0, 0);
						return sharedConnect.unlikePost(mPost.postID);
					}
				} else if (button == buttonLikes) {
					return true;
				} else if (button == buttonComment) {
					return true;
				} else {
					return null;
				}
			}
			
			protected void onPostExecute(Boolean result) {
		         
				if (button == buttonLike) {
					if (result && !mPost.likedThisPost) {
						buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_liked, 0, 0, 0);
						mPost.likedThisPost = true;
						mPost.postLikesCount++;
					} else if (result) {
						buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_like, 0, 0, 0);
						mPost.likedThisPost = false;
						mPost.postLikesCount--;
					}
					buttonLikes.setText( mPost.postLikesCount + " like");
				} else if (button == buttonLikes) {

				} else if (button == buttonComment) {

				} else {
					showUser();
				}
		     }
			
		}.execute();
	}
	
	public void setPost(Post post) {
		
		mPost = post;
		reloadView();
	}	

	protected void onFinishInflate () {
		
		buttonLike = (Button) findViewById(R.id.buttonLike);
		buttonLikes = (Button) findViewById(R.id.buttonLikes);
		buttonComment = (Button) findViewById(R.id.buttonComment);

		buttonLike.setOnClickListener(this);
		buttonLikes.setOnClickListener(this);
        buttonComment.setOnClickListener(this);
        
        TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        ImageView imageViewUserimage = (ImageView)findViewById(R.id.imageViewUserImage);
        textViewUsername.setClickable(true);
        imageViewUserimage.setClickable(true);
        textViewUsername.setOnClickListener(this);
        imageViewUserimage.setOnClickListener(this);
		sharedConnect = Connect.getInstance(getContext());
	}
	
	private void reloadView() {
		
		if (mPost.likedThisPost) {
			buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_liked, 0, 0, 0);
		} else {
			buttonLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_like, 0, 0, 0);
		}
		buttonLikes.setText(mPost.postLikesCount + " like");
		
        UrlImageViewHelper.setUrlDrawable(((ImageView)findViewById(R.id.imageViewUserImage)), mPost.postUser.userAvatarPath);
        UrlImageViewHelper.setUrlDrawable(((ImageView)findViewById(R.id.imageViewPostImage)), mPost.postImagePath);
        ((TextView)findViewById(R.id.textViewPostText)).setText(mPost.postTitle);
        ((TextView)findViewById(R.id.textViewUsername)).setText(mPost.postUser.userFullName);
        ((TextView)findViewById(R.id.textViewTimeAgo)).setText(mPost.timeAgo);
	}
	
	public void showUser() {
		
		Intent intentUser = new Intent(getContext(), ProfileActivity.class);
		intentUser.putExtra("userID", mPost.postUser.userID);
		getContext().startActivity(intentUser);
	}
}
