package com.codepath.apps.basictwitter.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.basictwitter.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class SearchResultsTweetsFragment extends TweetsListFragment {
	static String searchQuery;

	public static SearchResultsTweetsFragment newInstance(String srchQuery) {
		SearchResultsTweetsFragment searchTimeLineFragment = new SearchResultsTweetsFragment();
		searchQuery = srchQuery;
		Bundle args = new Bundle();
		args.putString("search_query", srchQuery);
		searchTimeLineFragment.setArguments(args);
		return searchTimeLineFragment;
	}
	
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
				populateSearchTimeline();
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
		populateSearchTimeline();
		lvTweets.onRefreshComplete();
	}
	
	private void populateSearchTimeline() {
		if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
			showProgressBar();
			if (tweets.size() > 0) {
				max_id = tweets.get(tweets.size() - 1).getUid();
			}
			client.getSearchresults(new JsonHttpResponseHandler() {
				JSONArray jsonArray  = null;
				@Override
				public void onSuccess(JSONObject jsonObj) {
					try {
						jsonArray =jsonObj.getJSONArray("statuses");
						aTweets.addAll(Tweet.fromJSON(jsonArray));
						aTweets.notifyDataSetChanged();
						Log.d("DEBUG", jsonObj.toString());
						hideProgressBar();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};

				@Override
				public void onFailure(Throwable e, JSONObject s) {
					Toast.makeText(getActivity(), getResources().getString(R.string.failed),
					 Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", s.toString());
				}
			}, searchQuery);
		} else {
			 Toast.makeText(getActivity(),
			getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}		
	}
}
