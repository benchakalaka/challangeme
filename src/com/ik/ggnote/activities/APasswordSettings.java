package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_password_settings ) public class APasswordSettings extends ActionBarActivity implements OnClickListener {

     // ============================================= VIEWS
     @ViewById CheckBox          cbAskPassword;
     @ViewById EditText          etNewPassword , etOldPassword;
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

          ((ImageView) actionBar.getCustomView().findViewById(R.id.ivRightOkButton)).setOnClickListener(this);
          ((ImageView) actionBar.getCustomView().findViewById(R.id.ivRightOkButton)).setImageResource(R.drawable.ok);
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.notifications);
          cbAskPassword.setChecked(appPref.askPassword().get());
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     private void saveSettings() {
          String newPass = etNewPassword.getText().toString();
          String oldPass = etOldPassword.getText().toString();

          // case 1: old password NOT equal actual passwrod
          if ( !oldPass.equals(appPref.password().get()) ) {
               Utils.showStickyNotification(APasswordSettings.this, R.string.old_password_is_wrong, AppMsg.STYLE_ALERT, 1500);
               etOldPassword.startAnimation(AnimationManager.load(R.anim.bounce));
               return;
          }

          // case 2: new pass is empty
          if ( TextUtils.isEmpty(newPass) ) {
               Utils.showStickyNotification(APasswordSettings.this, R.string.field_cannot_be_empty, AppMsg.STYLE_ALERT, 1500);
               etNewPassword.startAnimation(AnimationManager.load(R.anim.bounce));
               return;
          }

          // case 2: everything is ok, save new password and hide dialog
          appPref.edit().password().put(newPass).apply();
          appPref.edit().askPassword().put(cbAskPassword.isChecked()).apply();
          onBackPressed();
          Utils.showCustomToast(APasswordSettings.this, R.string.password_has_been_changed, R.drawable.key);
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    saveSettings();
                    break;
          }

     }
}
