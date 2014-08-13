package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;

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

          // set up GLOBAL context
          ActiveRecord.context = getApplicationContext();

          final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

          dialogBuilder.withButton1Text("Ok").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Welcome to GGNote")
                    .withMessage("Hello, it's seems like you are first time here, set up your password and login, these settings will be remembered when you next time will launch GGNote").setButton1Click(new View.OnClickListener() {
                         @Override public void onClick(View v) {
                              Utils.logw("btn1");
                              dialogBuilder.dismiss();
                         }
                    }).show();
     }

     /**
      * Login to the system
      */
     @Click void btnLogin() {
          // if fields are not empty and rememberMe flag is checked, save settings
          if ( tbRememberMe.isChecked() && !"".equals(etLogin.getText().toString()) && !"".equals(etPassword.getText().toString()) ) {
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
