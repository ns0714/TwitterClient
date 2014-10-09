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
import com.codepath.apps.restclienttemplate.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TwitterFollowersAdapter extends ArrayAdapter<User>{
	private static class ViewHolder {
		private ImageView ivFProfileImage;
		private TextView tvFUserName;
		private TextView tvFScreenName;
		private TextView tvFTagLine;
		private ImageView ivAddProfile;
	}
	private Context context;

	public TwitterFollowersAdapter(Context context, List<User> users) {
		super(context, 0, users);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final User user = getItem(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.user_followers, parent, false);
			viewHolder.ivFProfileImage = (ImageView) convertView
					.findViewById(R.id.ivFProfileImage);
			viewHolder.tvFUserName = (TextView) convertView
					.findViewById(R.id.tvFUsername);
			viewHolder.tvFScreenName = (TextView) convertView
					.findViewById(R.id.tvFScreenName);
			viewHolder.tvFTagLine = (TextView) convertView
					.findViewById(R.id.tvFTagLine);
			viewHolder.ivAddProfile = (ImageView)convertView.findViewById(R.id.ivAddProfile);
		
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.ivFProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(user.getProfileImageUrl(),
				viewHolder.ivFProfileImage);
		viewHolder.tvFUserName.setText(user.getName());
		viewHolder.tvFScreenName.setText("@" + user.getScreenName());
		viewHolder.tvFTagLine.setText(user.getTagLine());
		
		if(user.isFollowing()){
			viewHolder.ivAddProfile.setVisibility(View.VISIBLE);
		}else{
			viewHolder.ivAddProfile.setVisibility(View.GONE);
		}
		return convertView;

	}

}
