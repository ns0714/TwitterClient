package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FullTweetActivity extends Activity implements OnClickListener {

	private ImageView ivImg;
	private TextView tvFullName;
	private TextView tvScreenName;
	private TextView tvBody;
	private TextView tvTimeStamp;
	private EditText etReplyTweet;
	private Button btnReply;
	private ImageView ivMediaUrl;
	private TextView tvRetweets;
	private TextView tvFavorites;
	private Tweet tweet;
	private TwitterClient client;
	private ImageView ivFav;
	private ImageView ivRetweeted;
	private String tweetMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_tweet);

		client = TwitterApplication.getRestClient();

		tweet = (Tweet) getIntent().getSerializableExtra("tweet");

		ivImg = (ImageView) findViewById(R.id.ivImg);
		tvFullName = (TextView) findViewById(R.id.tvUser);
		tvScreenName = (TextView) findViewById(R.id.tvscreenName);
		tvBody = (TextView) findViewById(R.id.tvStatus);
		etReplyTweet = (EditText) findViewById(R.id.etReplyTweet);
		tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
		ivMediaUrl = (ImageView) findViewById(R.id.ivMediaUrl);
		tvRetweets = (TextView) findViewById(R.id.tvRetweets);
		tvFavorites = (TextView) findViewById(R.id.tvFavorites);
		btnReply = (Button) findViewById(R.id.btnReply);
		ivFav = (ImageView) findViewById(R.id.ivFavoriteIcon);
		ivRetweeted = (ImageView) findViewById(R.id.ivRetweetIcon);

		ivImg.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivImg);

		tvFullName.setText(tweet.getUser().getName());
		tvScreenName.setText(getResources().getString(R.string.user_id)
				+ tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimeStamp.setText(tweet.getCreatedAt());
		tvRetweets.setText(Html.fromHtml("<b>" + tweet.getRetweetCount()
				+ "</b>" + " " + getResources().getString(R.string.retweet)));
		tvFavorites.setText(Html.fromHtml("<b>" + tweet.getFavCount() + "</b>"
				+ " " + getResources().getString(R.string.favorites)));

		btnReply.setOnClickListener(this);

		if (tweet.getFavorited()) {
			ivFav.setImageResource(R.drawable.star_on);
		} else {
			ivFav.setImageResource(R.drawable.favstar_off);
		}

		if (tweet.isRetweeted()) {
			ivRetweeted.setImageResource(R.drawable.ic_retweeted);
		} else {
			ivRetweeted.setImageResource(R.drawable.ic_retweet);
		}

		if (tweet.getMediaUrl() != null) {
			ivMediaUrl.setImageResource(android.R.color.transparent);
			ImageLoader mediaLoader = ImageLoader.getInstance();
			mediaLoader.displayImage(tweet.getMediaUrl(), ivMediaUrl);
		} else {
			ivMediaUrl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		postReplyToStatusMsg(Long.toString(tweet.getUid()));
	}

	private void postReplyToStatusMsg(String uid) {
		// TODO Auto-generated method stub
		tweetMsg = getResources().getString(R.string.user_id)
				+ tweet.getUser().getScreenName() + " "
				+ etReplyTweet.getText().toString();

		client.postReplyToStatus(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				tweet = Tweet.fromJSON(json);
				Intent i = new Intent(FullTweetActivity.this,
						TimelineActivity.class);
				i.putExtra("tweet", tweet);
				startActivity(i);
			}

			@Override
			public void onFailure(Throwable ex, String arg1) {
				// TODO Auto-generated method stub
				 Log.d("DEBUG", ex.toString());
			}
		}, tweetMsg, uid);
	}
}
