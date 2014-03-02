package edu.calpoly.android.imfree;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FullPostView extends LinearLayout {
   
   private TextView mPosterNameTextView;
   private TextView mTimeSlotTextView;
   private TextView mLocationTextView;
   
   private Button mIllHangButton;
   
   private SupportMapFragment fragment;
   private GoogleMap map;

   public FullPostView(Context context) {
      super(context);
      
      ((Activity)context).getLayoutInflater().inflate(R.layout.full_post_view, this);
      
      mPosterNameTextView = (TextView)findViewById(R.id.fullPostPosterName);
      mPosterNameTextView.setText("");
      mTimeSlotTextView = (TextView)findViewById(R.id.fullPostTimeSlot);
      mLocationTextView = (TextView)findViewById(R.id.fullPostLocation);
      mIllHangButton = (Button)findViewById(R.id.fullPostHangButton);
      
      mIllHangButton.setOnClickListener((OnClickListener)context);
      
      fragment = (SupportMapFragment) ((SherlockFragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.postMap);
      if (fragment == null) {
         Log.e("FullPostView", "Null fragment");
      }
   }
   
   public boolean setPost(Post post) {
      
      map = fragment.getMap();

      mPosterNameTextView.setText(post.getPosterName());
      mTimeSlotTextView.setText(post.getTimeSlot());
      mLocationTextView.setText(post.getLocation());
      
      LatLng location = post.getGeoLoc();
      
      map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 15, 0, 0)));
      map.clear();
      map.addMarker(new MarkerOptions().position(location));
      
      UiSettings settings = map.getUiSettings();
      settings.setAllGesturesEnabled(false);
      settings.setZoomControlsEnabled(false);
      
      return true;
   }

}
