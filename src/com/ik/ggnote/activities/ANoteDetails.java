package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ik.ggnote.R;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_note_details ) public class ANoteDetails extends ActionBarActivity {

     public static ModelNote note;
     // =============================================== VIEWS
     @ViewById TextView      twTime , twNoteType , twDescription;

     @ViewById ImageButton   ibViewDraw;
     @ViewById ImageButton   ibViewPinOnMap;
     @ViewById ImageButton   ibViewPinPhoto;

     @ViewById ImageView     ivViewPinPhotoDone , ivNoteType , ivViewPinOnMapDone , ivViewDrawDone;

     // =============================================== VARIABLES

     // =============================================== METHODS
     @AfterViews void afterViews() {
          twTime.setText(note.date);
          twDescription.setText(note.description);
          twNoteType.setText(note.noteType);
          ivNoteType.setBackgroundResource(Utils.getNoteImageIdFromNoteType(note.noteType));
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
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setVisibility(View.INVISIBLE);
          ((TextView) actionBar.getCustomView().findViewById(R.id.text)).setText(R.string.note_detail);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          startActivity(new Intent(ANoteDetails.this, AMyNotes_.class));
          return super.onOptionsItemSelected(item);
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.slide_right, 0);
     }

     @Override protected void onResume() {
          super.onResume();
          displayDoneImages();
     }

     @Click void ibViewPinOnMap() {
          if ( null != note.location ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayOnMap_.class));
          } else {
               Utils.showStickyNotification(this, R.string.there_is_no_location, AppMsg.STYLE_INFO, 1000);
          }
     }

     private void displayDoneImages() {
          if ( null != note ) {
               // display only if user set proper location
               if ( null != note.location ) {
                    ivViewPinOnMapDone.setVisibility(View.VISIBLE);
               }

               if ( !TextUtils.isEmpty(note.pathToDrawing) ) {
                    ivViewDrawDone.setVisibility(View.VISIBLE);
               }

               if ( !TextUtils.isEmpty(note.pathToPhoto) ) {
                    ivViewPinPhotoDone.setVisibility(View.VISIBLE);
               }
          }
     }

     /**
      * View existing drawing, if user has drawn something before and attached to the note
      */
     @Click void ibViewDraw() {
          if ( !TextUtils.isEmpty(note.pathToDrawing) ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayDrawing_.class));
          } else {
               Utils.showStickyNotification(this, R.string.there_is_no_drawing, AppMsg.STYLE_INFO, 1000);
          }
     }

     @Click void ibViewPinPhoto() {
          if ( !TextUtils.isEmpty(note.pathToPhoto) ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayPhoto_.class));
          } else {
               Utils.showStickyNotification(this, R.string.there_is_no_photo, AppMsg.STYLE_INFO, 1000);
          }
     }
}