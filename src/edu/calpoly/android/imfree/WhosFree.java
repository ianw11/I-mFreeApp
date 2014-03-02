package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Activity that contains an interactive Google Map fragment. Users can record
 * a traveled path, mark the map with information and take pictures that become
 * associated with the map.
 */
public class WhosFree extends SherlockFragmentActivity implements OnClickListener {

	private ArrayList<Post> mPostList;
	private PostListAdapter mPostAdapter;
	private ListView mPostLayout;
	
	private String musername;
	
	private FullPostView fullPost;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       MapsInitializer.initialize(this);
       int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
       if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
          Log.e("WhosFree", "No Google Play Services");
       }
       
       this.mPostList = new ArrayList<Post>();
       this.mPostAdapter = new PostListAdapter(this, this.mPostList);
      
       Intent i = getIntent();
       musername = i.getStringExtra("ParseUser");
		
       initLayout();
       initLocationData();
       
    }
    
    /**
     * Initializes all Location-related data.
     */
    private void initLocationData() {
    	// Get posts/info from database
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	
    	query.findInBackground(new FindCallback<ParseUser>() {
    	  public void done(List<ParseUser> objects, ParseException e) {
    	    if (e == null) {
    	        for (ParseUser user : objects) {
    	           final Date date = user.getDate("TimeFree");
    	           if (date != null && !user.getUsername().equals(musername) && date.after(new Date())) {
    	              final ParseGeoPoint gp = user.getParseGeoPoint("Location");
    	              final LatLng loc = new LatLng(gp.getLatitude(), gp.getLongitude());
    	              final String userLocation = user.getString("UserLocation");
    	              
    	              final Post post = new Post(user.getString("FriendlyName"), date.toString(), userLocation, loc);
    	              addPost(post);
    	           }
    	        }
    	    } else {
    	        // Something went wrong.
    	       Log.e("WhosFree", "Exception in findInBackground");
    	    }
    	  }
    	});
    	// call addPost for each that is still active
    }
    
    /**
     * Initializes all other data for the application.
     */
	private void initLayout() {
		this.setContentView(R.layout.layout_whosfree);
		this.mPostLayout = (ListView)this.findViewById(R.id.postListViewGroup);
		this.mPostLayout.setAdapter(mPostAdapter);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.whosFreeLayout);
		fullPost = new FullPostView(this);
		layout.addView(fullPost, 0, new LayoutParams(450, 350));
	}

	
	
	public void addPost(Post post) {
		this.mPostList.add(post);
		this.mPostAdapter.notifyDataSetChanged();
	}

   @Override
   public void onClick(View v) {
      
      fullPost.setPost(((PostView)v).getPost());
      
   }
}