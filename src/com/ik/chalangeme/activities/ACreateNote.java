package com.ik.chalangeme.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.ik.chalangeme.R;
import com.ik.chalangeme.constants.Global;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_create_note ) public class ACreateNote extends FragmentActivity {
     // ---------------------------- VIEWS
     @ViewById ImageView ivDate;
     @ViewById ImageView ivDraw;

     // ---------------------------- VARIABLES
     CaldroidFragment    calendar;

     // ---------------------------- METHODS
     /**
      * Select note date
      */
     @Click void ivDate() {
          calendar = new CaldroidFragment();
          Bundle bundle = new Bundle();
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, "Select date");
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
          calendar.setArguments(bundle);

          // highlight dates in calendar with blue color
          calendar.show(getSupportFragmentManager(), Global.TAG);
     }

     /**
      * Draw and pin drawing to note
      */
     @Click void ivDraw() {
          startActivity(new Intent(this, ADrawingView_.class));
     }

}
