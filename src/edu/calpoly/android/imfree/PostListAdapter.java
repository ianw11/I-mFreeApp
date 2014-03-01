package edu.calpoly.android.imfree;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PostListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Post> mPosts;
	
	public PostListAdapter(Context context, List<Post> posts) {
		this.mContext = context;
		this.mPosts = posts;
	}
	
	@Override
	public int getCount() {
		return this.mPosts.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mPosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PostView postView = null;
		
		if (convertView == null) {
			postView = new PostView(mContext, this.mPosts.get(position));
		}
		else {
			postView = (PostView)convertView;
		}
		postView.setPost(this.mPosts.get(position));
		return postView;
	}
}
