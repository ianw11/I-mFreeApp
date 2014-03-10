package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class DataStore {
   
   private static ArrayList<String> friendsList = new ArrayList<String>();
   private static List<String> mRequests = new ArrayList<String>();
   
   private static ParseUser currentUser;
   
   public static void clearData() {
      friendsList.clear();
      currentUser = null;
   }
   
   public static void setRequests(List<Object> list) {
	   mRequests.clear();
	   for (Object s : list) {
		   mRequests.add((String)s);
	   }
   }
   
   public static List<String> getRequests() {
	   return mRequests;
   }
   
   public static void addParseFriend(final String friend) {
	   // Prevent users from adding duplicates
	   if (friendsList.contains(friend))
		   return;
	   
	   ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
	   query.findInBackground(new FindCallback<ParseObject>() {
		   public void done(List<ParseObject> reqList, ParseException e) {
			   if (e == null) {
				   // Emails are unique so first element is the desired user
				   for (ParseObject obj : reqList) {
					   if (obj.getString("OwnedBy").equals(friend)) {
						   obj.addUnique("Requests", currentUser.getEmail());
						   obj.saveInBackground();
					   }
				   }
			   } else {
				   Log.d("DataStore", "Error: " + e.toString());
			   }
		   }
	   });
   }
   
   public static void trueAddParseFriend(String friend) {
      if (friendsList.contains(friend))
         return;
      
	   friendsList.add(friend);
	   currentUser.addUnique("Friends", friend);
	   currentUser.saveInBackground();
   }
   
   public static void deleteParseFriend(final String friend) {
	   // Prevent users from deleting someone who isn't their friend
	   if (!friendsList.contains(friend))
		   return;
	   
	   DataStore.removeParseFriend(friend);
	   
	   ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
	   query.findInBackground(new FindCallback<ParseObject>() {
		   public void done(List<ParseObject> reqList, ParseException e) {
			   if (e == null) {
				   // Emails are unique so first element is the desired user
				   for (ParseObject obj : reqList) {
					   if (obj.getString("OwnedBy").equals(friend)) {
						   obj.addUnique("DeletedFriends", currentUser.getEmail());
						   obj.saveInBackground();
					   }
				   }
			   } else {
				   Log.d("DataStore", "Error: " + e.toString());
			   }
		   }
	   });
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
	  if (currentUser == null) {
		  currentUser = ParseUser.getCurrentUser();
	  }
      return currentUser;
   }
   
   public static void acceptFriendRequest(final String email) {
	   DataStore.trueAddParseFriend(email);
	   ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
	   query.findInBackground(new FindCallback<ParseObject>() {
		   public void done(List<ParseObject> reqList, ParseException e) {
			   if (e == null) {
				   // Emails are unique so first element is the desired user
				   for (ParseObject obj : reqList) {
					   if (obj.getString("OwnedBy").equals(email)) {
						   obj.addUnique("AcceptedRequests", currentUser.getEmail());
						   obj.saveInBackground();
					   }
				   }
			   } else {
				   // Something went wrong.
			   }
		   }
	   });
	   removeFriendRequest(email);
   }
   
   public static void removeFriendRequest(final String email) {
	   ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
	   query.findInBackground(new FindCallback<ParseObject>() {
		   public void done(List<ParseObject> reqList, ParseException e) {
			   if (e == null) {
				   for (ParseObject obj : reqList) {
					   if (obj.getString("OwnedBy").equals(currentUser.getEmail())) {
						   obj.removeAll("Requests", Arrays.asList(email));
						   obj.saveInBackground();
					   }
				   }
	           } else {
	        	   Log.d("score", "Error: " + e.getMessage());
	           }
	       }
	   });
   }
}
