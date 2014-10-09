package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragment.UsersListFragment;

public class UserListActivity extends FragmentActivity {

	private UsersListFragment userListFrag;
	String screenName;
	String list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_followers);
		screenName =getIntent().getStringExtra("screen_name");
		list =getIntent().getStringExtra("list");
		
		if(list.equals("following")){
			getActionBar().setTitle(getResources().getString(R.string.following));
		}else if(list.equals("followers")){
			getActionBar().setTitle(getResources().getString(R.string.followers));
		}
		initFragment();
	}
	
	protected void initFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		userListFrag = UsersListFragment.newInstance(list,screenName);
		transaction.replace(R.id.flFollowers, userListFrag);
		transaction.commit();
	}
}
