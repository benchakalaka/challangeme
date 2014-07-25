package com.ik.chalangeme.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.ik.chalangeme.R;
import com.roomorama.caldroid.CaldroidFragment;

/**
 * Description: class represents all functionaity which is available on Google
 * map
 * 
 * @author Ihor Karpachev
 *         All content is activity property of Datascope Systems Ltd. Date: 13 Dec 2013
 *         Time: 18:21:35
 */

@EActivity ( R.layout.activity_map ) public class AMap extends FragmentActivity implements OnCameraChangeListener {

     // Google map instance
     public GoogleMap               googleMap;
     // Calendar view
     public static CaldroidFragment dialogCaldroidFragment = new CaldroidFragment();
     // represents center of UK
     private static final LatLng    APPROX_CENTER_OF_UK    = new LatLng(52.6368778, -1.139759199999957);

     @AfterViews void afterViews() {
          // obtaining map object
          googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
          // set-up cluster manager

          // temporary trick, capture event when map has been downloaded ................................................
          googleMap.setOnCameraChangeListener(this);

          animateCamera(APPROX_CENTER_OF_UK, 7);
     }

     /**
      * Animate camera to new map position with given zoom
      * 
      * @param position
      * @param zoom
      */
     private void animateCamera(LatLng position, float zoom) {
          CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, zoom);
          googleMap.animateCamera(cameraUpdate, 2000, null);
     }

     @Override public void onCameraChange(CameraPosition arg0) {
          // map has been downloaded, refresh map and destroy listener
          googleMap.setOnCameraChangeListener(null);
     }
}