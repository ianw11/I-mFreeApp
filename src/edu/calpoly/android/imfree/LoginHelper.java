package edu.calpoly.android.imfree;

import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginHelper extends LogInCallback {
   
   private Activity context;
   private boolean isFromSplash;

   public LoginHelper(Context c) {
     this(c, false);
   }
   
   public LoginHelper(Context c, boolean fromSplash) {
      context = (Activity)c;
      isFromSplash = fromSplash;
   }

   @Override
   public void done(ParseUser user, ParseException e) {
      if (user != null) {
         DataStore.setCurrentUser(user);
         
         
         List<Object> arr = user.getList("Friends");
         if (arr != null) {
            Log.d("JSONArray", arr.toString());
            //DataStore.addMultipleParseFriends((List<String>)user.getList("Friends"));
            for (Object f : arr) {
               Log.d("friends", (String)f);
               DataStore.addParseFriend((String)f);
            }
         }
         
         Intent i = new Intent(context, ImFree.class);
         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         i.putExtra("ParseUser", user.getUsername());
         i.putExtra("ParseObjectId", user.getObjectId());
         context.startActivity(i);
         context.finish();
      } else {
         if (isFromSplash) {
            Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show();
            Log.d("ParseException", e.toString());
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
            context.finish();
         } else {
            Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show();
            Log.d("ParseException", e.toString());
         }
      }

   }

}
