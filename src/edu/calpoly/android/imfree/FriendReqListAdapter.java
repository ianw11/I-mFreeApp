package edu.calpoly.android.imfree;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FriendReqListAdapter extends BaseAdapter {
	private SherlockFragmentActivity mContext;
	private List<String> mRequests;
	
	public FriendReqListAdapter(Context context, List<String> requests) {
		this.mContext = (SherlockFragmentActivity)context;
		this.mRequests = requests;
	}
	
	@Override
	public int getCount() {
		return this.mRequests.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mRequests.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendRequestView friendReqView = null;
		
		if (convertView == null) {
		   // Always pass in the same map fragment
			friendReqView = new FriendRequestView(mContext, mRequests.get(position));
		}
		else {
			friendReqView = (FriendRequestView)convertView;
		}
		friendReqView.setEmail(this.mRequests.get(position));
		return friendReqView;
	}
}
