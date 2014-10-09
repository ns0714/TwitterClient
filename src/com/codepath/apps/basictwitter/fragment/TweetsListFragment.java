package com.codepath.apps.basictwitter.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapter.TwitterArrayAdapter;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment {
	public ArrayList<Tweet> tweets;
	public ArrayAdapter<Tweet> aTweets;
	public PullToRefreshListView lvTweets;

	public long max_id = 0;
	public long since_id = 1;
	public TwitterClient client;
	public User user;
	public String tweetMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		aTweets = new TwitterArrayAdapter(getActivity(), tweets);

		client = TwitterApplication.getRestClient();

		//getUserCredntials();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, container,
				false);
		lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		return view;
	}
	
	public void showProgressBar() {
        getActivity().setProgressBarIndeterminateVisibility(true); 
    }
	
	public void hideProgressBar() {
    	getActivity().setProgressBarIndeterminateVisibility(false); 
    }
}
