package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;

@EActivity ( R.layout.activity_notification_settings ) public class ANotificationSettings extends ActionBarActivity {

     // ============================================= VIEWS
     @ViewById CheckBox          cbVibrate , cbDisplayMessage;
     // @ViewById ImageView ivSimpleUp , ivSimpleDown , ivEventUp , ivEventDown , ivUrgentUp , ivUrgentDown , ivReminderUp , ivReminderDown , ivWorkUp ,
     // ivWorkDown;

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
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.notifications);
          cbDisplayMessage.setChecked(appPref.displayMessageText().get());
          cbVibrate.setChecked(appPref.vibrateOnNotification().get());
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          appPref.edit().displayMessageText().put(cbDisplayMessage.isChecked()).apply();
          appPref.edit().vibrateOnNotification().put(cbVibrate.isChecked()).apply();
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }
}
