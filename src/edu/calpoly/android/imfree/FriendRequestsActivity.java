package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FriendRequestsActivity extends SherlockFragmentActivity {

	private List<String> mRequests;
	private FriendReqListAdapter mAdapter;
	
	private ListView mFriendReqListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_friend_requests);
		
		mFriendReqListView = (ListView)findViewById(R.id.friendRequestsListView);
		updateFriendRequests();
		mAdapter = new FriendReqListAdapter(this, mRequests);
		mFriendReqListView.setAdapter(mAdapter);
	}

	private void updateFriendRequests() {
		ParseUser currUser = DataStore.getCurrentUser();
		mRequests = currUser.getList("FriendRequests");
	}
}
