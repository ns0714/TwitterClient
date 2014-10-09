package com.codepath.apps.basictwitter.activities;

import java.io.FileNotFoundException;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.basictwitter.fragment.UserTimelineFragment;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.basictwitter.helper.TwitterHelper;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private User user;
	private TextView tvName;
	private TextView tvTagLine;
	private TextView tvFollowers;
	private TextView tvFollowing;
	private ImageView ivProfileImage;
	private TextView tvTotalTweets;
	private ImageView ivBannerImg;
	private TwitterClient client;

	private UserTimelineFragment userTimelineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		client = TwitterApplication.getRestClient();
		loadProfileInfo();
	}

	private void loadProfileInfo() {

		if (NetworkCheckHelper.isNetworkAvailable(this)) {

			client.getUserCredentials(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					user = (User) getIntent().getSerializableExtra("user");
					getActionBar().setTitle("@" + user.getScreenName());
					populateProfileHeader();
					initFragment();
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					System.out.println("@@@@@" + "profile json failed");
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.failed),
							Toast.LENGTH_SHORT).show();
					super.onFailure(arg0, arg1);
				}
			});
		} else {
			Toast.makeText(this, getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void populateProfileHeader() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvTagLine = (TextView) findViewById(R.id.tvTagLine);
		tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvTotalTweets = (TextView) findViewById(R.id.tvTotalTweets);
		ivBannerImg = (ImageView) findViewById(R.id.ivBannerImg);

		tvFollowers.setText(Html.fromHtml("<b>"
				+ TwitterHelper.convertNumbers(user.getFollowers()) + " "
				+ "</b>" + "<br />"
				+ getResources().getString(R.string.followers)));
		tvFollowing.setText(Html.fromHtml("<b>"
				+ TwitterHelper.convertNumbers(user.getFollowing()) + " "
				+ "</b>" + "<br />"
				+ getResources().getString(R.string.following)));
		tvTotalTweets
				.setText(Html.fromHtml("<b>"
						+ TwitterHelper.convertNumbers(user.getTotalTweets())
						+ " " + "</b>" + "<br />"
						+ getResources().getString(R.string.tweets)));
		tvName.setText(user.getName());
		tvTagLine.setText(user.getTagLine());

		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(),
				ivProfileImage);
		try {
			loadBackgroundImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if (user.getBannerUrl() != null) { try{
		 * ImageLoader.getInstance().displayImage(user.getBannerUrl(),
		 * ivBannerImg); }catch(FileNotFoundException e){ e.printStackTrace(); }
		 * } else { ivBannerImg.setBackgroundResource(R.color.transparent); }
		 */

		tvFollowers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this,
						UserListActivity.class);
				i.putExtra("screen_name", user.getScreenName());
				i.putExtra("list", "followers");
				startActivity(i);
			}
		});

		tvFollowing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this,
						UserListActivity.class);
				i.putExtra("screen_name", user.getScreenName());
				i.putExtra("list", "following");
				startActivity(i);
			}
		});

	}

	public void loadBackgroundImage() throws FileNotFoundException {
		if (user.getBannerUrl() != null) {
			ImageLoader.getInstance().displayImage(user.getBannerUrl(),
					ivBannerImg);
		} else if (user.getBackgroundPic() != null) {
			ImageLoader.getInstance().displayImage(user.getBackgroundPic(),
					ivBannerImg);
		} else {
			ivBannerImg.setBackgroundResource(R.color.transparent);
		}
	}

	protected void initFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		userTimelineFragment = UserTimelineFragment.newInstance(user
				.getScreenName());
		transaction.replace(R.id.frUserTimeline, userTimelineFragment);
		transaction.commit();
	}
}
