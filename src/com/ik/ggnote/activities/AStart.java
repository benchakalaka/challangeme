package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;
import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

@EActivity ( R.layout.activity_start ) public class AStart extends ActionBarActivity implements OnClickListener {

     // ============================================= VIEWS
     @ViewById EditText          etPassword;
     @ViewById TextView          twResetPassword;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;
     private ProgressDialog      progressDialog;

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

          progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
          progressDialog.setMessage("Please wait....");
          progressDialog.setCancelable(true);
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
               dialogBuilder.setContentView(R.layout.dialog_first_time);
               dialogBuilder.setCancelable(false);
               dialogBuilder.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {

                    @Override public void onClick(View v) {
                         String newPass = ((EditText) dialogBuilder.findViewById(R.id.etPassword)).getText().toString();
                         String email = ((EditText) dialogBuilder.findViewById(R.id.etEmail)).getText().toString();

                         // case 1: new pass or email is empty string
                         if ( TextUtils.isEmpty(newPass) || TextUtils.isEmpty(email) ) {
                              Utils.showStickyNotification(AStart.this, "Password/Email cannot be empty", AppMsg.STYLE_INFO, 1000);
                              return;
                         }

                         appPref.edit().password().put(newPass).apply();
                         appPref.edit().email().put(email).apply();
                         Utils.showStickyNotification(AStart.this, "Password and Email has been saved", AppMsg.STYLE_CONFIRM, 1000);

                         dialogBuilder.dismiss();
                    }
               });

               dialogBuilder.show();
          }
     }

     @Click void twResetPassword() {
          twResetPassword.startAnimation(AnimationManager.load(R.anim.rotate_right));
          BackgroundMail bm = new BackgroundMail(this);
          bm.setGmailUserName("benchakalaka@gmail.com");
          bm.setGmailPassword("rhtyltktr260690ben");
          bm.setMailTo("benchakalaka@gmail.com");
          bm.setFormSubject("GGNote support");
          bm.setSendingMessage("Sending message to your email address");
          bm.setSendingMessageSuccess("Your message was sent successfully.");
          bm.setProcessVisibility(true);
          bm.setFormBody("Your password is : " + appPref.password().get());
          bm.send();
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          exit();
          return super.onOptionsItemSelected(item);
     }

     /**
      * 'Exit' form app
      */
     private void exit() {
          Utils.showCustomToast(this, R.string.good_bye, R.drawable.book);
          moveTaskToBack(true);
     }

     /**
      * Login to the system
      */
     private void login() {
          progressDialog.show();
          // if password is not empty
          if ( !TextUtils.isEmpty(etPassword.getText().toString()) ) {
               // if password from edittext == user password
               if ( etPassword.getText().toString().equals(appPref.password().get()) ) {
                    // go to main menu, authentification successful
                    startActivity(new Intent(this, AMyNotes_.class));
               } else {
                    Utils.showStickyNotification(this, "Wrong pass", AppMsg.STYLE_ALERT, 1000);
               }
          } else {
               Utils.showStickyNotification(this, "Filed cannot be empty", AppMsg.STYLE_INFO, 1000);
          }
          progressDialog.dismiss();
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    Animation anim = AnimationManager.load(R.anim.bounce);
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
