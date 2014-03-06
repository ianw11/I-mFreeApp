package edu.calpoly.android.imfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendRequestView extends LinearLayout implements OnClickListener {

	private TextView mFriendNameTextView;
	private Button mAcceptButton;
	private Button mDeclineButton;
	
	private String mEmail;
	private Context context;
	
	public FriendRequestView(Context context, String email) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.friend_request_view, this, true);
		
		mEmail = email;
		this.context = context;
		mFriendNameTextView = (TextView)findViewById(R.id.friendReqTextView);
		mFriendNameTextView.setText(mEmail);
		
		mAcceptButton = (Button)findViewById(R.id.friendReqAccept);
		mAcceptButton.setOnClickListener((OnClickListener)context);
		mDeclineButton = (Button)findViewById(R.id.friendReqDecline);
		mDeclineButton.setOnClickListener((OnClickListener)context);
	}
	
	public String getEmail() {
	   return mEmail;
	}
	
	public void setEmail(String email) {
		mEmail = email;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friendReqAccept:
			Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.friendReqDecline:
			Toast.makeText(context, "Declined", Toast.LENGTH_SHORT).show();
			
			break;
		default:
			break;
		}
	}
}
