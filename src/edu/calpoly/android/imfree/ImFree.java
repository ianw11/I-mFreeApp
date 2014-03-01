package edu.calpoly.android.imfree;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ImFree extends Activity {
   
   private String musername;
   private String mObjectId;
   
   private TimePicker mTimePicker;
   private Button mPost;
   private EditText mLocation;
   private Button navWhosFree;
   
   private Button navLogout;
   
   private LocationManager locManager;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_imfree);
      
      initLocationData();
      
      Intent i = getIntent();
      musername = i.getStringExtra("ParseUser");
      mObjectId = i.getStringExtra("ParseObjectId");
      
      initLayout();
      
      mPost.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View v) {
            //Upload to Server
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(mObjectId, new GetCallback<ParseUser>() {

               @Override
               public void done(ParseUser user, ParseException e) {
                  if (e == null) {
                     user.put("UserLocation", mLocation.getText().toString());
                     Date temp = new Date();
                     temp.setHours(mTimePicker.getCurrentHour());
                     temp.setMinutes(mTimePicker.getCurrentMinute());
                     user.put("TimeFree", temp);
                     
                     Location currLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                     ParseGeoPoint geoPoint = new ParseGeoPoint(currLoc.getLatitude(), currLoc.getLongitude());
                     user.put("Location", geoPoint);
                     
                     user.saveInBackground();
                  }
               }
               
            });
         }
         
      });
      
      navWhosFree.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View v) {
            Intent i = new Intent(ImFree.this, WhosFree.class);
            i.putExtra("ParseUser", musername);
            startActivity(i);
         }
         
      });
      
      navLogout.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View v) {
            Intent i = new Intent(ImFree.this, LoginActivity.class);
            i.putExtra("intent", "ImFree");
            startActivity(i);
            finish();
         }
         
      });
   }
   
   private void initLayout() {
      mTimePicker = (TimePicker)findViewById(R.id.freeTimePicker);
      mPost = (Button)findViewById(R.id.freePostButton);
      mLocation = (EditText)findViewById(R.id.freeWhereEditText);
      
      navWhosFree = (Button)findViewById(R.id.freeWhosFreeButton);
      navLogout = (Button)findViewById(R.id.freeLogoutButton);
   }
   
   private void initLocationData() {
      locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
   }

}
