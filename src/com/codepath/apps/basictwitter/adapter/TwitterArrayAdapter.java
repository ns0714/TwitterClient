package com.codepath.apps.basictwitter.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.activities.FullImageDisplayActivity;
import com.codepath.apps.basictwitter.activities.FullTweetActivity;
import com.codepath.apps.basictwitter.activities.ProfileActivity;
import com.codepath.apps.basictwitter.helper.TwitterHelper;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TwitterArrayAdapter extends ArrayAdapter<Tweet> {

	private static class ViewHolder {
		private ImageView ivProfileImage;
		private TextView tvUserName;
		private TextView tvBody;
		private TextView tvScreenName;
		private TextView tvTimeStamp;
		private ImageView ivMedia;
		private ImageView ivReply;
		private ImageView ivRetweet;
		private ImageView ivFav;
		private TextView tvRetweetCnt;
		private TextView tvFavCnt;
		private TextView tvIfRetweeted;
		private ImageView ivRetweetIc;
	}

	private Context context;

	public TwitterArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Tweet tweet = getItem(position);
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.tweet_item, parent, false);
			viewHolder.ivProfileImage = (ImageView) convertView
					.findViewById(R.id.ivProfileImage);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUsername);
			viewHolder.tvBody = (TextView) convertView
					.findViewById(R.id.tvBody);
			viewHolder.tvBody.setMovementMethod(LinkMovementMethod
					.getInstance());
			viewHolder.tvScreenName = (TextView) convertView
					.findViewById(R.id.tvScreenName);
			viewHolder.tvTimeStamp = (TextView) convertView
					.findViewById(R.id.tvTimestamp);
			viewHolder.ivMedia = (ImageView) convertView
					.findViewById(R.id.ivMedia);
			viewHolder.ivReply = (ImageView) convertView
					.findViewById(R.id.ivReply);
			viewHolder.ivRetweet = (ImageView) convertView
					.findViewById(R.id.ivRetweet);
			viewHolder.ivFav = (ImageView) convertView
					.findViewById(R.id.ivFavorite);
			viewHolder.tvRetweetCnt = (TextView) convertView
					.findViewById(R.id.tvRetweetCnt);
			viewHolder.tvFavCnt = (TextView) convertView
					.findViewById(R.id.tvFavCnt);
			viewHolder.tvIfRetweeted = (TextView) convertView
					.findViewById(R.id.tvIfRetweeted);
			viewHolder.ivRetweetIc = (ImageView) convertView
					.findViewById(R.id.ivRetweetedIc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvIfRetweeted.setVisibility(View.VISIBLE);
		viewHolder.ivRetweetIc.setVisibility(View.VISIBLE);
		
		if((tweet.isRetweeted())){
			viewHolder.tvIfRetweeted.setVisibility(View.VISIBLE);
			viewHolder.tvIfRetweeted.setText(tweet.getUser().getName() + "  retweeted");
			viewHolder.ivRetweetIc.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tvIfRetweeted.setVisibility(View.GONE);
			viewHolder.ivRetweetIc.setVisibility(View.GONE);
		}
		
		viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(tweet.getUser().getProfileImageUrl(),
				viewHolder.ivProfileImage);
		viewHolder.tvUserName.setText(tweet.getUser().getName());
		viewHolder.tvBody.setText(tweet.getBody());
		viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
		viewHolder.tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet
				.getCreatedAt()));
	
		viewHolder.tvRetweetCnt.setText(String.valueOf(tweet.getRetweetCount()));
		viewHolder.tvFavCnt.setText(String.valueOf(tweet.getFavCount()));
		
		if(tweet.getFavorited()){
			viewHolder.ivFav.setImageResource(R.drawable.ic_fav_star_on);
		}else{
			viewHolder.ivFav.setImageResource(R.drawable.ic_fav_star_off);
		}
		
		if(tweet.isRetweeted()){
			viewHolder.ivRetweet.setImageResource(R.drawable.ic_re_tweet);
		}else{
			viewHolder.ivRetweet.setImageResource(R.drawable.ic_retweet);
		}
		
		if (tweet.getMediaUrl() != null) {
			//viewHolder.ivMedia.setImageResource(android.R.color.transparent);
			ImageLoader mediaLoader = ImageLoader.getInstance();
			mediaLoader.displayImage(tweet.getMediaUrl(), viewHolder.ivMedia);
			viewHolder.ivMedia.setVisibility(View.VISIBLE);
			viewHolder.ivMedia.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, FullImageDisplayActivity.class);
					i.putExtra("tweet", tweet);
					context.startActivity(i);
				}
			});
		} else {
			viewHolder.ivMedia.setVisibility(View.GONE);
		}
		
		viewHolder.ivProfileImage.setTag(tweet.getUser());
		viewHolder.ivProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ProfileActivity.class);
				i.putExtra("user", tweet.getUser());
				context.startActivity(i);
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, FullTweetActivity.class);
				i.putExtra("tweet", tweet);
				context.startActivity(i);
			}
		});

		return convertView;

	}

}
