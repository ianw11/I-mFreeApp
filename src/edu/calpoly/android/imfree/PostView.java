package edu.calpoly.android.imfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostView extends LinearLayout implements OnClickListener {

	private TextView mPosterNameTextView;
	private TextView mTimeSlotTextView;
	private TextView mLocationTextView;
	private GoogleMap mMap;
	private Button mIllHangButton;
	
	private Post mPost;
	
	public PostView(Context context, Post post) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.post_view, this, true);
		
		this.mPosterNameTextView = (TextView)findViewById(R.id.posterNameTextView);
		this.mTimeSlotTextView = (TextView)findViewById(R.id.timeSlotTextView);
		this.mLocationTextView = (TextView)findViewById(R.id.locationTextView);
		
		this.mIllHangButton = (Button)findViewById(R.id.illHangButton);
		this.mIllHangButton.setOnClickListener(this);
		
		this.setPost(post);
	}

	public void setPost(Post post) {
		this.mPost = post;
		this.mPosterNameTextView.setText(mPost.getPosterName());
		this.mTimeSlotTextView.setText(mPost.getTimeSlot());
		this.mLocationTextView.setText(mPost.getLocation());
		
		mMap = ((SupportMapFragment)(post.getFragmentManager().findFragmentById(R.id.postMap))).getMap();
      mMap.moveCamera(CameraUpdateFactory.newLatLng(post.getGeoLoc()));
      mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
		mMap.addMarker(new MarkerOptions().position(mPost.getGeoLoc()));
		
		UiSettings settings = mMap.getUiSettings();
		settings.setZoomControlsEnabled(false);
		settings.setAllGesturesEnabled(false);
	}
	
	@Override
	public void onClick(View v) {
	   switch(v.getId()) {
	   case R.id.illHangButton:
	      Toast.makeText(getContext(), this.mPost.getPosterName() + " has been notified.", Toast.LENGTH_SHORT).show();
	      break;
	   default:
	      break;
	   }
	}
}
