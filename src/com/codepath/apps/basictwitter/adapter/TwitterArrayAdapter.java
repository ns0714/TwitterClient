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
import com.codepath.apps.basictwitter.activities.FullTweetActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
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
	}

	private Context context;

	public TwitterArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
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
			viewHolder.ivFav.setImageResource(R.drawable.star_on);
		}else{
			viewHolder.ivFav.setImageResource(R.drawable.favstar_off);
		}
		
		if(tweet.isRetweeted()){
			viewHolder.ivRetweet.setImageResource(R.drawable.ic_retweeted);
		}else{
			viewHolder.ivRetweet.setImageResource(R.drawable.ic_retweet);
		}
		
		if (tweet.getMediaUrl() != null) {
			//viewHolder.ivMedia.setImageResource(android.R.color.transparent);
			ImageLoader mediaLoader = ImageLoader.getInstance();
			mediaLoader.displayImage(tweet.getMediaUrl(), viewHolder.ivMedia);
			viewHolder.ivMedia.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ivMedia.setVisibility(View.GONE);
		}
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, FullTweetActivity.class);
				i.putExtra("tweet", tweet);
				context.startActivity(i);
			}
		});

		return convertView;

	}

}
