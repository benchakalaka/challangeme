package com.ik.chalangeme.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import com.ik.chalangeme.R;

@EActivity ( R.layout.activity_srtart ) public class ASrtart extends Activity {

     // load views
     @ViewById Button btnIamNew;

     /**
      * Execute if user clicked "i am new" button
      */
     @Click void btnIamNew() {
          startActivity(new Intent(this, ALogin_.class));
     }
}
