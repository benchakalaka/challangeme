package com.ik.chalangeme.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import com.ik.chalangeme.R;

@EActivity ( R.layout.activity_login ) public class ALogin extends Activity {
     @ViewById Button btnLogin;

     @Click void btnLogin() {
          startActivity(new Intent(this, AMenu_.class));
     }
}
