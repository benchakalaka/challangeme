package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.ik.chalangeme.R;
import com.ik.ggnote.utils.AppSharedPreferences_;

@EActivity ( R.layout.activity_start ) public class ASrtart extends Activity {

     // ============================================= VIEWS
     @ViewById Button            btnLogin;
     @ViewById EditText          etLogin;
     @ViewById EditText          etPassword;
     @ViewById ToggleButton      tbRememberMe;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;

     // ============================================= METHODS
     @AfterViews void afterViews() {
          // check if flag rememberMe was set, fill fields with proper values
          tbRememberMe.setChecked(appPref.rememberMe().getOr(false));
          if ( tbRememberMe.isChecked() ) {
               etLogin.setText(appPref.login().getOr(""));
               etPassword.setText(appPref.password().getOr(""));
          }
     }

     /**
      * Login to the system
      */
     @Click void btnLogin() {

          // if fields are not empty and rememberMe flag is checked, save settings
          if ( tbRememberMe.isChecked() && !"".equals(etLogin.getText().toString()) || !"".equals(etPassword.getText().toString()) ) {
               appPref.edit().login().put(etLogin.getText().toString()).apply();
               appPref.edit().password().put(etPassword.getText().toString()).apply();
               appPref.edit().rememberMe().put(true).apply();
          } else {
               // clear preferences
               appPref.login().put("");
               appPref.password().put("");
               appPref.edit().rememberMe().put(false).apply();
          }

          // go to main menu
          startActivity(new Intent(this, AMenu_.class));
     }
}
