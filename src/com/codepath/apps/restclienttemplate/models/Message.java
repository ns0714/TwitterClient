package com.codepath.apps.restclienttemplate.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Message {
	private String id;
	private Tweet tweet;
	private String senderName;
	private String senderScreenName;
	private String profileImage;
	private static Message msg;
	//profile_image_url
	private String created_at;
	public String getId() {
		return id;
	}
	public Tweet getTweet() {
		return tweet;
	}
	public String getSenderName() {
		return senderName;
	}
	public String getSenderScreenName() {
		return senderScreenName;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public String getCreated_at() {
		return created_at;
	}
	
	public static ArrayList<Message> fromJSON(JSONArray jsonArray) {
		ArrayList<Message> messages = new ArrayList<Message>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject msgJson = null;
			try {
				msgJson = jsonArray.getJSONObject(i);
			} catch (JSONException ex) {
				ex.printStackTrace();
				continue;
			}

			//msg = Message.fromJSON(msgJson);
			if (msg != null) {
				messages.add(msg);
			}
		}
		return messages;
	}
}
