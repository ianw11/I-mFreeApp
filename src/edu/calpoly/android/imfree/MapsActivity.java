package edu.calpoly.android.imfree;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends SherlockFragmentActivity {
   
   private GoogleMap mMap;
   
   private LatLng loc;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_map);
      
      FragmentManager fm = getSupportFragmentManager();
      mMap = ((SupportMapFragment)fm.findFragmentById(R.id.map)).getMap();
      
      loc = getIntent().getParcelableExtra("latlng");
      
      MarkerOptions mo = new MarkerOptions();
      mo.position(loc).title("Location").snippet("Snippet");
      
      Marker marker = mMap.addMarker(mo);
      
      //marker.setPosition(loc);
      CameraUpdate camUpdate = CameraUpdateFactory.newLatLng(loc);
      mMap.moveCamera(camUpdate);
      camUpdate = CameraUpdateFactory.zoomTo(15);
      mMap.moveCamera(camUpdate);
      
      
   }
}
