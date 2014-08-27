package com.ik.ggnote.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ik.ggnote.R;
import com.ik.ggnote.custom.CDrawingView;
import com.ik.ggnote.model.PathSerializable;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_display_drawing ) public class ADisplayDrawing extends ActionBarActivity {

     // ================================= VIEWS
     @ViewById ImageView    ivLock;

     // ================================= VARIABLES
     // canvas
     @ViewById CDrawingView cDisplayDrawingView;

     @AfterViews void afterViews() {
          try {
               ObjectInputStream oin = new ObjectInputStream(new FileInputStream(new File(ANoteDetails.note.pathToDrawing)));
               @SuppressWarnings ( "unchecked" )
               List <PathSerializable> paths = (List <PathSerializable>) oin.readObject();
               oin.close();
               cDisplayDrawingView.setPaths(paths);
               cDisplayDrawingView.invalidate();
               cDisplayDrawingView.setColor(Color.TRANSPARENT);
          } catch (Exception e) {
               e.printStackTrace();
          }

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
          ((TextView) actionBar.getCustomView().findViewById(R.id.text)).setText(R.string.attached_drawing);
     }

     @Click void ivLock() {
          Utils.showStickyNotification(ADisplayDrawing.this, R.string.drawing_is_locked, AppMsg.STYLE_INFO, 1000);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

}