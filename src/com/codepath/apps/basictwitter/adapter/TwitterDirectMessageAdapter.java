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

public class TwitterDirectMessageAdapter extends ArrayAdapter<User>{
	private static class ViewHolder {
		private ImageView ivProfileImg;
		private TextView tvUserName;
		private TextView tvTimeStamp;
		private TextView tvScreename;
	}
	private Context context;

	public TwitterDirectMessageAdapter(Context context, List<User> users) {
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
			convertView = inflator.inflate(R.layout.direct_message, parent, false);
			viewHolder.ivProfileImg = (ImageView) convertView
					.findViewById(R.id.ivProfileImg);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			viewHolder.tvTimeStamp = (TextView) convertView
					.findViewById(R.id.tvTimeStamp);
			viewHolder.tvScreename = (TextView) convertView
					.findViewById(R.id.tvScreename);
		
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.ivProfileImg.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(user.getProfileImageUrl(),
				viewHolder.ivProfileImg);
		viewHolder.tvUserName.setText(user.getName());
		viewHolder.tvScreename.setText("@" + user.getScreenName());
		//viewHolder.tvTimeStamp.setText(user.getTagLine());
		
		return convertView;

	}

}
