package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class DataStore {
   
   private static ArrayList<String> friendsList = new ArrayList<String>();
   
   private static ParseUser currentUser;
   
   public static void clearData() {
      friendsList.clear();
      currentUser = null;
   }
   
   public static void addParseFriend(final String friend) {
	   // Prevent users from adding duplicates
	   if (friendsList.contains(friend))
		   return;
	   
	   ParseQuery<ParseUser> query = ParseUser.getQuery();
	   query.whereEqualTo("email", friend);
	   
	   query.findInBackground(new FindCallback<ParseUser>() {
		   public void done(List<ParseUser> objects, ParseException e) {
			   if (e == null) {
				   // Emails are unique so first element is the desired user
				   if (!objects.isEmpty()) {
					   ParseUser friendUser = objects.get(0);
					   friendUser.addUnique("FriendRequests", currentUser.getEmail());
				   }
			   } else {
				   // Something went wrong.
			   }
		   }
	   });
   }
   
   public static void trueAddParseFriend(String friend) {
	   friendsList.add(friend);
	   currentUser.addUnique("Friends", friend);
	   currentUser.saveInBackground();
   }
   
   public static List<String> getParseFriends() {
      return friendsList;
   }
   
   public static void removeParseFriend(String friend) {
      boolean res = friendsList.remove(friend);
      Log.d("DataStore", "Remove called " + res);
      currentUser.removeAll("Friends", Arrays.asList(friend));
      currentUser.saveInBackground();
   }
   
   public static void setCurrentUser(ParseUser user) {
      currentUser = user;
   }
   
   public static ParseUser getCurrentUser() {
      return currentUser;
   }
   
   public static void acceptFriendRequest(String email) {
	   DataStore.trueAddParseFriend(email);
	   ParseQuery<ParseUser> query = ParseUser.getQuery();
	   query.whereEqualTo("email", email);
	   
	   query.findInBackground(new FindCallback<ParseUser>() {
		   public void done(List<ParseUser> objects, ParseException e) {
			   if (e == null) {
				   // Emails are unique so first element is the desired user
				   ParseUser friendUser = objects.get(0);
				   friendUser.addUnique("Friends", currentUser.getEmail());
			   } else {
				   // Something went wrong.
			   }
		   }
	   });
	   removeFriendRequest(email);
   }
   
   public static void removeFriendRequest(String email) {
	   currentUser.removeAll("FriendRequests", Arrays.asList(email));
	   currentUser.saveInBackground();
   }
}
