package com.codepath.apps.restclienttemplate.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable{
	
	private static final long serialVersionUID = -605190968411465088L;
	private String name;
	private long uid;
	private String screenName;
	private String profileImageUrl;
	
	public static User fromJSON(JSONObject json) {
		// TODO Auto-generated method stub
		User user = new User();
		try {
			user.name = json.getString("name");
			//System.out.println("@@@@@" + user.name);
			user.uid = json.getLong("id");
			user.screenName = json.getString("screen_name");
			user.profileImageUrl = json.getString("profile_image_url");
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
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return user;
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
	}


