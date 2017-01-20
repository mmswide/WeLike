package com.planet1107.welike.connect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

public class Connect {

	private static Connect mSharedInstance = null;
	DefaultHttpClient mHttpClient;
	User mCurrentUser;
	Context mContext;
	
	public Connect(Context context) {
		
		mHttpClient = new DefaultHttpClient();
		mContext = context;
		User oldUser = User.loadFromDisk(context);
		if (oldUser != null) {
			mCurrentUser = oldUser;
		}
	}
	
	public static Connect getInstance(Context context) {
		
		if (mSharedInstance == null) {
			mSharedInstance = new Connect(context.getApplicationContext());
		}
		return mSharedInstance;
	}
	
	public User loginUser(String username, String password) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("username", username));
		parameters.add(new BasicNameValuePair("password", password));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/login", parameters);
		try {
	        if (jsonObject.has("item") && !jsonObject.getJSONObject("item").isNull("userID")) {
	        	User user = new User(jsonObject.getJSONObject("item"));
	        	user.saveOnDisk(mContext);
	        	mCurrentUser = user;
	        	return user;
	        } else {
	        	return null;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User registerUser(String username, String password, String email, Bitmap userAvatar, int userType, String fullName, String info, double latitude, double longitude, String companyAddress, String companyPhone, String companyWeb) {
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		userAvatar.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
		mpEntity.addBinaryBody("userAvatar", byteArray, ContentType.DEFAULT_BINARY, "userAvatar.jpg");
		mpEntity.addTextBody("username", username);
		mpEntity.addTextBody("password", password);
		mpEntity.addTextBody("email", email);
		mpEntity.addTextBody("userFullname", fullName);
		mpEntity.addTextBody("userTypeID", String.valueOf(userType));
		mpEntity.addTextBody("userInfo", info != null ? info : "");
		mpEntity.addTextBody("userLat", String.valueOf(latitude));
		mpEntity.addTextBody("userLong", String.valueOf(longitude));
		mpEntity.addTextBody("userAddress", companyAddress != null ? companyAddress : "");
		mpEntity.addTextBody("userPhone", companyPhone != null ? companyPhone : "");
		mpEntity.addTextBody("userWeb", companyWeb != null ? companyWeb : "");

		
		HttpPost httpPost = new HttpPost("http://planet1107-solutions.net/wli/api/register");
		httpPost.addHeader("api_key", "!#wli!sdWQDScxzczFžŽYewQsq_?wdX09612627364[3072∑34260-#");
		HttpResponse response = null;
		try {
			httpPost.setEntity(mpEntity.build());
			response = mHttpClient.execute(httpPost);
		    HttpEntity entity = response.getEntity();
	        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
	        if (jsonObject != null && jsonObject.has("item")) {
	        	try {
	        		User user = new User(jsonObject.getJSONObject("item"));
		        	user.saveOnDisk(mContext);
		        	mCurrentUser = user;
		        	return user;
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User updateUser(int userID, String email, Bitmap userAvatar, int userType, String fullName, String info, double latitude, double longitude, String companyAddress, String companyPhone, String companyWeb) {
		
		MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
		if (userAvatar != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			userAvatar.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			mpEntity.addBinaryBody("userAvatar", byteArray, ContentType.DEFAULT_BINARY, "userAvatar.jpg");
		}
		
		mpEntity.addTextBody("userID", String.valueOf(userID));
		if (email != null && email.length() > 0) {
			mpEntity.addTextBody("email", email);
		}
		if (fullName != null && fullName.length() > 0) {
			mpEntity.addTextBody("userFullname", fullName);
		}
		if (info != null && info.length() > 0) {
			mpEntity.addTextBody("userInfo", info != null ? info : "");
		}
		if (companyAddress != null && companyAddress.length() > 0) {
			mpEntity.addTextBody("userAddress", companyAddress != null ? companyAddress : "");
		}
		if (companyPhone != null && companyPhone.length() > 0) {
			mpEntity.addTextBody("userPhone", companyPhone != null ? companyPhone : "");
		}
		if (companyWeb != null && companyWeb.length() > 0) {
			mpEntity.addTextBody("userWeb", companyWeb != null ? companyWeb : "");
		}
		mpEntity.addTextBody("userTypeID", String.valueOf(userType));
		mpEntity.addTextBody("userLat", String.valueOf(latitude));
		mpEntity.addTextBody("userLong", String.valueOf(longitude));

		
		HttpPost httpPost = new HttpPost("http://planet1107-solutions.net/wli/api/setProfile");
		httpPost.addHeader("api_key", "!#wli!sdWQDScxzczFžŽYewQsq_?wdX09612627364[3072∑34260-#");
		HttpResponse response = null;
		try {
			httpPost.setEntity(mpEntity.build());
			response = mHttpClient.execute(httpPost);
		    HttpEntity entity = response.getEntity();
	        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
	        if (jsonObject != null && jsonObject.has("item")) {
	        	try {
	        		User user = new User(jsonObject.getJSONObject("item"));
		        	user.saveOnDisk(mContext);
		        	mCurrentUser = user;
		        	return user;
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUser(int userID) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("forUserID", String.valueOf(userID)));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getProfile", parameters);
		try {
	        if (jsonObject.has("item")) {
	        	User user = new User(jsonObject.getJSONObject("item"));
	        	return user;
	        } else {
	        	return null;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Post> getTimeline(int userID, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("forUserID", String.valueOf(userID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<Post> posts = new ArrayList<Post>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getTimeline", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		Post post = new Post(jsonArray.getJSONObject(i));
	        		posts.add(post);
	        	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return posts;
	}
	
	public ArrayList<Post> getPopular(int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<Post> posts = new ArrayList<Post>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getPopularPosts", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		Post post = new Post(jsonArray.getJSONObject(i));
	        		posts.add(post);
	        	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return posts;
	}
	
	public ArrayList<Post> getRecent(int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<Post> posts = new ArrayList<Post>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getRecentPosts", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		Post post = new Post(jsonArray.getJSONObject(i));
	        		posts.add(post);
	        	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return posts;
	}

	// Comments
	
	public ArrayList<Comment> getComments(int postID, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("postID", String.valueOf(postID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<Comment> comments = new ArrayList<Comment>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getComments", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		Comment comment = new Comment(jsonArray.getJSONObject(i));
	        		comments.add(comment);
	        	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return comments;
	}
	
	public Comment sendComment(int postID, String commentText) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("postID", String.valueOf(postID)));
		parameters.add(new BasicNameValuePair("commentText", commentText));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/setComment", parameters);
		if (jsonObject != null && jsonObject.has("item")) {
        	return new Comment(BaseObject.getJSONFromJSONObject(jsonObject, "item"));
        } else {
        	return null;
        }
	}

	public boolean removeComment(int commentID) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("commentID", String.valueOf(commentID)));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/removeComment", parameters);
        if (jsonObject != null && jsonObject.has("status") && BaseObject.getStringFromJSONObject(jsonObject, "status").equals("OK")) {
        	return true;
        } else {
        	return false;
        }
	}
	
	//Likes
	
	public ArrayList<Like> getLikes(int postID, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("postID", String.valueOf(postID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<Like> likes = new ArrayList<Like>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getLikes", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		Like like = new Like(jsonArray.getJSONObject(i));
	        		likes.add(like);
	        	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return likes;
	}
	
	public boolean likePost(int postID) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("postID", String.valueOf(postID)));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/setLike", parameters);
		if (jsonObject != null && jsonObject.has("item")) {
        	return true;
        } else {
        	return false;
        }
	}
	
	public boolean unlikePost(int postID) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("postID", String.valueOf(postID)));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/removeLike", parameters);
		if (jsonObject != null && jsonObject.has("status") && BaseObject.getStringFromJSONObject(jsonObject, "status").equals("OK")) {
        	return true;
        } else {
        	return false;
        }
	}
	
	// Follows
	
	public ArrayList<User> getFollowers(int userID, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(userID)));
		parameters.add(new BasicNameValuePair("forUserID", String.valueOf(userID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<User> users = new ArrayList<User>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getFollowers", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		User user = new User(jsonArray.getJSONObject(i).getJSONObject("user"));
	        		users.add(user);
	        	}
	        	return users;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return users;
	}
	
	public ArrayList<User> getFollowing(int userID, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(userID)));
		parameters.add(new BasicNameValuePair("forUserID", String.valueOf(userID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<User> users = new ArrayList<User>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getFollowing", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		User user = new User(jsonArray.getJSONObject(i).getJSONObject("user"));
	        		users.add(user);
	        	}
	        	return users;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return users;
	}

	public boolean followUser(int userID) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("followingID", String.valueOf(userID)));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/setFollow", parameters);
		if (jsonObject != null && jsonObject.has("item")) {
        	return true;
        } else {
        	return false;
        }
	}
	
	public boolean unfollowUser(int followID) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("followingID", String.valueOf(followID)));
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/removeFollow", parameters);
		if (jsonObject != null && jsonObject.has("status") && BaseObject.getStringFromJSONObject(jsonObject, "status").equals("OK")) {
        	return true;
        } else {
        	return false;
        }
	}
	
	
	// Search
	
	public ArrayList<User> getUsersForString(String query, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		parameters.add(new BasicNameValuePair("searchTerm", query));
		ArrayList<User> users = new ArrayList<User>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/findUsers", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		User user = new User(jsonArray.getJSONObject(i));
	        		users.add(user);
	        	}
	        	return users;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return users;
	}

	public JSONObject getJSONObject(String url, List<NameValuePair> parameters) {
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("api_key", "!#wli!sdWQDScxzczFžŽYewQsq_?wdX09612627364[3072∑34260-#");
		HttpResponse response = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
			response = mHttpClient.execute(httpPost);
		    HttpEntity entity = response.getEntity();
	        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
		    return jsonObject;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public ArrayList<User> getUsersAround(double latitude, double longitude, double distance, int page, int pageSize) {
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userID", String.valueOf(mCurrentUser.userID)));
		parameters.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		parameters.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
		parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
		parameters.add(new BasicNameValuePair("take", String.valueOf(pageSize)));
		ArrayList<User> users = new ArrayList<User>();
        JSONObject jsonObject = this.getJSONObject("http://planet1107-solutions.net/wli/api/getLocationsForLatLong", parameters);
		if (jsonObject != null && jsonObject.has("items")) {
        	JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("items");
	        	for (int i = 0; i < jsonArray.length(); i++) {
	        		User user = new User(jsonArray.getJSONObject(i));
	        		users.add(user);
	        	}
	        	return users;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return users;
	}

	public Post sendPost(String postTitle, Bitmap postImage) {
        
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		postImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
		mpEntity.addBinaryBody("postImage", byteArray, ContentType.DEFAULT_BINARY, "image.jpg");
		mpEntity.addTextBody("postTitle", postTitle);
		mpEntity.addTextBody("userID", String.valueOf(mCurrentUser.userID));

		
		HttpPost httpPost = new HttpPost("http://planet1107-solutions.net/wli/api/sendPost");
		httpPost.addHeader("api_key", "!#wli!sdWQDScxzczFžŽYewQsq_?wdX09612627364[3072∑34260-#");
		HttpResponse response = null;
		try {
			httpPost.setEntity(mpEntity.build());
			response = mHttpClient.execute(httpPost);
		    HttpEntity entity = response.getEntity();
	        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
	        if (jsonObject != null && jsonObject.has("item")) {
	        	try {
					return new Post(jsonObject.getJSONObject("item"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public User getCurrentUser() {
		
		return mCurrentUser;
	}
}
