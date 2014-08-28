package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ik.ggnote.R;
import com.ik.ggnote.custom.CDrawingView;

@EActivity ( R.layout.activity_display_photo ) public class ADisplayPhoto extends ActionBarActivity {

     // ================================= VIEWS
     @ViewById ImageView    ivPhoto;

     // ================================= VARIABLES
     // canvas
     @ViewById CDrawingView cDisplayDrawingView;

     @AfterViews void afterViews() {
          Drawable d = Drawable.createFromPath(ANoteDetails.note.pathToPhoto);
          ivPhoto.setBackgroundDrawable(d);
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

          ((TextView) actionBar.getCustomView().findViewById(R.id.text)).setText(R.string.attached_photo);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);

     }

}