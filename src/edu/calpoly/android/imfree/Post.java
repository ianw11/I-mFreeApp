package edu.calpoly.android.imfree;

import com.google.android.gms.maps.model.LatLng;

public class Post {
	private String mPosterName;
	private String mTimeSlot;
	private String mLocation;
	private LatLng mGeoLoc;
	
	public Post(String name, String timeSlot, String location, LatLng geoLoc) {
		this.mPosterName = name;
		this.mTimeSlot = timeSlot;
		this.mLocation = location;
		this.mGeoLoc = geoLoc;
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
}
