package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_start ) public class AStart extends ActionBarActivity implements OnClickListener {

     // ============================================= VIEWS
     @ViewById EditText          etPassword;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;

     // ============================================= METHODS
     @AfterViews void afterViews() {
          // set up GLOBAL context
          ActiveRecord.context = getApplicationContext();
          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // You customization
          actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ab_background)));

          actionBar.setIcon(R.drawable.off);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.login);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.slide_right, 0);
     }

     @Override protected void onResume() {
          super.onResume();
          etPassword.setText("p");
          // check if flag rememberMe was set, fill fields with proper values
          if ( appPref.password().get().equals("") ) {
               final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);

               dialogBuilder.withButton1Text("Ok").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Welcome to GGNote")
                         .withMessage("Hello, it's seems like you are first time here, set up your password and login, these settings will be remembered when you next time will launch GGNote").setButton1Click(new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                   startActivity(new Intent(AStart.this, ASettings_.class));
                                   dialogBuilder.dismiss();
                              }
                         }).show();
          }
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          exit();
          return super.onOptionsItemSelected(item);
     }

     /**
      * 'Exit' form app
      */
     private void exit() {
          Utils.showCustomToast(AStart.this, "Byeee", R.drawable.book);
          moveTaskToBack(true);
     }

     /**
      * Login to the system
      */
     private void login() {
          // if password is not empty
          if ( !TextUtils.isEmpty(etPassword.getText().toString()) ) {
               // if password from edittext == user password
               if ( etPassword.getText().toString().equals(appPref.password().get()) ) {
                    // go to main menu, authentification successful
                    startActivity(new Intent(this, AMyNotes_.class));
               } else {
                    Utils.showCustomToast(AStart.this, "Wrong pass", R.drawable.unsuccess);
               }
          } else {
               Utils.showCustomToast(AStart.this, "Filed cannot be empty", R.drawable.unsuccess);
          }
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    Animation anim = AnimationManager.load(R.anim.rotate_right);
                    anim.setAnimationListener(new AnimationListener() {

                         @Override public void onAnimationStart(Animation animation) {
                         }

                         @Override public void onAnimationRepeat(Animation animation) {
                         }

                         @Override public void onAnimationEnd(Animation animation) {
                              login();
                         }
                    });
                    v.startAnimation(anim);
                    break;
          }

     }
}
