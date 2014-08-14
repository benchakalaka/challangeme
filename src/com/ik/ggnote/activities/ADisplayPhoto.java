package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ik.ggnote.R;
import com.ik.ggnote.custom.CDrawingView;

@EActivity ( R.layout.activity_display_photo ) public class ADisplayPhoto extends FragmentActivity {

     // ================================= VIEWS
     @ViewById ImageButton  ibBack;
     @ViewById ImageView    ivPhoto;

     // ================================= VARIABLES
     // canvas
     @ViewById CDrawingView cDisplayDrawingView;

     @AfterViews void afterViews() {
          Drawable d = Drawable.createFromPath(ANoteDetails.note.pathToPhoto);
          ivPhoto.setBackgroundDrawable(d);
     }

     @Click void ibBack() {
          onBackPressed();
     }

}