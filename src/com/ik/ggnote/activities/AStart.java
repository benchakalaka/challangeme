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

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_start ) public class AStart extends Activity {

     // ============================================= VIEWS
     @ViewById ImageButton       ibLogin , ibSettings , ibExit;
     @ViewById EditText          etPassword;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;

     // ============================================= METHODS
     @AfterViews void afterViews() {
          // set up GLOBAL context
          ActiveRecord.context = getApplicationContext();
     }

     @Override protected void onResume() {
          super.onResume();
          etPassword.setText("");
          // check if flag rememberMe was set, fill fields with proper values
          if ( appPref.password().get().equals("") ) {
               final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

               dialogBuilder.withButton1Text("Ok").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Welcome to GGNote")
                         .withMessage("Hello, it's seems like you are first time here, set up your password and login, these settings will be remembered when you next time will launch GGNote").setButton1Click(new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                   startActivity(new Intent(AStart.this, ASettings_.class));
                                   dialogBuilder.dismiss();
                              }
                         }).show();
          }
     }

     /**
      * 'Exit' form app
      */
     @Click void ibSettings() {
          startActivity(new Intent(AStart.this, ASettings_.class));
     }

     /**
      * 'Exit' form app
      */
     @Click void ibExit() {
          Utils.showCustomToast(AStart.this, "Byeee", R.drawable.happy);
          moveTaskToBack(true);
     }

     @Override public void onBackPressed() {
          super.onBackPressed();
          ibExit.performClick();
     }

     /**
      * Login to the system
      */
     @Click void ibLogin() {
          // if password is not empty
          if ( !TextUtils.isEmpty(etPassword.getText().toString()) ) {
               // if password from edittext == user password
               if ( etPassword.getText().toString().equals(appPref.password().get()) ) {
                    // go to main menu, authentification successful
                    startActivity(new Intent(this, AMenu_.class));
               } else {
                    Utils.showCustomToast(AStart.this, "Wrong pass", R.drawable.unsuccess);
               }
          } else {
               Utils.showCustomToast(AStart.this, "Filed cannot be empty", R.drawable.unsuccess);
          }
     }
}
