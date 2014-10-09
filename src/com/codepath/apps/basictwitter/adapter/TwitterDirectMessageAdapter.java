package com.codepath.apps.basictwitter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TwitterDirectMessageAdapter extends ArrayAdapter<Tweet>{
	private static class ViewHolder {
		private ImageView ivProfileImg;
		private TextView tvUserName;
		private TextView tvTimeStamp;
		private TextView tvScreename;
		private TextView tvmessage;
	}
	private Context context;

	public TwitterDirectMessageAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final Tweet tweet = getItem(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.direct_message, parent, false);
			viewHolder.ivProfileImg = (ImageView) convertView
					.findViewById(R.id.ivProfileImg);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			viewHolder.tvTimeStamp = (TextView) convertView
					.findViewById(R.id.tvTimeStamp);
			viewHolder.tvScreename = (TextView) convertView
					.findViewById(R.id.tvScreename);
			viewHolder.tvmessage = (TextView) convertView
					.findViewById(R.id.tvmessage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.ivProfileImg.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(tweet.getUser().getProfileImageUrl(),
				viewHolder.ivProfileImg);
		viewHolder.tvUserName.setText(tweet.getUser().getName());
		viewHolder.tvScreename.setText("@" + tweet.getUser().getScreenName());
		viewHolder.tvmessage.setText(tweet.getBody());
		viewHolder.tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet
				.getCreatedAt()));
		return convertView;

	}

}
