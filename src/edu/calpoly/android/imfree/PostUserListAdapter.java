package edu.calpoly.android.imfree;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class PostUserListAdapter extends BaseAdapter {

	private SherlockFragmentActivity mContext;
	private List<String> mUsers;
	
	public PostUserListAdapter(Context context, List<String> posts) {
		this.mContext = (SherlockFragmentActivity)context;
		this.mUsers = posts;
	}
	
	@Override
	public int getCount() {
		return this.mUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PostView postView = null;
		
		if (convertView == null) {
			postView = new PostView(mContext, this.mUsers.get(position));
		}
		else {
			postView = (PostView)convertView;
			postView.setPost(this.mUsers.get(position));
		}
		
		return postView;
	}
}
