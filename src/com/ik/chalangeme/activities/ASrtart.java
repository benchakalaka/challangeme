package com.ik.chalangeme.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.ik.chalangeme.R;
import com.ik.chalangeme.constants.ActiveRecord;

@EActivity ( R.layout.activity_srtart ) public class ASrtart extends Activity {

     // load views
     @ViewById Button   btnIamNew;

     @ViewById TextView twOwerview;

     @AfterViews void afterViews() {
          // DeviceInfo device = new TelephonyManagerProcessorUtils().getTelephonyOverview(this);
          // twOwerview.setText(device.toString());
     }

     @Override protected void onResume() {
          super.onResume();
          ActiveRecord.context = getApplicationContext();
     }

     /**
      * Execute if user clicked "i am new" button
      */
     @Click void btnIamNew() {
          startActivity(new Intent(this, ALogin_.class));
     }
}
