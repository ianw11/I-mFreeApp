package edu.calpoly.android.imfree;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FullPostView extends RelativeLayout {
   
   private TextView mPosterNameTextView;
   private TextView mTimeSlotTextView;
   private TextView mLocationTextView;
   
   private Button mIllHangButton;
   private Button closeButton;
   
   private SupportMapFragment fragment;
   private GoogleMap map;
   
   private Post smallerPost;
   
   private WhosFree parentActivity;

   public FullPostView(Context context) {
      super(context);
      
      ((Activity)context).getLayoutInflater().inflate(R.layout.full_post_view, this);
      
      parentActivity = (WhosFree)context;
      
      mPosterNameTextView = (TextView)findViewById(R.id.fullPostPosterName);
      mPosterNameTextView.setText("");
      mTimeSlotTextView = (TextView)findViewById(R.id.fullPostTimeSlot);
      mLocationTextView = (TextView)findViewById(R.id.fullPostLocation);
      mIllHangButton = (Button)findViewById(R.id.fullPostHangButton);
      closeButton = (Button)findViewById(R.id.fullPostCloseButton);
      
      /* This is allowing WhosFree to be the handler of a click */
      mIllHangButton.setOnClickListener(parentActivity);
      closeButton.setOnClickListener(parentActivity);
      
      fragment = (SupportMapFragment) (parentActivity.getSupportFragmentManager().findFragmentById(R.id.postMap));
      if (fragment == null) {
         Log.e("FullPostView", "Null fragment");
      }
   }
   
   public boolean setPost(Post post) {
      
      smallerPost = post;
      
      map = fragment.getMap();

      mPosterNameTextView.setText(post.getPosterName());
      mTimeSlotTextView.setText("Free until " + post.getTimeSlot());
      mLocationTextView.setText("At " + post.getLocation());
      
      LatLng location = post.getGeoLoc();
      
      
      if (location.latitude == 0 && location.longitude == 0) {
         mLocationTextView.setText(R.string.whosFree_noLocation);
         map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 1, 0, 0)));
         map.clear();
      } else {
         map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 15, 0, 0)));
         map.clear();
         map.addMarker(new MarkerOptions().position(location));
      }
      
      UiSettings settings = map.getUiSettings();
      settings.setAllGesturesEnabled(false);
      settings.setZoomControlsEnabled(false);
      
      
      return true;
   }
   
   public Post getPost() {
      return smallerPost;
   }

}
