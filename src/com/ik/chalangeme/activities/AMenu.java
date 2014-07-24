package com.ik.chalangeme.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import com.ik.chalangeme.R;

@EActivity ( R.layout.activity_menu ) public class AMenu extends Activity {
     @ViewById Button btnCreateNote;

     @Click void btnCreateNote() {
          startActivity(new Intent(this, ACreateNote_.class));
     }
}
