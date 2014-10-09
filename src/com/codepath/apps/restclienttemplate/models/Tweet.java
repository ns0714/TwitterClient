package com.codepath.apps.restclienttemplate.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ClipData.Item;
import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

	private static final long serialVersionUID = -5733801136443403137L;

	@Column(name = "body")
	private String body;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "createdAt")
	private String createdAt;
	@Column(name = "user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
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
	private long id_str;
	private boolean is_retweeted;
	private User sender;

	public long isId_str() {
		return id_str;
	}

	static Tweet tweet;

	public Tweet() {
		super();
	}

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
		return tweets;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();

		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = getUser(jsonObject);
			// tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
			/*
			 * if (jsonObject.getJSONObject("sender") != JSONObject.NULL) {
			 * tweet.user = User.fromJSON(jsonObject.getJSONObject("sender")); }
			 */
			// entitiesJson = jsonObject.getJSONObject("entities");
			tweet.mediaUrl = getMediaUrl(jsonObject);
			tweet.retweetCount = jsonObject.getInt("retweet_count");
			tweet.favCount = jsonObject.getInt("favorite_count");

			tweet.retweeted = jsonObject.getBoolean("retweeted");
			tweet.favorited = jsonObject.getBoolean("favorited");
			tweet.id_str = jsonObject.getLong("id_str");
			tweet.is_retweeted = getRetweetedStatus(jsonObject);
		} catch (JSONException ex) {
			System.out.println("User not found");
			ex.printStackTrace();
		}
		return tweet;
	}

	public static User getUser(JSONObject json) {
		JSONObject userJson;
		User user = null;
		try {
			userJson = json.getJSONObject("user");
			if (userJson != JSONObject.NULL) {
				user = User.fromJSON(userJson);
			}
		} catch (JSONException ex) {
			try {
				userJson = json.getJSONObject("sender");
				if (userJson != JSONObject.NULL) {
					user = User.fromJSON(userJson);
				}
			} catch (JSONException e) {
				user = null;
			}
		}
		return user;
	}

	public static boolean getRetweetedStatus(JSONObject json) {
		JSONObject retweetedJson;
		boolean isRetweeted = false;
		try {
			retweetedJson = json.getJSONObject("retweeted_status");
			if (retweetedJson != JSONObject.NULL) {
				isRetweeted = true;
			}
		} catch (JSONException ex) {
			isRetweeted = false;
		}
		return isRetweeted;
	}

	public static String getMediaUrl(JSONObject mediaJson) {
		JSONObject entitiesJson;
		String mediaUurl = null;
		JSONArray mediaJsonArray;
		try {
			entitiesJson = mediaJson.getJSONObject("entities");
			if (entitiesJson != JSONObject.NULL) {
				mediaJsonArray = entitiesJson.getJSONArray("media");
				mediaUurl = mediaJsonArray.getJSONObject(0).getString(
						"media_url");
			}
		} catch (JSONException ex) {
			mediaUurl = null;
		}
		return mediaUurl;
	}

	public User getSender() {
		return sender;
	}

	public boolean isIs_retweeted() {
		return is_retweeted;
	}

	public long getId_str() {
		return id_str;
	}

	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		String time = "";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat,
				Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(getCreatedAt()).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
					.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String[] relativeTime = relativeDate.split(" ");
		// System.out.println("@@@@" + relativeDate);
		if (relativeTime.length > 1) {
			time = relativeTime[0] + relativeTime[1].charAt(0);
		} else {
			time = relativeTime[0];
		}
		return time;
	}

	public String getFormatedTime(long createdTime) {
		String formattedTime = "";
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(createdTime);
		Date creationTime = instance.getTime();

		long diffInMs = (new Date(System.currentTimeMillis()).getTime() / 1000)
				- creationTime.getTime();
		long diffInHours = TimeUnit.SECONDS.toHours(diffInMs);
		long diffInMins = TimeUnit.SECONDS.toMinutes(diffInMs);
		long diffInDays = TimeUnit.SECONDS.toDays(diffInMins);

		if (diffInMins < 60) {
			formattedTime = diffInMins + "m";
		} else if (diffInHours >= 1 && diffInHours < 24) {
			formattedTime = diffInHours + "h";
		} else if (diffInDays >= 1) {
			formattedTime = diffInDays + "d";
		}

		return formattedTime;
	}

	public static List<Tweet> getAll() {
		// This is how you execute a query
		return new Select().from(Tweet.class).execute();
	}

	public static void deleteAll() {
		new Delete().from(Tweet.class).execute();
	}
}
