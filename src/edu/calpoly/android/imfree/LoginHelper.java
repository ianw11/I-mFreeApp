package edu.calpoly.android.imfree;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginHelper extends LogInCallback {
   
   private Activity context;
   private boolean isFromSplash;
   private boolean isNewUser;

   public LoginHelper(Context c) {
     this(c, false);
   }
   
   public LoginHelper(Context c, boolean fromSplash) {
      this(c, fromSplash, false);
   }
   
   public LoginHelper(Context c, boolean fromSplash, boolean newUser) {
      context = (Activity)c;
      isFromSplash = fromSplash;
      isNewUser = newUser;
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
               DataStore.trueAddParseFriend((String)f);
            }
         }
         
         if (isNewUser) {
            ParseObject friendRequests = new ParseObject("FriendRequests");
            friendRequests.put("OwnedBy", user.getEmail());
            friendRequests.saveInBackground();
            Log.d("LoginHelper", "Created new FriendRequests object");
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
            final LoginActivity test = (LoginActivity) context;
            test.resetViews();
            Log.d("ParseException", e.toString());
         }
      }

   }

}
