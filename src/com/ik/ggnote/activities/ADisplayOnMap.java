package com.ik.ggnote.activities;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devspark.appmsg.AppMsg;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.ik.ggnote.R;
import com.ik.ggnote.utils.GMapV2Direction;
import com.ik.ggnote.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.roomorama.caldroid.CaldroidFragment;

/**
 * Description: class represents all functionaity which is available on Google
 * map
 * 
 * @author Ihor Karpachev
 */

@EActivity ( R.layout.activity_display_on_map ) public class ADisplayOnMap extends ActionBarActivity implements OnMarkerDragListener , OnCameraChangeListener , ConnectionCallbacks , OnConnectionFailedListener , OnMarkerClickListener , LocationListener {

     // ---------------------------- VIEVS
     @ViewById ImageView                                ivBike , ivCar , ivWalking , ivMap , ivRouteType , ivCloseRouteInfo;
     @ViewById TextView                                 twDistance , twStartAddress , twEndAddress , twDuration;
     @ViewById LinearLayout                             llBottomMapMenuDescritption;

     // ---------------------------- VARIABLES
     // Google map instance
     public GoogleMap                                   googleMap;
     // Calendar view
     public static CaldroidFragment                     dialogCaldroidFragment             = new CaldroidFragment();
     // Location client
     private LocationClient                             locationClient;
     // marker
     private Marker                                     locationMarker , currentLocation;
     // for displaying path on gmap
     private final GMapV2Direction                      routeDrawer                        = new GMapV2Direction();
     // display progress
     ProgressDialog                                     progress;

     AnimatorListener                                   animationListenerInvisibleInTheEnd = new AnimatorListener() {

                                                                                                @Override public void onAnimationStart(Animator arg0) {
                                                                                                     // TODO Auto-generated method stub

                                                                                                }

                                                                                                @Override public void onAnimationRepeat(Animator arg0) {
                                                                                                     // TODO Auto-generated method stub

                                                                                                }

                                                                                                @Override public void onAnimationEnd(Animator arg0) {
                                                                                                     llBottomMapMenuDescritption.setVisibility(View.GONE);
                                                                                                }

                                                                                                @Override public void onAnimationCancel(Animator arg0) {
                                                                                                     // TODO Auto-generated method stub

                                                                                                }
                                                                                           };

     private final GoogleMap.OnMyLocationChangeListener myLocationChangeListener           = new GoogleMap.OnMyLocationChangeListener() {

                                                                                                @Override public void onMyLocationChange(Location location) {
                                                                                                     if ( null != currentLocation ) {
                                                                                                          currentLocation.remove();
                                                                                                     }
                                                                                                     LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                                                                                                     currentLocation = googleMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.start_position)));
                                                                                                     currentLocation.showInfoWindow();
                                                                                                }
                                                                                           };

     @AfterViews void afterViews() {
          // obtaining map object
          googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
          googleMap.setOnMarkerDragListener(this);
          // enable my location button
          googleMap.setMyLocationEnabled(true);

          googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

          // create location manager client
          locationClient = new LocationClient(this, this, this);
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
          ((TextView) actionBar.getCustomView().findViewById(R.id.text1)).setText(R.string.pinned_position);

          // set listener which will repaint image to some color during click
          ivBike.setOnTouchListener(Utils.touchListener);
          ivCar.setOnTouchListener(Utils.touchListener);
          ivWalking.setOnTouchListener(Utils.touchListener);
          ivMap.setOnTouchListener(Utils.touchListener);

          // preapare progress dialog
          progress = new ProgressDialog(this);
          progress.setMessage(getResources().getString(R.string.loading));
          progress.setCancelable(true);
     }

     @Click void ivCloseRouteInfo() {
          YoYo.with(Techniques.FlipOutX).duration(1200).withListener(animationListenerInvisibleInTheEnd).playOn(llBottomMapMenuDescritption);
     }

     @Click void ivMap() {

          if ( googleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL ) {
               googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
               Utils.showStickyNotification(ADisplayOnMap.this, R.string.map_type_terrain, AppMsg.STYLE_CONFIRM, 1500);
               return;
          }

          if ( googleMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN ) {
               googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
               Utils.showStickyNotification(ADisplayOnMap.this, R.string.map_type_satellite, AppMsg.STYLE_CONFIRM, 1500);
               return;
          }

          if ( googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ) {
               googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
               Utils.showStickyNotification(ADisplayOnMap.this, R.string.map_type_normal, AppMsg.STYLE_CONFIRM, 1500);
               return;
          }
     }

     @Click void ivBike() {
          ivRouteType.setImageResource(R.drawable.bike);
          Utils.showStickyNotification(ADisplayOnMap.this, R.string.route_type_bike, AppMsg.STYLE_CONFIRM, 1500);
          drawPathToDestination(GMapV2Direction.MODE_BICYCLING, new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), new LatLng(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude));
     }

     @Click void ivCar() {
          ivRouteType.setImageResource(R.drawable.car);
          Utils.showStickyNotification(ADisplayOnMap.this, R.string.route_type_car, AppMsg.STYLE_CONFIRM, 1500);
          drawPathToDestination(GMapV2Direction.MODE_DRIVING, new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), new LatLng(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude));
     }

     @Click void ivWalking() {
          ivRouteType.setImageResource(R.drawable.walking);
          Utils.showStickyNotification(ADisplayOnMap.this, R.string.route_type_walking, AppMsg.STYLE_CONFIRM, 1500);
          drawPathToDestination(GMapV2Direction.MODE_WALKING, new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), new LatLng(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude));
     }

     private void saveLocation() {
          if ( null != locationMarker ) {
               ANoteDetails.note.location.latitude = locationMarker.getPosition().latitude;
               ANoteDetails.note.location.longitude = locationMarker.getPosition().longitude;
               Utils.showCustomToast(this, R.string.location_has_been_saved, R.drawable.ok);
          }
          onBackPressed();
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          saveLocation();
          return super.onOptionsItemSelected(item);
     }

     @Background void drawPathToDestination(String directionMode, LatLng myLocation, LatLng destination) {
          showProgressDialog();
          Document doc = routeDrawer.getDocument(myLocation, destination, directionMode);
          final String durationText = routeDrawer.getDurationText(doc);
          final String distance = routeDrawer.getDistanceText(doc);
          final String start_address = routeDrawer.getStartAddress(doc);
          final String end_address = routeDrawer.getEndAddress(doc);

          ArrayList <LatLng> directionPoint = routeDrawer.getDirection(doc);
          final PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources().getColor(R.color.route_first_color));
          final PolylineOptions rectLine2 = new PolylineOptions().width(7).color(getResources().getColor(R.color.route_second_color));

          for ( int i = 0; i < directionPoint.size(); i++ ) {
               rectLine.add(directionPoint.get(i));
               rectLine2.add(directionPoint.get(i));
          }

          ADisplayOnMap.this.runOnUiThread(new Runnable() {

               @Override public void run() {

                    googleMap.clear();

                    createMarker(new LatLng(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude));

                    // white line
                    googleMap.addPolyline(rectLine);
                    // smaller blue line
                    googleMap.addPolyline(rectLine2);

                    llBottomMapMenuDescritption.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FlipInX).duration(1200).playOn(llBottomMapMenuDescritption);

                    twDistance.setText(distance);
                    twStartAddress.setText(getResources().getString(R.string.start_addr) + start_address);
                    twEndAddress.setText(getResources().getString(R.string.end_addr) + end_address);
                    twDuration.setText(durationText);
                    hideProgressDialog();
               }
          });

     }

     @UiThread void showProgressDialog() {
          if ( null != progress && !progress.isShowing() ) {
               progress.show();
          }
     }

     @UiThread void hideProgressDialog() {
          if ( null != progress && progress.isShowing() ) {
               progress.dismiss();
          }
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
               Utils.showStickyNotification(this, R.string.there_is_no_location, AppMsg.STYLE_INFO, 1500);
               return;
          }
          if ( null != locationMarker ) { return; }
          LatLng location = new LatLng(ANoteDetails.note.location.latitude, ANoteDetails.note.location.longitude);

          // animate to center of UK
          animateCamera(location, 12);
          createMarker(location);

     }

     private void createMarker(LatLng position) {
          // set up marker options
          MarkerOptions markerOptions = new MarkerOptions();
          markerOptions.position(position);
          markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.note_position));
          // set up marker
          locationMarker = googleMap.addMarker(markerOptions);
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
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

     @Override public void onMarkerDrag(Marker arg0) {

     }

     @Override public void onMarkerDragEnd(Marker marker) {
          Utils.showStickyNotification(this, R.string.position_has_been_changed, AppMsg.STYLE_CONFIRM, 1500);
          googleMap.clear();
          createMarker(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
          if ( llBottomMapMenuDescritption.getVisibility() != View.GONE ) {
               llBottomMapMenuDescritption.setVisibility(View.VISIBLE);
               YoYo.with(Techniques.FlipOutX).duration(1200).withListener(animationListenerInvisibleInTheEnd).playOn(llBottomMapMenuDescritption);
          }
     }

     @Override public void onMarkerDragStart(Marker arg0) {

     }
}