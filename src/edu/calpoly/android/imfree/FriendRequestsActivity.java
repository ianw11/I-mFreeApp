package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FriendRequestsActivity extends SherlockFragmentActivity implements OnClickListener {

	private List<String> mRequests;
	private FriendReqListAdapter mAdapter;
	
	private ListView mFriendReqListView;
	
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_friend_requests);
		
		mFriendReqListView = (ListView)findViewById(R.id.friendRequestsListView);
		updateFriendRequests();
		mRequests = DataStore.getRequests();
		if (mRequests == null)
			mRequests = new ArrayList<String>();
		mAdapter = new FriendReqListAdapter(this, mRequests);
		mFriendReqListView.setAdapter(mAdapter);
		
		backButton = (Button)findViewById(R.id.friendRequestsBackButton);
		backButton.setOnClickListener(this);
	}

	private void updateFriendRequests() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> reqList, ParseException e) {
				if (e == null) {
					// Emails are unique so first element is the desired user
					for (ParseObject obj : reqList) {
						if (obj.getString("OwnedBy").equals(DataStore.getCurrentUser().getEmail())) {
						   List<Object> req = obj.getList("Requests");
						   if (req != null) {
						      DataStore.setRequests(obj.getList("Requests"));
							   mAdapter.notifyDataSetChanged();
						   }
						}
					}
				} else {
					Log.d("DataStore", "Error: " + e.toString());
				}
			}
		});
	}
	
	public void removeEntry(String email) {
	   mRequests.remove(email);
	   mAdapter.notifyDataSetChanged();
	}

   @Override
   public void onClick(View v) {
      DataStore.invalidateParseFriendRequestsObject();
      finish();
   }
}
