package com.codepath.apps.restclienttemplate.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable{
	
	private static final long serialVersionUID = -5733801136443403137L;
	
	@Column(name ="body")
	private String body;
	@Column(name ="uid")
	private long uid;
	@Column(name ="createdAt")
	private String createdAt;
	@Column(name ="user")
	private User user;
	@Column(name = "mediaUrl")
	private String mediaUrl;
	@Column(name = "retweetCount")
	private int retweetCount;
	@Column(name = "favCount")
	private int favCount;
	@Column(name = "retweeted")
	private boolean retweeted;
	@Column(name = "favorited")
	private boolean favorited;
	
	
	public boolean isRetweeted() {
		return retweeted;
	}

	public boolean getFavorited() {
		return favorited;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public int getFavCount() {
		return favCount;
	}

	public static Tweet getTweet() {
		return tweet;
	}

	static Tweet tweet;
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public static ArrayList<Tweet> fromJSON(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (JSONException ex) {
				ex.printStackTrace();
				continue;
			}

			tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		
		for(int i=0; i<tweets.size();i++){
			//System.out.println("@@@tweets" + tweets.get(i).getBody());
		}
		return tweets;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		JSONObject entitiesJson;
		JSONArray mediaJson;
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
			entitiesJson = jsonObject.getJSONObject("entities");
			tweet.retweetCount = jsonObject.getInt("retweet_count");
			tweet.favCount = jsonObject.getInt("favorite_count");
			System.out.println("*********"+ tweet.body+ "+++++"+ tweet.retweetCount + "&&&" + tweet.favCount);
			
			tweet.retweeted = jsonObject.getBoolean("retweeted");
			tweet.favorited = jsonObject.getBoolean("favorited");
			
			if(entitiesJson != JSONObject.NULL){
				mediaJson = entitiesJson.getJSONArray("media");
				tweet.mediaUrl = mediaJson.getJSONObject(0).getString("media_url");
				System.out.println("MEDIAAAAA*****" + tweet.mediaUrl);
			}
		} catch (JSONException ex) {
			//ex.printStackTrace();
		}
		return tweet;
	}
	
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(getCreatedAt()).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}	 
		String[] relativeTime = relativeDate.split(" ");
		String time = relativeTime[0] + relativeTime[1].charAt(0);
		return time;
	}
	
}
