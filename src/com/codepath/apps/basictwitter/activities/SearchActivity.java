package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragment.SearchResultsTweetsFragment;

public class SearchActivity extends FragmentActivity {

	TextView tv;
	String srchQuery;
	SearchResultsTweetsFragment searhFrag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		srchQuery = getIntent().getStringExtra("query");
		initFragment();
	}
	
	protected void initFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		searhFrag = SearchResultsTweetsFragment.newInstance(srchQuery);
		transaction.replace(R.id.frSearchTweets, searhFrag);
		transaction.commit();
	}
}
