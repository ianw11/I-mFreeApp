package edu.calpoly.android.imfree;

import com.google.android.gms.maps.model.LatLng;

public class Post {
	private String mPosterName;
	private String mTimeSlot;
	private String mLocation;
	private LatLng mGeoLoc;
	private String email;
	private String userId;
	
	public Post(String name, String email, String timeSlot, String location, LatLng geoLoc, String uID) {
		this.mPosterName = name;
		this.mTimeSlot = timeSlot;
		this.mLocation = location;
		this.mGeoLoc = geoLoc;
		this.email = email;
		this.userId = uID;
	}
	
	public String getPosterName() {
		return this.mPosterName;
	}
	
	public String getTimeSlot() {
		return this.mTimeSlot;
	}
	
	public String getLocation() {
		return this.mLocation;
	}
	
	public LatLng getGeoLoc() {
		return this.mGeoLoc;
	}
	
	public String getEmail() {
	   return this.email;
	}
	
	public String getUserId() {
	   return userId;
	}
}
