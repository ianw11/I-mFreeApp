package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.GoogleMap;
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
public class WhosFree extends SherlockFragmentActivity {

	/** The interactive Google Map fragment. */
	private GoogleMap mMap;
	
	private ArrayList<Post> mPostList;
	private PostListAdapter mPostAdapter;
	private ListView mPostLayout;
	
	private String musername;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
      super.onCreate(savedInstanceState);
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
    	//query.whereEqualTo("gender", "female");
    	/*
    	try {
			query.getFirst();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		*/
    	
    	query.findInBackground(new FindCallback<ParseUser>() {
    	  public void done(List<ParseUser> objects, ParseException e) {
    	    if (e == null) {
    	        for (ParseUser user : objects) {
    	           final Date date = user.getDate("TimeFree");
    	           if (date != null && !user.getUsername().equals(musername) && date.after(new Date())) {
    	              final ParseGeoPoint gp = user.getParseGeoPoint("Location");
    	              final LatLng loc = new LatLng(gp.getLatitude(), gp.getLongitude());
    	              final String userLocation = user.getString("UserLocation");
    	              
    	              final Post post = new Post(user.getString("FriendlyName"), date.toString(), userLocation, loc, getSupportFragmentManager());
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
	}

	
	
	public void addPost(Post post) {
		this.mPostList.add(post);
		//mPostList.add(post);
		this.mPostAdapter.notifyDataSetChanged();
	}
}