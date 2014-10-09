package com.codepath.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapter.TwitterDirectMessageAdapter;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DirectMessageActivity extends Activity {
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aMessages;
	private ListView lvUsers;
	private TwitterClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direct_message);
		tweets = new ArrayList<Tweet>();
		aMessages = new TwitterDirectMessageAdapter(this, tweets);

		client = TwitterApplication.getRestClient();

		lvUsers = (ListView) findViewById(R.id.lvDirectMessages);
		lvUsers.setAdapter(aMessages);

		loadDirectMessages();
	}

	private void loadDirectMessages() {
		client.getdirectMessages(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				aMessages.addAll(Tweet.fromJSON(json));
				aMessages.notifyDataSetChanged();
			}
		});
	}
}
