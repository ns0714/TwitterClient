package com.codepath.apps.basictwitter.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	static String userScreenName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getUserCredntials();
		populateUserTimeline();
	}

	public static UserTimelineFragment newInstance(String screenName) {
		UserTimelineFragment userTimeLineFragment = new UserTimelineFragment();
		userScreenName = screenName;
		Bundle args = new Bundle();
		args.putString("screen_name", screenName);
		userTimeLineFragment.setArguments(args);
		return userTimeLineFragment;
	}

	private void populateUserTimeline() {
		if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
			showProgressBar();
			if (tweets.size() > 0) {
				max_id = tweets.get(tweets.size() - 1).getUid();
			}
			client.getUserTimeline(new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONArray json) {
					aTweets.addAll(Tweet.fromJSON(json));
					aTweets.notifyDataSetChanged();
					Log.d("DEBUG", json.toString());
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
			}, userScreenName);
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void getUserCredntials() {

		client.getUserCredentials(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonObj) {
				user = User.fromJSONToUser(jsonObj);
				Log.d("DEBUG", jsonObj.toString());
			}

			@Override
			public void onFailure(Throwable e, JSONObject js) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				Log.d("DEBUG", e.toString());
				Log.d("DEBUG", js.toString());
			}
		});
	}

}
