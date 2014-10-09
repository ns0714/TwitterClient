package com.codepath.apps.basictwitter.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.basictwitter.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, container,
				false);
		lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		aTweets.clear();
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateHomeTimeline();
			}
		});

		lvTweets.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				tweets.clear();
				fetchTimelineAsync();
			}
		});

		return view;
	}

	private void fetchTimelineAsync() {
		max_id = 0;
		populateHomeTimeline();
		lvTweets.onRefreshComplete();
	}

	private void populateHomeTimeline() {
		if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
			Tweet.deleteAll();
			showProgressBar();
			if (tweets.size() > 0) {
				max_id = tweets.get(tweets.size() - 1).getUid();
			}
			client.getHomeTimeline(new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONArray json) {
					aTweets.addAll(Tweet.fromJSON(json));
					aTweets.notifyDataSetChanged();
					Log.d("DEBUG", json.toString());
					for (Tweet tweet : tweets) {
						tweet.getUser().save();
						tweet.save();
					}
					hideProgressBar();
				};

				@Override
				public void onFailure(Throwable e, JSONObject s) {
					Toast.makeText(getActivity(),
							getResources().getString(R.string.failed),
							Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", s.toString());
				}
			}, since_id, max_id);
		} else {
			aTweets.addAll(Tweet.getAll());
			for (Tweet t : Tweet.getAll()) {
				System.out.println("^^^" + t.getBody());
				System.out.println("^^^" + t.getUser().getProfileImageUrl());
			}
			// Toast.makeText(getActivity(),getResources().getString(R.string.no_network),Toast.LENGTH_SHORT).show();
		}
	}

	public void insert(Tweet tweet, int location) {
		aTweets.insert(tweet, location);
	}
}
