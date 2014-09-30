package com.codepath.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapter.TwitterArrayAdapter;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.basictwitter.fragment.ComposeTweetFragment;
import com.codepath.apps.basictwitter.fragment.ComposeTweetFragment.ComposeTweetFragmentListener;
import com.codepath.apps.basictwitter.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity implements
		ComposeTweetFragmentListener {

	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	private String max_id = "0";
	private User user;
	private String tweetMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		getUserCredntials();

		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TwitterArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);

		if (!isNetworkAvailable()) {
			Toast.makeText(this, getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}


		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				System.out.println("######" + totalItemsCount);
				
				populateTimeline();
			}
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				aTweets.clear();
				max_id ="0";
				fetchTimelineAsync();
			}

		});
		
	}

	private void fetchTimelineAsync() {
		// TODO Auto-generated method stub
		//tweets.clear();
		populateTimeline();
		lvTweets.onRefreshComplete();
	}
	
	private void populateTimeline() {
		// TODO Auto-generated method stub
		client.getHomeTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJSON(json));
				max_id = Long.toString(tweets.get(tweets.size() - 1).getUid());
				System.out.println("&&&&&&&MAX ID "
						+ tweets.get(tweets.size() - 1).getUid());

				aTweets.notifyDataSetChanged();
				Log.d("DEBUG", json.toString());
			};

			@Override
			public void onFailure(Throwable e, JSONObject s) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),"JSON failed",
						Toast.LENGTH_SHORT).show();
				Log.d("DEBUG", e.toString());
				Log.d("DEBUG", s.toString());
			}
		}, max_id);
	}

	public void getUserCredntials() {

		client.getUserCredentials(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonObj) {
				// TODO Auto-generated method stub
				user = User.fromJSONToUser(jsonObj);
				Log.d("DEBUG", jsonObj.toString());
			}

			@Override
			public void onFailure(Throwable e, JSONObject js) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Failed",
						Toast.LENGTH_SHORT).show();
				Log.d("DEBUG", e.toString());
				Log.d("DEBUG", js.toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.compose_tweet, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.compose_tweet:
			FragmentManager frag = getSupportFragmentManager();
			ComposeTweetFragment diag = ComposeTweetFragment.newInstance(
					"Compose Tweets", user);
			diag.show(frag, "compose_tweet");
			return true;
		}
		return false;
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	@Override
	public void onFinishedComposingStatus(String status) {
		// TODO Auto-generated method stub
		tweetMsg = status;
		System.out.println("STATUS " +tweetMsg);
		postStatusMessage(tweetMsg);
	}

	private void postStatusMessage(String tweetMsg) {
		// TODO Auto-generated method stub
		client.postStatusUpdates(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				//populateTimeline();
				 aTweets.clear();
                 max_id = "0";
                 //populateTimeline();
				System.out.println("SUCCESS!!!!!!!");
				Log.d("DEBUG", arg0);
			}
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.d("DEBUG", arg1);
			}
		}, tweetMsg);
	}
}
