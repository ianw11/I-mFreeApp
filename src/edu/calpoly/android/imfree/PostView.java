package edu.calpoly.android.imfree;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostView extends LinearLayout {

	private TextView mPosterNameTextView;
	private Post mPost;
	
	// Used as data for a plain user view
	private boolean isUserView = false;
	private String name;
	// End
	
	public PostView(Context context, Post post) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.post_view, this, true);
		
		this.mPosterNameTextView = (TextView)findViewById(R.id.posterNameTextView);
		
		/* Yes, I know it uses an OnClickListener that's somewhere else. It's for scope reasons
		 * The context is WhosFree, and it uses the default in the switch statement */
		this.setOnClickListener((OnClickListener)context);
		this.setOnLongClickListener((OnLongClickListener)context);
		
		this.setPost(post);
	}
	
	// This constructor is to be used when only a name is to be displayed.
	// It simplifies a lot, and only adds a LongClickListener
	public PostView(Context context, String name) {
	   super(context);
	   
	   LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   inflater.inflate(R.layout.post_view, this, true);
	   
	   this.mPosterNameTextView = (TextView)findViewById(R.id.posterNameTextView);
	   this.mPosterNameTextView.setText(name);

	   this.setOnLongClickListener((OnLongClickListener)context);
	   
	   isUserView = true;
	   
	   this.name = name;
	}

	public void setPost(Post post) {
		this.mPost = post;
		this.mPosterNameTextView.setText(mPost.getPosterName());
	}
	
	public void setPost(String name) {
	   this.mPosterNameTextView.setText(name);
	}
	
	public Post getPost() {
	   return mPost;
	}
	
	public boolean isUser() {
	   return isUserView;
	}
	
	public String getUser() {
	   return name;
	}
}
