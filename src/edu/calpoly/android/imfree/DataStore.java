package edu.calpoly.android.imfree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.parse.ParseUser;


public class DataStore {
   
   private static ArrayList<String> friendsList = new ArrayList<String>();
   
   private static ParseUser currentUser;
   
   public static void clearData() {
      friendsList.clear();
      currentUser = null;
   }
   
   public static void addParseFriend(String friend) {
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
	   DataStore.addParseFriend(email);
	   currentUser.removeAll("FriendRequests", Arrays.asList(email));
	   currentUser.saveInBackground();
	   
   }
   
   public static void declineFriendRequest(String email) {
	   
   }
}
