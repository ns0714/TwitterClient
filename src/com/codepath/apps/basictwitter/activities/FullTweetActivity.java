package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.client.TwitterApplication;
import com.codepath.apps.basictwitter.client.TwitterClient;
import com.codepath.apps.basictwitter.fragment.ComposeTweetFragment;
import com.codepath.apps.basictwitter.helper.NetworkCheckHelper;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FullTweetActivity extends FragmentActivity implements OnClickListener {

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
	private ImageView ivFavIcon;
	private ImageView ivReplyIcon;
	private ImageView ivRetweetIcon;
	private ImageView ivShareIcon;
	private String tweetMsg;
	private Tweet tweet;
	private TwitterClient client;

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
		ivFavIcon = (ImageView) findViewById(R.id.ivFavoriteIcon);
		ivRetweetIcon = (ImageView) findViewById(R.id.ivRetweetIcon);
		ivReplyIcon = (ImageView) findViewById(R.id.ivReplyIcon);
		ivShareIcon = (ImageView) findViewById(R.id.ivShareIcon);
		
		ivReplyIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openCompose();				
			}
		});
		
		ivFavIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.postFavoriteToStatus(new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject jsonObj) {
						System.out.println("Success");
						ivFavIcon.setImageResource(R.drawable.ic_fav_star_on);
					}
					
					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						super.onFailure(arg0, arg1);
					}
				}, Long.toString(tweet.isId_str()));
			}
	});
		
		ivRetweetIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.postRetweetStatus(new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int arg0, String arg1) {
						ivRetweetIcon.setImageResource(R.drawable.ic_re_tweet);
					}
					
					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						super.onFailure(arg0, arg1);
					}
				}, Long.toString(tweet.isId_str()));
			}
		});
		
		ivShareIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onShareItem();
			}
		});
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
			ivFavIcon.setImageResource(R.drawable.ic_fav_star_on);
		} else {
			ivFavIcon.setImageResource(R.drawable.ic_fav_star_off);
		}

		if (tweet.isRetweeted()) {
			ivRetweetIcon.setImageResource(R.drawable.ic_re_tweet);
		} else {
			ivRetweetIcon.setImageResource(R.drawable.ic_retweet);
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
		postReplyToStatusMsg(Long.toString(tweet.getUid()));
	}

	private void postReplyToStatusMsg(String uid) {
		tweetMsg = getResources().getString(R.string.user_id)
				+ tweet.getUser().getScreenName() + " "
				+ etReplyTweet.getText().toString();

		if (NetworkCheckHelper.isNetworkAvailable(this)) {
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
					Log.d("DEBUG", ex.toString());
				}
			}, tweetMsg, uid);
		} else {
			Toast.makeText(this, getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void openCompose(){
		FragmentManager frag = getSupportFragmentManager();
		ComposeTweetFragment diag = ComposeTweetFragment.newInstance(
				"Compose Tweets", tweet.getUser(), tweet.getUser().getScreenName());
		diag.show(frag, "compose_tweet");
	}
	
	public void onShareItem() {
	        // Construct a ShareIntent with link to image
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, tweetMsg);
	        //shareIntent.setType("image/*");
	        // Launch sharing dialog for image
	        startActivity(Intent.createChooser(shareIntent, "Share Image"));	
	    } /*else {
	    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_fullimage), 
	    			Toast.LENGTH_SHORT).show();
	    			}
	    }*/

}
