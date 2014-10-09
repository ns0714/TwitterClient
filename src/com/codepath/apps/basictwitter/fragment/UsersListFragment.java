package com.codepath.apps.basictwitter.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.adapter.TwitterFollowersAdapter;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UsersListFragment extends Fragment{
	private ArrayList<User> users;
	private ArrayAdapter<User> aUsers;
	private ListView lvUsers;
	private TwitterClient client;
	private static String userName;
	private static String listUsers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		users = new ArrayList<User>();
		aUsers = new TwitterFollowersAdapter(getActivity(), users);

		client = TwitterApplication.getRestClient();
	}
	
	public static UsersListFragment newInstance(String screen, String screenName) {
		UsersListFragment userListFragment = new UsersListFragment();
		userName = screenName;
		listUsers = screen;
		Bundle args = new Bundle();
		args.putString("screen_name", screenName);
		userListFragment.setArguments(args);
		return userListFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_user_list, container,
				false);
		lvUsers = (ListView) view.findViewById(R.id.lvFollowers);
		lvUsers.setAdapter(aUsers);
		
		if(listUsers.equals("following")){
			populateFollowingInfo();
		}else if(listUsers.equals("followers")){
			populateFollowersInfo();
		}
		
		
		return view;
	}
	
	private void populateFollowersInfo() {
		if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
			//showProgressBar();
			client.getMyFollowers(new JsonHttpResponseHandler() {
				JSONArray json = null;
				@Override
				public void onSuccess(JSONObject jsonObj) {
					try {
						json = jsonObj.getJSONArray("users");
						aUsers.addAll(User.fromJSON(json));
						aUsers.notifyDataSetChanged();
						Log.d("DEBUG", json.toString());
						//hideProgressBar();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};
				@Override
				public void onFailure(Throwable e, JSONObject s) {
					Toast.makeText(getActivity(), getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", s.toString());
				}
			}, userName);
		} else {
			Toast.makeText(getActivity(),getResources().getString(R.string.no_network),Toast.LENGTH_SHORT).show();
		}
	}
	
	private void populateFollowingInfo() {
		if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
			//showProgressBar();
			client.getMyFollowing(new JsonHttpResponseHandler() {
				JSONArray json = null;
				@Override
				public void onSuccess(JSONObject jsonObj) {
					try {
						json = jsonObj.getJSONArray("users");
						aUsers.addAll(User.fromJSON(json));
						aUsers.notifyDataSetChanged();
						Log.d("DEBUG", json.toString());
						//hideProgressBar();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};
				@Override
				public void onFailure(Throwable e, JSONObject s) {
					Toast.makeText(getActivity(), getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", s.toString());
				}
			}, userName);
		} else {
			Toast.makeText(getActivity(),getResources().getString(R.string.no_network),Toast.LENGTH_SHORT).show();
		}
	}
}
