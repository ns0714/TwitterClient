package com.codepath.apps.basictwitter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
	private TextView tvReplyTweet;
	private Tweet tweet;
	private TwitterClient client;
	private String tweetMsg;
	private ListView lvReplies;
	private ArrayAdapter<Tweet> aTweets;

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
		tvReplyTweet = (TextView) findViewById(R.id.tvReplyText);
		btnReply = (Button) findViewById(R.id.btnReply);

		ivImg.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivImg);

		if (tweet.getMediaUrl() != null) {
			ivMediaUrl.setImageResource(android.R.color.transparent);
			ImageLoader mediaLoader = ImageLoader.getInstance();
			mediaLoader.displayImage(tweet.getMediaUrl(),
					ivMediaUrl);
		}else{
			ivMediaUrl.setVisibility(View.GONE);
		}
		tvFullName.setText(tweet.getUser().getName());
		tvScreenName.setText("@" + tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		etReplyTweet.setText("@" + tweet.getUser().getScreenName());
		tvTimeStamp.setText(tweet.getCreatedAt());
		btnReply.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		postReplyToStatusMsg(Long.toString(tweet.getUid()));
	}

	private void postReplyToStatusMsg(String uid) {
		// TODO Auto-generated method stub
		tweetMsg = etReplyTweet.getText().toString();
		client.postReplyToStatus(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				System.out.println("SUCCESS!!!!!!!");
				tvReplyTweet.setText(tweetMsg);
				Log.d("DEBUG", arg0);
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.d("DEBUG", arg1);
			}
		}, tweetMsg, uid);
	}
}
