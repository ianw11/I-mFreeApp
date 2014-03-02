package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
	
	private Button backButton;
	private Button addFriend;
	private Button removeFriend;
	
	
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
    	      List<String> friends = DataStore.getParseFriends();
    	        for (ParseUser user : objects) {
    	           final Date date = user.getDate("TimeFree");
    	           if (date != null && !user.getUsername().equals(musername) && date.after(new Date()) && friends.contains(user.getUsername())) {
    	              final ParseGeoPoint gp = user.getParseGeoPoint("Location");
    	              final LatLng loc = new LatLng(gp.getLatitude(), gp.getLongitude());
    	              final String userLocation = user.getString("UserLocation");
    	              
    	              final Post post = new Post(user.getString("FriendlyName"), user.getEmail(), date.toString(), userLocation, loc);
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
		
		backButton = (Button)findViewById(R.id.whosFreeBackButton);
		addFriend = (Button)findViewById(R.id.whosFreeAddFriendButton);
		removeFriend = (Button)findViewById(R.id.whosFreeRemoveFriendButton);
		
		backButton.setOnClickListener(this);
		addFriend.setOnClickListener(this);
		removeFriend.setOnClickListener(this);
	}

	
	
	public void addPost(Post post) {
		this.mPostList.add(post);
		this.mPostAdapter.notifyDataSetChanged();
	}
	
	public void removePost(String username) {
	   for (Post p : mPostList) {
	      if (p.getEmail().equals(username)) {
	         mPostList.remove(p);
	         break;
	      }
	   }
	   mPostAdapter.notifyDataSetChanged();
	}

   @Override
   public void onClick(View v) {
      switch(v.getId()) {
      
      case R.id.whosFreeBackButton:
         finish();
         break;
         
      case R.id.whosFreeAddFriendButton:
         Toast.makeText(this, "Add Friend", Toast.LENGTH_SHORT).show();
         DataStore.addParseFriend(((EditText)findViewById(R.id.whosFreeAddFriendEditText)).getText().toString());
         break;
         
      case R.id.whosFreeRemoveFriendButton:
         final String removedUser = ((EditText)findViewById(R.id.whosFreeAddFriendEditText)).getText().toString();
         DataStore.removeParseFriend(removedUser);
         removePost(removedUser);
         break;
         
      case R.id.fullPostHangButton:
         Toast.makeText(this, "Hang Button Pressed", Toast.LENGTH_SHORT).show();
         break;
      
      default:
         LinearLayout layout = (LinearLayout)findViewById(R.id.whosFreeLayout);
         if (fullPost == null) {
            fullPost = new FullPostView(this);
            // Adding this to index 1 of the linearlayout.  This needs to be
            // changed if more gets added to layout_whosfree.xml
            Log.d("WhosFree", "width: " + layout.getWidth());
            layout.addView(fullPost, 1, new LayoutParams(600, 600));
         }
         
         fullPost.setPost(((PostView)v).getPost());
         break;
      }
      
   }
}