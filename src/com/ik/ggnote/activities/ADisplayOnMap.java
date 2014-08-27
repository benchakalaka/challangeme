package com.ik.ggnote.activities;

import java.io.IOException;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.appmsg.AppMsg;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ik.ggnote.R;
import com.ik.ggnote.utils.GPSTracker;
import com.ik.ggnote.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

/**
 * Description: class represents all functionaity which is available on Google
 * map
 * 
 * @author Ihor Karpachev
 */

@EActivity ( R.layout.activity_display_on_map ) public class ADisplayOnMap extends ActionBarActivity implements OnCameraChangeListener , ConnectionCallbacks , OnConnectionFailedListener , OnMarkerClickListener , LocationListener {

     // ---------------------------- VIEVS

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
          // set up geocoder
          geocoder = new Geocoder(this);
          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // customization
          actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ab_background)));

          actionBar.setIcon(R.drawable.arrowleft);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setVisibility(View.INVISIBLE);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     /**
      * Animate camera to new map position with given zoom
      * 
      * @param position
      * @param zoom
      */
     private void animateCamera(LatLng position, float zoom) {
          if ( null != position ) {
               googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom), 2000, null);
          }
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
          if ( null == ANoteDetails.note.location ) {
               Utils.showStickyNotification(this, R.string.there_is_no_location, AppMsg.STYLE_INFO, 1000);
               return;
          }
          LatLng location = null;
          try {
               location = new LatLng(ANoteDetails.note.location.latitude, ANoteDetails.note.location.longitude);
               List <Address> result = geocoder.getFromLocation(ANoteDetails.note.location.latitude, ANoteDetails.note.location.longitude, 1);

               // animate to center of UK
               animateCamera(location, 12);
               createMarker(location, result.get(0));
               // createMarkerWithLocation();
          } catch (IOException e) {
               e.printStackTrace();
               Utils.showStickyNotification(this, R.string.problem_obtaining_address, AppMsg.STYLE_ALERT, 1000);
               animateCamera(location, 12);
               createMarker(location, null);
          }
     }

     private void createMarker(LatLng position, Address address) {
          // set up marker options
          MarkerOptions markerOptions = new MarkerOptions();
          markerOptions.position(position);
          if ( null != address ) {
               markerOptions.title(String.valueOf(GPSTracker.convertAddressToText(address)));
          } else {
               markerOptions.title(getResources().getString(R.string.unknown_address));
          }
          // set up marker
          locationMarker = googleMap.addMarker(markerOptions);
          locationMarker.setDraggable(false);
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

     public static boolean isConnected(Context context) {
          NetworkInfo ni = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
          return (ni != null && ni.isAvailable() && ni.isConnected());
     }

     @Override public void onLocationChanged(Location location) {
     }

     @Override public void onProviderDisabled(String provider) {
     }

     @Override public void onProviderEnabled(String provider) {
     }

     @Override public void onStatusChanged(String provider, int status, Bundle extras) {
     }
}