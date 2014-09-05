package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_settings ) public class ASettings extends ActionBarActivity {

     // ============================================= VIEWS

     @ViewById RelativeLayout    rlNotifications , rlNotesOrder , rlChangePass , rlChangeEmail;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;

     // ============================================= METHODS
     @AfterViews void afterViews() {

          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // You customization
          actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ab_background)));

          actionBar.setIcon(R.drawable.arrowleft);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setVisibility(View.INVISIBLE);
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.settings);
     }

     @Click void rlChangeEmail() {

          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.setContentView(R.layout.dialog_change_email);
          ((EditText) dialogBuilder.findViewById(R.id.etNewEmail)).setText(appPref.email().get());

          dialogBuilder.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    String newEmail = ((EditText) dialogBuilder.findViewById(R.id.etNewEmail)).getText().toString();

                    // case 1: new pass or old pass is empty string
                    if ( TextUtils.isEmpty(newEmail) ) {
                         Utils.showStickyNotification(ASettings.this, R.string.field_cannot_be_empty, AppMsg.STYLE_INFO, 1000);
                         return;
                    }

                    // case 3: everything is ok, save new password and hide dialog
                    appPref.edit().email().put(newEmail).apply();
                    Utils.showStickyNotification(ASettings.this, R.string.email_has_been_changed, AppMsg.STYLE_INFO, 1500);
                    dialogBuilder.dismiss();
               }
          });

          dialogBuilder.findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          });

          dialogBuilder.show();
     }

     @Click void rlChangePass() {
          startActivity(new Intent(this, APasswordSettings_.class));
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     @Click void rlNotesOrder() {
          rlNotesOrder.startAnimation(AnimationManager.load(R.anim.abc_fade_in));
          startActivity(new Intent(ASettings.this, ANotesOrder_.class));
     }

     @Click void rlNotifications() {
          rlNotifications.startAnimation(AnimationManager.load(R.anim.abc_fade_in));
          startActivity(new Intent(ASettings.this, ANotificationSettings_.class));
     }

}
