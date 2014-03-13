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
	private PostUserListAdapter mUserAdapter;
	
	private ListView mPostLayout;
	private FullPostView fullPost;
	private Button addFriend;
	private Button viewRequests;
	private EditText username;
	private Button viewAllToggle;
	private LinearLayout masterLayout;
	
	private boolean isViewAll = false;
	private boolean isFullInflated = false;
	
	private boolean toastAppeared = false;
	
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
      
      setActivityName("WhosFree");
   }
   
   
   @Override
   protected void onResume() {
      super.onResume();
      if (DataStore.getParseFriendRequestsObject() == null) {
         Log.d("WhosFree", "Null");
         DataStore.requeryParseFriendRequestsObject();
      } else {
         Log.d("WhosFree", "Not null");
      }

      this.mPostList = new ArrayList<Post>();
      this.mPostAdapter = new PostListAdapter(this, this.mPostList);
      this.mUserAdapter = new PostUserListAdapter(this, DataStore.getParseFriends());
	   initLayout();
	   initLocationData();
	   fullPost = null;
	   isFullInflated = false;
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
    	         final Date timeNow = new Date();
    	         
    	         for (ParseUser user : objects) {
    	            final Date date = user.getDate("TimeFree");
    	            if (friends.contains(user.getUsername()) && date != null && date.after(timeNow)) {
    	               final ParseGeoPoint gp = user.getParseGeoPoint("Location");
    	               final LatLng loc = new LatLng(gp.getLatitude(), gp.getLongitude());
    	               final String userLocation = user.getString("UserLocation");
    	               final String fullName = user.getString("FirstName") + " " + user.getString("LastName");
    	               
    	               final Post post = new Post(fullName, user.getEmail(), date.toString(), userLocation, loc, user.getObjectId());
    	               addPost(post);
    	            }
 	            }
    	      } else {
    	         // e != null
    	      }
    	   }
    	});
    }
    
    /**
     * Initializes all other data for the application.
     */
	private void initLayout() {
		this.setContentView(R.layout.layout_whosfree);
		
		masterLayout = (LinearLayout)findViewById(R.id.whosFreeLayout);
		this.mPostLayout = (ListView)this.findViewById(R.id.postListViewGroup);
		this.mPostLayout.setAdapter(mPostAdapter);
		addFriend = (Button)findViewById(R.id.whosFreeAddFriendButton);
		viewRequests = (Button)findViewById(R.id.whosFreeViewRequestsButton);
		username = (EditText)findViewById(R.id.whosFreeAddFriendEditText);
		
		addFriend.setOnClickListener(this);
		viewRequests.setOnClickListener(this);
		
		viewAllToggle = (Button)findViewById(R.id.whosFreeViewAllToggle);
		viewAllToggle.setOnClickListener(this);
		
		
	}

	public void destroyFullPostView() {
	   if (isFullInflated) {
         masterLayout.removeViewAt(1);
         isFullInflated = false;
      }
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
      
      case R.id.whosFreeViewAllToggle:
         
         destroyFullPostView();
         
         if (isViewAll) {
            viewAllToggle.setText(R.string.whosFree_viewAllToggle);
            isViewAll = false;
            this.mPostLayout.setAdapter(mPostAdapter);
         } else {
            viewAllToggle.setText(R.string.whosFree_viewActiveToggle);
            isViewAll = true;
            this.mPostLayout.setAdapter(mUserAdapter);
            
            if (!toastAppeared) {
               Toast.makeText(this, R.string.whosFree_longClickDel, Toast.LENGTH_LONG).show();
               toastAppeared = true;
            }
         }
         break;
      
      case R.id.whosFreeAddFriendButton:
         temp = username.getText().toString();
         getWindow().setSoftInputMode(
               WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         if (!temp.equals("") && SignUpActivity.EMAIL_ADDRESS_PATTERN.matcher(temp).matches()) {
            getWindow().setSoftInputMode(
                  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            DataStore.addParseFriend(temp);
            username.setText("");
            Toast.makeText(this, R.string.whosFree_friendReqSent, Toast.LENGTH_SHORT).show();
         } else {
            Toast.makeText(WhosFree.this, R.string.signUp_enterValidEmail, Toast.LENGTH_SHORT).show();
         }
         break;
         
      case R.id.whosFreeViewRequestsButton:
    	  Intent i = new Intent(this, FriendRequestsActivity.class);
    	  startActivity(i);
    	  break;
         
      case R.id.fullPostHangButton:
         Toast.makeText(this, R.string.illHang_sent, Toast.LENGTH_LONG).show();
         
         final ParseUser curr = DataStore.getCurrentUser();
         
         ParsePush push = new ParsePush();
         push.setChannel("channel" + fullPost.getPost().getUserId());
         push.setMessage(curr.getString("FirstName") + " " + curr.getString("LastName") + " is coming!");
         push.sendInBackground();
         
         break;
         
      case R.id.fullPostCloseButton:
         destroyFullPostView();
         break;
      
      default:
         if (fullPost == null) {
            fullPost = new FullPostView(masterLayout.getContext());
            // Adding this to index 1 of the linearlayout.  This needs to be
            // changed if more gets added to layout_whosfree.xml
            Log.d("WhosFree", "width: " + masterLayout.getWidth());
            masterLayout.addView(fullPost, 1, new LayoutParams(600, 600));
         } else {
            if (!isFullInflated) {
               // This portion is run only if destroyFullPostView() is called
               // before this OnClick occurs
               masterLayout.addView(fullPost, 1, new LayoutParams(600, 600));
            }
         }
         
         fullPost.setPost(((PostView)v).getPost());
         isFullInflated = true;
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
			   
			   destroyFullPostView();

			   if (((PostView)v).isUser()) {
				   DataStore.deleteParseFriend(((PostView)v).getUser());
				   mUserAdapter.notifyDataSetChanged();
			   } else {
				   final String email = ((PostView)v).getPost().getEmail();
				   DataStore.deleteParseFriend(email);
				   removePost(email);
				}
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