package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Activity that contains an interactive Google Map fragment. Users can record
 * a traveled path, mark the map with information and take pictures that become
 * associated with the map.
 */
public class WhosFree extends BaseActivity implements OnClickListener, OnLongClickListener {

	private ArrayList<Post> mPostList;
	private PostListAdapter mPostAdapter;
	private ListView mPostLayout;
	
	private String musername;
	
	private FullPostView fullPost;
	
	private Button addFriend;
	private Button removeFriend;
	private Button viewRequests;
	private EditText username;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       MapsInitializer.initialize(this);
       getWindow().setSoftInputMode(
             WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
       if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
          Log.e("WhosFree", "No Google Play Services");
       }
       
       ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
	   query.findInBackground(new FindCallback<ParseObject>() {
		   public void done(List<ParseObject> reqList, ParseException e) {
			   if (e == null) {
				   // Check for accepted friend requests
				   for (ParseObject obj : reqList) {
					   if (obj != null && obj.getString("OwnedBy").equals(DataStore.getCurrentUser().getEmail())) {
						   List<String> toAdd = obj.getList("AcceptedRequests");
						   
						   if (toAdd != null) {
							   for (String s : toAdd) {
								   DataStore.trueAddParseFriend(s);
							   }
							   obj.removeAll("AcceptedRequests", toAdd);
							   obj.saveInBackground();
						   }
					   }
				   }
				   // Check for friends who have deleted the user
				   for (ParseObject obj : reqList) {
					   if (obj != null && obj.getString("OwnedBy").equals(DataStore.getCurrentUser().getEmail())) {
						   List<String> toDelete = obj.getList("DeletedFriends");
						   
						   if (toDelete != null) {
							   for (String s : toDelete) {
								   DataStore.removeParseFriend(s);
							   }
							   obj.removeAll("DeletedFriends", toDelete);
							   obj.saveInBackground();
						   }
					   }
				   }
	           } else {
	        	   Log.d("score", "Error: " + e.getMessage());
	           }
	       }
	   });
       
       this.mPostList = new ArrayList<Post>();
       this.mPostAdapter = new PostListAdapter(this, this.mPostList);
      
       Intent i = getIntent();
       musername = i.getStringExtra("ParseUser");
       setBaseUsername(musername);
       setActivityName("WhosFree");
       
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
    	              
    	              final Post post = new Post(user.getString("FirstName") + " " + user.getString("LastName"), user.getEmail(), date.toString(), userLocation, loc, user.getObjectId());
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
		addFriend = (Button)findViewById(R.id.whosFreeAddFriendButton);
		removeFriend = (Button)findViewById(R.id.whosFreeRemoveFriendButton);
		viewRequests = (Button)findViewById(R.id.whosFreeViewRequestsButton);
		username = (EditText)findViewById(R.id.whosFreeAddFriendEditText);
		
		addFriend.setOnClickListener(this);
		removeFriend.setOnClickListener(this);
		viewRequests.setOnClickListener(this);
		
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
      String temp;
      switch(v.getId()) {
         
      case R.id.whosFreeAddFriendButton:
         temp = username.getText().toString();
         if (!temp.equals("")) {
            getWindow().setSoftInputMode(
                  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            DataStore.addParseFriend(temp);
            username.setText("");
         }
         break;
         
      case R.id.whosFreeRemoveFriendButton:
         temp = username.getText().toString();
         if (!temp.equals("")) {
            getWindow().setSoftInputMode(
                  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            DataStore.removeParseFriend(temp);
            removePost(temp);
            username.setText("");
         }
         break;
         
      case R.id.whosFreeViewRequestsButton:
    	  Intent i = new Intent(this, FriendRequestsActivity.class);
    	  startActivity(i);
    	  break;
         
      case R.id.fullPostHangButton:
         Toast.makeText(this, "The user has been notified that you're on the way.", Toast.LENGTH_SHORT).show();
         
         final ParseUser curr = DataStore.getCurrentUser();
         
         ParsePush push = new ParsePush();
         push.setChannel("channel" + fullPost.getPost().getUserId());
         push.setMessage(curr.getString("FirstName") + " " + curr.getString("LastName") + " is coming!");
         push.sendInBackground();
         
         
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

	@Override
	public boolean onLongClick(final View v) {
		
		AlertDialog.Builder deleteDialog = new AlertDialog.Builder(WhosFree.this);
		deleteDialog.setTitle("Delete Friend?");
		
		deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(WhosFree.this, "TODO: Delete", Toast.LENGTH_SHORT).show();
				DataStore.deleteParseFriend(((PostView)v).getPost().getEmail());
			}
		});
		
		deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	
		deleteDialog.show();
		
		return false;
	}
}