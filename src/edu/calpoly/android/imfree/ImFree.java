package edu.calpoly.android.imfree;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_imfree);
      
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
                     user.put("Location", new ParseGeoPoint(0,0));
                     user.put("UserLocation", mLocation.getText().toString());
                     Date temp = new Date();
                     temp.setHours(mTimePicker.getCurrentHour());
                     temp.setMinutes(mTimePicker.getCurrentMinute());
                     user.put("TimeFree", temp);
                     
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

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.im_free, menu);
      return true;
   }

}
