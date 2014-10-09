package com.codepath.apps.basictwitter.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helper.TouchImageView;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullImageDisplayActivity extends Activity {

	private Tweet tweet;
	private TouchImageView ivImageResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image_display);
		
		tweet = (Tweet) getIntent().getSerializableExtra("tweet");

		ivImageResult = (TouchImageView)findViewById(R.id.ivImageResult);
		Picasso.with(this).load(tweet.getMediaUrl()).resize(600, 600).into(ivImageResult, new Callback() {
			
			@Override
			public void onSuccess() {
			}
			
			@Override
			public void onError() {
				Toast.makeText(FullImageDisplayActivity.this, 
						getResources().getString(R.string.error_fullimage), Toast.LENGTH_LONG).show();
			}
		});
		getCustomizedActionBar();
	}
	
	void getCustomizedActionBar(){
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.transperant_white));
		getActionBar().setBackgroundDrawable(colorDrawable);
		getActionBar().setTitle(Html.fromHtml(
				"<font color='#4F4F4F'>"+ "@" + tweet.getUser().getScreenName() +"</font>"));
	}
}
