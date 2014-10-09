package com.codepath.apps.restclienttemplate.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends Model implements Serializable {

	private static final long serialVersionUID = -605190968411465088L;

	@Column(name = "name")
	private String name;
	@Column(name = "uid")
	private long uid;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	@Column(name = "followers")
	private int followers;
	@Column(name = "following")
	private int following;
	@Column(name = "totalTweets")
	private int totalTweets;
	@Column(name = "bannerUrl")
	private String bannerUrl;
	private String backgroundPic;
	private boolean isFollowing;

	public boolean isFollowing() {
		return isFollowing;
	}

	public String getBackgroundPic() {
		return backgroundPic;
	}

	private static User user;

	public User() {
		super();
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public int getTotalTweets() {
		return totalTweets;
	}

	private String tagLine;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getFollowers() {
		return followers;
	}

	public int getFollowing() {
		return following;
	}

	public String getTagLine() {
		return tagLine;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static ArrayList<User> fromJSON(JSONArray jsonArray) {
		ArrayList<User> users = new ArrayList<User>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject userJson = null;
			try {
				userJson = jsonArray.getJSONObject(i);
			} catch (JSONException ex) {
				ex.printStackTrace();
				continue;
			}

			user = User.fromJSON(userJson);
			if (user != null) {
				users.add(user);
			}
		}
		return users;
	}

	public static User fromJSON(JSONObject json) {
		User user = new User();
		try {
			user.name = json.getString("name");
			user.uid = json.getLong("id");
			user.screenName = json.getString("screen_name");
			user.profileImageUrl = json.getString("profile_image_url");
			user.followers = json.getInt("followers_count");
			user.following = json.getInt("friends_count");
			user.tagLine = json.getString("description");
			user.totalTweets = json.getInt("statuses_count");
			user.bannerUrl = json.getString("profile_banner_url");
			user.backgroundPic = json.getString("profile_background_image_url");
			user.isFollowing = json.getBoolean("following");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public static User fromJSONToUser(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.followers = jsonObject.getInt("followers_count");
			user.following = jsonObject.getInt("friends_count");
			user.tagLine = jsonObject.getString("description");
			user.totalTweets = jsonObject.getInt("statuses_count");
			user.isFollowing = jsonObject.getBoolean("following");
			if (jsonObject.get("profile_banner_url") != JSONObject.NULL) {
				user.bannerUrl = jsonObject.getString("profile_banner_url");
			}
			user.backgroundPic = jsonObject
					.getString("profile_background_image_url");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return user;
	}

	public static User getSenderInfo(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.followers = jsonObject.getInt("followers_count");
			user.following = jsonObject.getInt("friends_count");
			user.tagLine = jsonObject.getString("description");
			user.totalTweets = jsonObject.getInt("statuses_count");
			if (jsonObject.get("profile_banner_url") != JSONObject.NULL) {
				user.bannerUrl = jsonObject.getString("profile_banner_url");
			}
			user.backgroundPic = jsonObject
					.getString("profile_background_image_url");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return user;
	}
	public static List<User> getAll(User users) {
		// This is how you execute a query
		return new Select().from(User.class).execute();
	}
}
