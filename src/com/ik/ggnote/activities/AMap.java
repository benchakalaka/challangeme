package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
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
import com.ik.ggnote.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

/**
 * Description: class represents all functionaity which is available on Google
 * map
 * 
 * @author Ihor Karpachev
 */

@EActivity ( R.layout.activity_map ) public class AMap extends ActionBarActivity implements OnMarkerDragListener , OnCameraChangeListener , ConnectionCallbacks , OnConnectionFailedListener , OnMarkerClickListener , LocationListener , OnClickListener {

     // ---------------------------- VIEVS

     // ---------------------------- VARIABLES
     // Google map instance
     public GoogleMap               googleMap;
     // Calendar view
     public static CaldroidFragment dialogCaldroidFragment = new CaldroidFragment();
     // Location client
     private LocationClient         locationClient;
     // marker
     private Marker                 locationMarker;
     // saving dialog
     private NiftyDialogBuilder     dialogBuilder;
     LocationManager                mlocManager;

     @AfterViews void afterViews() {
          // obtaining map object
          googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
          // enable my location button
          googleMap.setMyLocationEnabled(true);
          // create location manager client
          locationClient = new LocationClient(this, this, this);
          // set drag listener as this
          googleMap.setOnMarkerDragListener(this);
          // set this as listener on marker click
          googleMap.setOnMarkerClickListener(this);

          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // You customization
          actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ab_background)));

          actionBar.setIcon(R.drawable.arrowleft);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.ok);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
          ((TextView) actionBar.getCustomView().findViewById(R.id.text1)).setText(R.string.attached_position);
          dialogBuilder = new NiftyDialogBuilder(this);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          ibPinMyLocation(true);
          return super.onOptionsItemSelected(item);
     }

     /**
      * Save my location to current note
      */
     public void ibPinMyLocation(boolean askSave) {

          if ( askSave ) {
               dialogBuilder.withButton1Text(getResources().getString(android.R.string.cancel)).withButton2Text(getResources().getString(R.string.save)).withIcon(R.drawable.marker).withEffect(Effectstype.Slit).withTitle(getResources()
                         .getString(R.string.location_has_not_been_saved)).withMessage(getResources().getString(R.string.do_you_want_to_save_location)).setButton1Click(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                         dialogBuilder.dismiss();
                         onBackPressed();
                    }
               }).setButton2Click(new OnClickListener() {

                    @Override public void onClick(View v) {
                         saveLocation();
                    }

               }).show();
          } else {
               saveLocation();
          }
     }

     private void saveLocation() {
          if ( null != locationMarker ) {
               ActiveRecord.currentNote.location = new ModelLocation(getApplicationContext());
               ActiveRecord.currentNote.location.latitude = locationMarker.getPosition().latitude;
               ActiveRecord.currentNote.location.longitude = locationMarker.getPosition().longitude;
               Utils.showCustomToast(this, R.string.location_has_been_saved, R.drawable.ok);
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
          LatLng location = null;
          // user already pinned location, display marker
          if ( null != ActiveRecord.currentNote.location ) {
               location = new LatLng(ActiveRecord.currentNote.location.latitude, ActiveRecord.currentNote.location.longitude);
               createMarker(location);
               // animate to center of location
               animateCamera(location, 12);
          } else {
               createMarkerOnMyLocation();
          }
     }

     private void createMarker(LatLng position) {
          // set up marker options
          MarkerOptions markerOptions = new MarkerOptions();
          markerOptions.position(position);
          markerOptions.title(getResources().getString(R.string.drag_me));

          // set up marker
          locationMarker = googleMap.addMarker(markerOptions);
          // locationMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
          locationMarker.setDraggable(true);
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
          dialogBuilder.dismiss();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          try {
               mlocManager.removeUpdates(this);
          } catch (Exception ex) {
               ex.printStackTrace();
          }
     }

     /**
      * Trying to connect to location client
      */
     @Override protected void onResume() {
          super.onResume();
          locationClient.connect();
     }

     private void createMarkerOnMyLocation() {
          /* Use the LocationManager class to obtain GPS locations */
          mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
          mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 40, this);

          Location location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          /* check both providers even for lastKnownLocation */
          if ( location == null ) {
               location = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
          }
          if ( location != null ) {

               double latitude = location.getLatitude();
               double longitude = location.getLongitude();

               LatLng currentLatLng = new LatLng(latitude, longitude);
               // animate to center of location
               animateCamera(currentLatLng, 12);

               if ( isConnected(this) ) {

                    locationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.start_position)).position(currentLatLng).title(getResources().getString(R.string.you_are_here)));
                    locationMarker.setDraggable(false);

                    locationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.note_position)).position(currentLatLng).title(getResources().getString(R.string.drag_me)));
                    locationMarker.setDraggable(true);

               }
          }
     }

     public static boolean isConnected(Context context) {
          NetworkInfo ni = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
          return (ni != null && ni.isAvailable() && ni.isConnected());
     }

     @Override public void onMarkerDrag(Marker marker) {
          // ommit it
     }

     @Override public void onMarkerDragEnd(Marker marker) {
          Utils.showStickyNotification(this, R.string.position_has_been_changed, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Override public void onMarkerDragStart(Marker marker) {
          Utils.showStickyNotification(this, R.string.drag_me_to_any_position, AppMsg.STYLE_INFO, 1500);
     }

     @Override public void onLocationChanged(Location location) {
     }

     @Override public void onProviderDisabled(String provider) {
     }

     @Override public void onProviderEnabled(String provider) {

     }

     @Override public void onStatusChanged(String provider, int status, Bundle extras) {
          // TODO Auto-generated method stub

     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    ibPinMyLocation(false);
                    break;
          }

     }
}