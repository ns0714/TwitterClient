package com.codepath.apps.basictwitter.client;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change
																				// this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change
																			// this,
																			// base
																			// API
																			// URL
	public static final String REST_CONSUMER_KEY = "jOYtWoz1eeyJjqMVg2Vb7Qaw2"; // Change
																				// this
	public static final String REST_CONSUMER_SECRET = "Qu4YYu6NGtHuK2Orn5eCCLck9JQCBIHXnaVem0gUxAnbvTqoTq"; // Change
																											// this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change
																			// this
																			// (here
																			// and
																			// in
																			// manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler,
			long since_id, long max_id) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("since_id", Long.toString(since_id));
		if (max_id != 0) {
			params.put("max_id", Long.toString(max_id - 1));
		}
		client.get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(AsyncHttpResponseHandler handler,
			long since_id, long max_id) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("since_id", Long.toString(since_id));
		if (max_id != 0) {
			params.put("max_id", Long.toString(max_id - 1));
		}
		client.get(apiUrl, params, handler);
	}

	public void getUserCredentials(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

	public void getMyFollowers(AsyncHttpResponseHandler handler, String screen_name) {
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}
	
	public void getMyFollowing(AsyncHttpResponseHandler handler, String screen_name) {
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}
	
	public void getUserTimeline(AsyncHttpResponseHandler handler,
			String screen_name) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}

	public void getFullStatus(AsyncHttpResponseHandler handler, String id) {
		String apiUrl = getApiUrl("statuses/show.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		client.post(apiUrl, params, handler);
	}

	public void getSearchresults(AsyncHttpResponseHandler handler, String query) {
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		params.put("q", query);
		client.get(apiUrl, params, handler);
	}
	
	public void getdirectMessages(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("direct_messages.json");
		client.get(apiUrl, null, handler);
	}
	
	public void postStatusUpdates(AsyncHttpResponseHandler handler, String tweet) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		client.post(apiUrl, params, handler);
	}

	public void postReplyToStatus(AsyncHttpResponseHandler handler,
			String tweet, String status_id) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		params.put("in_reply_to_status_id", status_id);
		client.post(apiUrl, params, handler);
	}
	
	public void postFavoriteToStatus(AsyncHttpResponseHandler handler, String status_id) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", status_id);
		client.post(apiUrl, params, handler);
	}
	
	public void postUnFavoriteToStatus(AsyncHttpResponseHandler handler, String status_id) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", status_id);
		client.post(apiUrl, params, handler);
	}
	
	public void postRetweetStatus(AsyncHttpResponseHandler handler, String status_id) {
		String apiUrl = getApiUrl("retweet/"+status_id+".json");
		client.post(apiUrl, null, handler);
	}
	
	public void postDirectMessage(AsyncHttpResponseHandler handler, String screen_name) {
		String apiUrl = getApiUrl("direct_messages/new.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screen_name);
		client.post(apiUrl, null, handler);
	}
}