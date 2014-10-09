package com.codepath.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapter.TwitterDirectMessageAdapter;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DirectMessageActivity extends Activity {
	private ArrayList<User> users;
	private ArrayAdapter<User> aUsers;
	private ListView lvUsers;
	private TwitterClient client;
	private static String userName;
	private static String listUsers;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direct_message);
		users = new ArrayList<User>();
		aUsers = new TwitterDirectMessageAdapter(this, users);

		client = TwitterApplication.getRestClient();

		lvUsers = (ListView) findViewById(R.id.lvDirectMessages);
		lvUsers.setAdapter(aUsers);

		loadDirectMessages();
		

	}
	private void loadDirectMessages() {
		// System.out.println("HELLLLOOOOOOO");
		client.getdirectMessages(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				Log.d("DEBUG", json.toString());
			}
		});
}
}
