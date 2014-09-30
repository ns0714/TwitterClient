package com.codepath.apps.basictwitter.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.restclienttemplate.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetFragment extends DialogFragment implements
		OnClickListener {

	private ImageView ivProfileImg;
	private TextView tvFullName;
	private TextView tvUserName;
	private TextView tvCounter;
	private Button btnTweet;
	private EditText etTweetMsg;
	private static User currUser;
	private String status;
	int totalChars = 140;

	public ComposeTweetFragment() {

	}

	public interface ComposeTweetFragmentListener {
		 void onFinishedComposingStatus(String status);
	}

	public static ComposeTweetFragment newInstance(String title, User user) {
		ComposeTweetFragment dialog = new ComposeTweetFragment();
		currUser = new User();
		currUser = user;
		 dialog.setStyle(dialog.STYLE_NO_FRAME,
		 android.R.style.Theme_Holo_Light);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setUpDialog();

		View view = inflater.inflate(R.layout.compose_tweet, container);
		ivProfileImg = (ImageView) view.findViewById(R.id.ivProfileImg);
		tvFullName = (TextView) view.findViewById(R.id.tvFullName);
		tvUserName = (TextView) view.findViewById(R.id.tvUserId);
		tvCounter = (TextView) view.findViewById(R.id.tvCounter);
		etTweetMsg = (EditText) view.findViewById(R.id.etTweetMsg);
		btnTweet = (Button) view.findViewById(R.id.btnTweet);

		ivProfileImg.setImageResource(android.R.color.transparent);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(currUser.getProfileImageUrl(), ivProfileImg);

		tvFullName.setText(currUser.getName());
		tvUserName.setText(getResources().getString(R.string.user_id)
				+ currUser.getScreenName());
		
		System.out.println("#### " + currUser.getName()
				+ currUser.getScreenName());
		
		
		final TextWatcher mTextEditorWatcher = new TextWatcher() {
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		        }

		        public void onTextChanged(CharSequence s, int start, int before, int count) {
		           //This sets a textview to the current length
		        	System.out.println("#####" + Integer.parseInt(String.valueOf(s.length())));
		        	//totalChars -=Integer.parseInt(String.valueOf(s.length()));
		        	
		            tvCounter.setText(Integer.toString(totalChars - Integer.parseInt(String.valueOf(s.length()))));
		            System.out.println("@@@@" + totalChars);
		        }

		        public void afterTextChanged(Editable s) {
		        	
		        }
		};
		
		
		etTweetMsg.addTextChangedListener(mTextEditorWatcher);
		
		btnTweet.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTweet: {

			ComposeTweetFragmentListener listener = (ComposeTweetFragmentListener)getActivity();
			status = etTweetMsg.getText().toString();

			System.out.println("This is status " + status);
			listener.onFinishedComposingStatus(status);
			dismiss();
		}
		}
	}

	public void setUpDialog() {
		// getDialog().setTitle(getResources().getText("");
		// getDialog().getActionBar().hide();
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.white)));
		final WindowManager.LayoutParams params = getDialog().getWindow()
				.getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.gravity = Gravity.CENTER;

	}

}