package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ik.ggnote.R;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_note_details ) public class ANoteDetails extends FragmentActivity {

     public static ModelNote note;
     // =============================================== VIEWS
     @ViewById TextView      twTime;
     @ViewById TextView      twDescription;

     @ViewById ImageButton   ibViewDraw;
     @ViewById ImageButton   ibViewPinOnMap;
     @ViewById ImageButton   ibViewPinPhoto;
     @ViewById ImageButton   ibBackToMenu;

     @ViewById ImageView     ivViewDrawDone;
     @ViewById ImageView     ivViewPinOnMapDone;
     @ViewById ImageView     ivViewPinPhotoDone;

     // =============================================== VARIABLES

     // =============================================== METHODS
     @AfterViews void afterViews() {
          twTime.setText(note.date);
          twDescription.setText(note.description);
     }

     @Override protected void onResume() {
          super.onResume();
          displayDoneImages();

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
               Utils.showCustomToast(ANoteDetails.this, "NETY Drawing", R.drawable.unsuccess);
          }
     }

     @Click void ibViewPinPhoto() {
          if ( !TextUtils.isEmpty(note.pathToPhoto) ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayPhoto_.class));
          } else {
               Utils.showCustomToast(ANoteDetails.this, "NETY photo", R.drawable.unsuccess);
          }
     }

     /**
      * Back to menu class on back button press
      */
     @Click void ibBackToMenu() {
          startActivity(new Intent(ANoteDetails.this, AMyNotes_.class));
     }
}