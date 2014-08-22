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
import android.widget.Toast;

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
import com.ik.ggnote.utils.GPSTracker;
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
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.attach);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
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
               final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);

               dialogBuilder.withButton1Text("Cancel").withButton2Text("Save").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Location has not been saved.").withMessage("Do you want to save location?").setButton1Click(new View.OnClickListener() {
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
          LatLng location = null;
          Location loc = locationClient.getLastLocation();
          if ( null == loc ) {
               // TODO obtain location with help of location service
               Utils.showCustomToast(this, "Cannot obtain current location", R.drawable.scream);
               createMarkerWithLocation();
               return;
          }
          try {
               location = new LatLng(loc.getLatitude(), loc.getLongitude());
               List <Address> result = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

               // animate to center of UK
               animateCamera(location, 12);
               createMarker(location, result.get(0));
               // createMarkerWithLocation();
          } catch (IOException e) {
               e.printStackTrace();
               Utils.showCustomToast(AMap.this, "Problem obtain address   " + e.getMessage(), R.drawable.scream);
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
               markerOptions.title("Unknown address");
          }
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

     private void createMarkerWithLocation() {
          /* Use the LocationManager class to obtain GPS locations */
          LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
          mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 40, this);

          Location location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          /* check both providers even for lastKnownLocation */
          if ( location == null ) {
               location = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
          }
          if ( location != null ) {

               double latitude = location.getLatitude();
               double longitude = location.getLongitude();

               LatLng currentLatLng = new LatLng(latitude, longitude);

               if ( isConnected(this) ) {
                    // List <Address> addresses = getInfos(latitude, longitude);
                    String address = "address"; // addresses.get(0).getAddressLine(0);

                    String city = "city";// addresses.get(0).getAddressLine(1);
                    String country = "country";// addresses.get(0).getAddressLine(2);
                    Toast.makeText(this, country + ", " + city + ", " + address, Toast.LENGTH_SHORT).show();

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(city).snippet(address).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
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

     @Override public void onLocationChanged(Location location) {
          // TODO Auto-generated method stub

     }

     @Override public void onProviderDisabled(String provider) {
          // TODO Auto-generated method stub

     }

     @Override public void onProviderEnabled(String provider) {
          // TODO Auto-generated method stub

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