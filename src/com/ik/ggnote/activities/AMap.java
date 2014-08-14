package com.ik.ggnote.activities;

import java.io.IOException;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.model.ModelLocation;
import com.ik.ggnote.utils.GPSTracker;
import com.ik.ggnote.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

/**
 * Description: class represents all functionaity which is available on Google
 * map
 * 
 * @author Ihor Karpachev
 */

@EActivity ( R.layout.activity_map ) public class AMap extends FragmentActivity implements OnMarkerDragListener , OnCameraChangeListener , ConnectionCallbacks , OnConnectionFailedListener , OnMarkerClickListener {

     // ---------------------------- VIEVS
     @ViewById ImageButton          ibPinMyLocation;

     // ---------------------------- VARIABLES
     // Google map instance
     public GoogleMap               googleMap;
     // Calendar view
     public static CaldroidFragment dialogCaldroidFragment = new CaldroidFragment();
     // Location client
     private LocationClient         locationClient;
     // geocoder to represents address to human readable string
     private Geocoder               geocoder;
     // marker
     private Marker                 locationMarker;

     @AfterViews void afterViews() {
          // obtaining map object
          googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
          // enable my location button
          googleMap.setMyLocationEnabled(true);
          // create location manager client
          locationClient = new LocationClient(this, this, this);
          // set drag listener as this
          googleMap.setOnMarkerDragListener(this);
          // set up geocoder
          geocoder = new Geocoder(this);
          // set this as listener on marker click
          googleMap.setOnMarkerClickListener(this);
     }

     /**
      * Save my location to current note
      */
     @Click void ibPinMyLocation() {
          ActiveRecord.currentNote.location = new ModelLocation(getApplicationContext());
          ActiveRecord.currentNote.location.latitude = locationMarker.getPosition().latitude;
          ActiveRecord.currentNote.location.longitude = locationMarker.getPosition().longitude;

          try {
               List <Address> result = geocoder.getFromLocation(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude, 1);
               ActiveRecord.currentNote.location.address = String.valueOf(GPSTracker.convertAddressToText(result.get(0)));
          } catch (IOException e) {
               e.printStackTrace();
          }

          onBackPressed();
     }

     /**
      * Animate camera to new map position with given zoom
      * 
      * @param position
      * @param zoom
      */
     private void animateCamera(LatLng position, float zoom) {
          googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom), 2000, null);
     }

     @Override public void onCameraChange(CameraPosition camera) {
          // map has been downloaded, refresh map and destroy listener
          googleMap.setOnCameraChangeListener(null);
     }

     @Override public void onConnectionFailed(ConnectionResult connectionResult) {
          // TODO SET UT IF FAILED
     }

     @Override public void onDisconnected() {
          // TODO SET UT IF FAILED
     }

     @Override public void onConnected(Bundle bundle) {
          Location loc = locationClient.getLastLocation();
          if ( null == loc ) {
               Utils.showCustomToast(this, "Cannot obtain current location", R.drawable.calendar);
               return;
          }
          try {
               List <Address> result = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
               LatLng location = new LatLng(loc.getLatitude(), loc.getLongitude());
               // animate to center of UK
               animateCamera(location, 12);
               createMarker(location, result.get(0));
          } catch (IOException e) {
               e.printStackTrace();
               Utils.showCustomToast(AMap.this, "PROBLEEEMS", R.drawable.scream);
               onBackPressed();
          }
     }

     private void createMarker(LatLng position, Address address) {
          // set up marker options
          MarkerOptions markerOptions = new MarkerOptions();
          markerOptions.position(position);
          markerOptions.title(String.valueOf(GPSTracker.convertAddressToText(address)));
          markerOptions.snippet("Drag me");

          // set up marker
          locationMarker = googleMap.addMarker(markerOptions);
          locationMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
          locationMarker.setDraggable(true);
          locationMarker.showInfoWindow();
     }

     @Override public boolean onMarkerClick(Marker marker) {
          return false;
     }

     /**
      * OnPause - disconnect from location client
      */
     @Override protected void onPause() {
          super.onPause();
          locationClient.disconnect();
     }

     /**
      * Trying to connect to location client
      */
     @Override protected void onResume() {
          super.onResume();
          locationClient.connect();
     }

     @Override public void onMarkerDrag(Marker marker) {
          // ommit it
     }

     @Override public void onMarkerDragEnd(Marker marker) {
          Utils.showCustomToast(this, "Marker: " + marker.getPosition().latitude, R.drawable.scream);
     }

     @Override public void onMarkerDragStart(Marker marker) {
          Utils.showCustomToast(this, "Drag me somewhere", R.drawable.scream);

          marker.setTitle("Drag me somewhere");
          marker.hideInfoWindow();
          marker.showInfoWindow();
          /*
           * try {
           * List <Address> result = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
           * if ( null != result && result.size() > 0 ) {
           * marker.setTitle(String.valueOf(GPSTracker.convertAddressToText(result.get(0))));
           * marker.hideInfoWindow();
           * marker.showInfoWindow();
           * }
           * } catch (IOException e) {
           * e.printStackTrace();
           * }
           */
     }
}