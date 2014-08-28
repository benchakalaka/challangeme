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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_settings ) public class ASettings extends ActionBarActivity implements OnClickListener {

     // ============================================= VIEWS
     @ViewById RadioButton       rbAskPassword , rbDontAskPassword;

     @ViewById RelativeLayout    rlNotifications , rlNotesOrder , rlChangePass;

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
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text)).setText(R.string.settings);
          boolean isChecked = appPref.askPassword().getOr(true);
          if ( !isChecked ) {
               rbDontAskPassword.setChecked(true);
          }
          rbAskPassword.setChecked(isChecked);
          rbAskPassword.setOnClickListener(this);
          rbDontAskPassword.setOnClickListener(this);
     }

     public void logout() {
          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);

          dialogBuilder.withButton1Text(getResources().getString(android.R.string.ok)).withButton2Text(getResources().getString(R.string.cancel)).withIcon(R.drawable.book).withEffect(Effectstype.Slit).withTitle(getResources().getString(R.string.logout))
                    .withMessage(getResources().getString(R.string.do_you_really_want_logout)).setButton1Click(new View.OnClickListener() {
                         @Override public void onClick(View v) {
                              startActivity(new Intent(ASettings.this, AStart_.class));
                              dialogBuilder.dismiss();
                         }
                    }).setButton2Click(new OnClickListener() {

                         @Override public void onClick(View v) {
                              dialogBuilder.dismiss();
                         }
                    }).show();
     }

     @Click void rlChangePass() {

          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.setContentView(R.layout.dialog_change_password);
          ((EditText) dialogBuilder.findViewById(R.id.etNewEmail)).setText(appPref.email().get());

          dialogBuilder.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    String newPass = ((EditText) dialogBuilder.findViewById(R.id.etNewPassword)).getText().toString();
                    String oldPass = ((EditText) dialogBuilder.findViewById(R.id.etOldPassword)).getText().toString();
                    String newEmail = ((EditText) dialogBuilder.findViewById(R.id.etNewEmail)).getText().toString();

                    // case 1: new pass or old pass is empty string
                    if ( TextUtils.isEmpty(newPass) || TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newEmail) ) {
                         Utils.showStickyNotification(ASettings.this, "R.string.Email, Old/New password cannot be empty", AppMsg.STYLE_INFO, 1000);
                         return;
                    }

                    // case 2: old password NOT equal actual passwrod
                    if ( !oldPass.equals(appPref.password().get()) ) {
                         Utils.showStickyNotification(ASettings.this, "R.string.Old password is wrong", AppMsg.STYLE_INFO, 1000);
                         return;
                    }

                    // case 3: everything is ok, save new password and hide dialog
                    appPref.edit().password().put(newPass).apply();
                    Utils.showStickyNotification(ASettings.this, "R.string.Password/Email has been changed", AppMsg.STYLE_INFO, 1000);
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

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.slide_right, 0);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     @Override public void onClick(View v) {
          Utils.logw("Ask password == " + rbAskPassword.isChecked());
          appPref.edit().askPassword().put(rbAskPassword.isChecked()).apply();
     }
}
