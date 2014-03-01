package edu.calpoly.android.imfree;

import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

public class Post {
	private String mPosterName;
	private String mTimeSlot;
	private String mLocation;
	private LatLng mGeoLoc;
	
	private FragmentManager fragmentManager;
	
	public Post(String name, String timeSlot, String location, LatLng geoLoc, FragmentManager sfm) {
		this.mPosterName = name;
		this.mTimeSlot = timeSlot;
		this.mLocation = location;
		this.mGeoLoc = geoLoc;
		fragmentManager = sfm;
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
	
	public FragmentManager getFragmentManager() {
	   return fragmentManager;
	}
}
