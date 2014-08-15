package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_settings ) public class ASettings extends Activity {

     // ============================================= VIEWS
     @ViewById ImageButton       ibSave , ibBack;
     @ViewById EditText          etPassword;
     @ViewById RadioButton       rbAskPassword , rbDontAskPassword;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;

     // ============================================= METHODS
     @AfterViews void afterViews() {
          if ( !TextUtils.isEmpty(appPref.password().get()) ) {
               etPassword.setVisibility(View.GONE);
          }
     }

     /**
      * 'back to login' screen
      */
     @Click void ibBack() {
          onBackPressed();
     }

     @Override public void onBackPressed() {
          super.onBackPressed();
     }

     /**
      * Login to the system
      */
     @Click void ibSave() {

          if ( etPassword.getVisibility() == View.GONE ) {
               // go to main menu
               startActivity(new Intent(this, AStart_.class));
               return;
          }

          if ( !"".equals(etPassword.getText().toString()) ) {
               appPref.edit().password().put(etPassword.getText().toString()).apply();
               // go to main menu
               startActivity(new Intent(this, AStart_.class));
          } else {
               Utils.showCustomToast(ASettings.this, "fileds cannot be empty", R.drawable.unsuccess);
          }
     }
}
