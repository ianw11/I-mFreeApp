package edu.calpoly.android.imfree;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostView extends LinearLayout {

	private TextView mPosterNameTextView;
	
	private Post mPost;
	
	public PostView(Context context, Post post) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.post_view, this, true);
		
		this.mPosterNameTextView = (TextView)findViewById(R.id.posterNameTextView);
		
		/* Yes, I know it uses an OnClickListener that's somewhere else. It's for scope reasons
		 * The context is WhosFree, and it uses the default in the switch statement */
		this.setOnClickListener((OnClickListener)context);
		
		this.setPost(post);
	}

	public void setPost(Post post) {
		this.mPost = post;
		this.mPosterNameTextView.setText(mPost.getPosterName());
	}
	
	public Post getPost() {
	   return mPost;
	}
}
