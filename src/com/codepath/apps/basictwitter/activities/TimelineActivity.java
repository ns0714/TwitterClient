package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.basictwitter.fragment.ComposeTweetFragment;
import com.codepath.apps.basictwitter.fragment.ComposeTweetFragment.ComposeTweetFragmentListener;
import com.codepath.apps.basictwitter.fragment.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragment.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.basictwitter.listener.FragmentTabListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements
		ComposeTweetFragmentListener {

	private User user;
	private TwitterClient client;
	private String tweetMsg;
	private HomeTimelineFragment homefrag;
	private SearchView searchView;
	private String srchQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		getUserCredntials();
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

		Tab home = actionBar.newTab()
				.setText("Home")
				// .setIcon(R.drawable.ic_launcher)
				.setTag("HomeTimelineFragment")
				.setTabListener(
						new FragmentTabListener<HomeTimelineFragment>(
								R.id.flContainer, this, "home",
								HomeTimelineFragment.class));
		actionBar.addTab(home);
		actionBar.selectTab(home);

		Tab mention = actionBar.newTab()
				.setText("Mentions")
				// .setIcon(R.drawable.ic_launcher)
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new FragmentTabListener<MentionsTimelineFragment>(
								R.id.flContainer, this, "mentions",
								MentionsTimelineFragment.class));

		actionBar.addTab(mention);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.compose_tweet, menu);
		inflater.inflate(R.menu.profile, menu);
		inflater.inflate(R.menu.message, menu);
		inflater.inflate(R.menu.search, menu);
		MenuItem searchItem = menu.findItem(R.id.search);

		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// perform query here
				srchQuery = query.trim();

				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
				newImageSearch();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	public void newImageSearch() {
		Intent i = new Intent(this, SearchActivity.class);
		i.putExtra("query", srchQuery);
		startActivity(i);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.compose_tweet:
			FragmentManager frag = getSupportFragmentManager();
			ComposeTweetFragment diag = ComposeTweetFragment.newInstance(
					"Compose Tweets", user, "");
			diag.show(frag, "compose_tweet");

			return true;
		}
		return false;
	}

	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user", user);
		startActivity(i);
	}
	
	public void getDirectMessages(MenuItem mi) {
		Intent i = new Intent(this, DirectMessageActivity.class);
		i.putExtra("user", user);
		startActivity(i);
	}
	

	public void getUserCredntials() {

		if (NetworkCheckHelper.isNetworkAvailable(this)) {

			client.getUserCredentials(new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONObject jsonObj) {
					user = User.fromJSONToUser(jsonObj);
					Log.d("DEBUG", jsonObj.toString());
				}

				@Override
				public void onFailure(Throwable e, JSONObject js) {
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", js.toString());
				}
			});
		} else {
			Toast.makeText(this, getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onFinishedComposingStatus(String status) {
		tweetMsg = status;
		postStatusMessage(tweetMsg);
	}

	private void postStatusMessage(String tweetMsg) {

		if (NetworkCheckHelper.isNetworkAvailable(this)) {
			showProgressBar();
			client.postStatusUpdates(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					Tweet tweet = Tweet.fromJSON(json);
					initFragment();
					homefrag.insert(tweet, 0);
					Log.d("DEBUG", json.toString());
					hideProgressBar();
				}

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					Log.d("DEBUG", arg1);
				}
			}, tweetMsg);
		} else {
			Toast.makeText(this, getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 50) {
			if (resultCode == RESULT_OK) {
				Tweet replyTweet = (Tweet) data.getSerializableExtra("tweet");
				Toast.makeText(this, Tweet.getTweet().getBody(),
						Toast.LENGTH_SHORT).show();
				initFragment();
				homefrag.insert(replyTweet, 0);
			}
		}
	}

	protected void initFragment() {
		homefrag = (HomeTimelineFragment) getSupportFragmentManager()
				.findFragmentById(R.id.flContainer);
	}
	
	public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true); 
    }
	
	public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false); 
    }
}
