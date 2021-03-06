package edu.calpoly.android.imfree;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class ImFree extends BaseActivity implements OnClickListener, LocationListener {
   
   private TimePicker mTimePicker;
   private Button mPost;
   private Button mEdit;
   private Button mCancel;
   private EditText mLocation;
   private TextView mFreeUntilTime;
   
   private LocationManager locManager;
   
   private static final int MIN_TIME_CHANGE = 3000;
   private static final int MIN_DISTANCE_CHANGE = 3;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_imfree);
      
      initLocationData();
      
      setActivityName("ImFree");
      
      initLayout(false);
      initOnClickListeners();
      
      //PushService.setDefaultPushCallback(this,  ImFree.class);
      // Subscribe every user to their own "channel"
      PushService.subscribe(this, "channel" + DataStore.getCurrentUser().getObjectId(), ImFree.class);
   }
   
   @Override
   protected void onResume() {
	   super.onResume();
	   initLayout(true);
	   DataStore.removeActivityFromCallStack(this);
	   if (mLocation.getText().toString().equals("")) {
		   mLocation.setText(DataStore.getSavedLocText());
	   }
	   Integer hour = DataStore.getSavedHour();
	   Integer minute = DataStore.getSavedMinute();
	   if (hour != null)
		   mTimePicker.setCurrentHour(hour);
	   if (minute != null)
		   mTimePicker.setCurrentMinute(minute);
   }
   
   @Override
   public void onPause() {
	   DataStore.setSavedLocText(mLocation.getText().toString());
	   DataStore.setSavedHour(mTimePicker.getCurrentHour());
	   DataStore.setSavedMinute(mTimePicker.getCurrentMinute());
	   super.onPause();
   }
   
   private void initLayout(boolean resuming) {
	   if (!resuming) {
		   mTimePicker = (TimePicker)findViewById(R.id.freeTimePicker);
		   mPost = (Button)findViewById(R.id.freePostButton);
		   mEdit = (Button)findViewById(R.id.freeEditButton);
		   mCancel = (Button)findViewById(R.id.freeCancelButton);
		   mLocation = (EditText)findViewById(R.id.freeWhereEditText);
		   mFreeUntilTime = (TextView)findViewById(R.id.freeUntilTextView);
	   }
      
	   ParseUser user = DataStore.getCurrentUser();
	   Date mostRecentPostDate = user.getDate("TimeFree");
	   if (mostRecentPostDate != null && mostRecentPostDate.after(new Date())) {
		   setActivePostLayout(user, false);
	   }
	   else {
		   setDefaultPostLayout(user, false);
	   }
   }
   
   // Sets uneditable views based on the user's active post
   private void setActivePostLayout(ParseUser user, boolean fromPost) {
	   if (!fromPost)
		   mLocation.setText(user.getString("UserLocation"));
	   String curLocText = mLocation.getText().toString();
	   if (curLocText.equals("") || curLocText.equals(R.string.where))
		   mLocation.setText(R.string.imFree_defaultActiveLocation);
	   mLocation.setEnabled(false);
	   mLocation.setFocusable(false);
	   mLocation.setFocusableInTouchMode(false);
	   
	   mTimePicker.setEnabled(false);
	   mTimePicker.setFocusable(false);
	   mTimePicker.setVisibility(View.INVISIBLE);
	   
	   Date date = null;
	   String timeOfDay = "AM";
	   Integer hour;
	   Integer minutes;
	   
	   if (!fromPost) {
		   date = user.getDate("TimeFree");
		   hour = date.getHours();
		   minutes = date.getMinutes();
	   }
	   else {
		   hour = mTimePicker.getCurrentHour();
		   minutes = mTimePicker.getCurrentMinute();
	   }
	   if (hour > 12) {
		   hour = hour - 12;
		   timeOfDay = "PM";
	   }
	   String minuteStr = minutes < 10 ? "0" + minutes.toString() : minutes.toString();
	   mFreeUntilTime.setText(hour.toString() + ":" + minuteStr + timeOfDay);
	   mFreeUntilTime.setVisibility(View.VISIBLE);
	   
	   mPost.setEnabled(false);
	   mPost.setVisibility(View.INVISIBLE);
	   
	   mEdit.setEnabled(true);
	   mEdit.setVisibility(View.VISIBLE);
	   
	   mCancel.setEnabled(true);
	   mCancel.setVisibility(View.VISIBLE);
   }
   
   // Sets views based on a fresh post or active post to be edited
   private void setDefaultPostLayout(ParseUser user, boolean fromEdit) {
	   if (fromEdit) {
		   String mostRecentLocation = user.getString("UserLocation");
		   mLocation.setText(mostRecentLocation);
	   }
	   else {
		   mLocation.setText("");
	   }
	   mLocation.setEnabled(true);
	   mLocation.setFocusable(true);
	   mLocation.setFocusableInTouchMode(true);
	   
	   mTimePicker.setEnabled(true);
	   mTimePicker.setFocusable(true);
	   mTimePicker.setVisibility(View.VISIBLE);
	   
	   if (!fromEdit) {
		   Calendar cal = Calendar.getInstance();
		   mTimePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		   mTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
	   }
	   
	   mFreeUntilTime.setVisibility(View.INVISIBLE);
	   mFreeUntilTime.setText("");
	   
	   mPost.setEnabled(true);
	   mPost.setVisibility(View.VISIBLE);
	   
	   mEdit.setEnabled(false);
	   mEdit.setVisibility(View.INVISIBLE);
	   
	   mCancel.setEnabled(false);
	   mCancel.setVisibility(View.INVISIBLE);
   }
   
   private void initLocationData() {
      locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ImFree.MIN_TIME_CHANGE,ImFree.MIN_DISTANCE_CHANGE,this);
   }
   
   private void initOnClickListeners() {
	   mPost.setOnClickListener(this);
	   
	   mEdit.setOnClickListener(this);
	   
	   mCancel.setOnClickListener(this);
   }
   
   private void lockButtons() {
      mPost.setClickable(false);
   }
   
   private void unlockButtons() {
      mPost.setClickable(true);
   }

   @Override
   public void onClick(View v) {
	  ParseUser user = DataStore.getCurrentUser();
	   
      switch (v.getId()) {
      
      case R.id.freeEditButton:
    	  setDefaultPostLayout(user, true);
    	  break;
         
      case R.id.freePostButton:
         //Upload to Server
         lockButtons();

         user.put("UserLocation", mLocation.getText().toString());
         Date temp = new Date();
         temp.setHours(mTimePicker.getCurrentHour());
         temp.setMinutes(mTimePicker.getCurrentMinute());
         user.put("TimeFree", temp);
         
         Location currLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         if (currLoc != null) {
            ParseGeoPoint geoPoint = new ParseGeoPoint(currLoc.getLatitude(), currLoc.getLongitude());
            user.put("Location", geoPoint);
            // Remove location updates after posting to save battery
            locManager.removeUpdates(ImFree.this);
         }
         else {
            Toast.makeText(ImFree.this, R.string.imFree_gpsNote, Toast.LENGTH_LONG).show();
            user.put("Location", new ParseGeoPoint());
         }
         
         user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
               if (e != null) {
                  // Unsuccessful save
                  Log.e("ImFree", e.toString());
                  Toast.makeText(ImFree.this, "Save Unsuccessful", Toast.LENGTH_SHORT).show();
               }
            }
         });
         
         setActivePostLayout(user, true);
         unlockButtons();
         break;
      
      case R.id.freeCancelButton:
    	  user.put("TimeFree", new Date());
          setDefaultPostLayout(user, false);
          
          user.saveInBackground(new SaveCallback() {
             @Override
             public void done(ParseException e) {
                if (e == null) {
                   // Nothing to do for successful cancel
                } else {
                   // Unsuccessful cancel
                   Log.e("ImFree", e.toString());
                   Toast.makeText(ImFree.this, "Post could not be removed.", Toast.LENGTH_SHORT).show();
                }
             }
          });
          break;
      
      default:
         break;
      }
   }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
