package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;

@EActivity ( R.layout.activity_settings ) public class ASettings extends ActionBarActivity implements OnClickListener {

     // ============================================= VIEWS
     @ViewById EditText          etPassword;
     @ViewById RadioButton       rbAskPassword , rbDontAskPassword;

     // ============================================= VARIABLES
     @Pref AppSharedPreferences_ appPref;

     // ============================================= METHODS
     @AfterViews void afterViews() {
          // if ( !TextUtils.isEmpty(appPref.password().get()) ) {
          // etPassword.setVisibility(View.GONE);
          // }

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
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.ok);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);

     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.slide_right, 0);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          startActivity(new Intent(this, AMyNotes_.class));
          return super.onOptionsItemSelected(item);
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:

                    if ( etPassword.getVisibility() == View.GONE ) {
                         // go to main menu
                         startActivity(new Intent(this, AStart_.class));
                         return;
                    }

                    if ( !"".equals(etPassword.getText().toString()) ) {
                         appPref.edit().password().put(etPassword.getText().toString()).apply();
                         Utils.showCustomToast(ASettings.this, "Saved settings!", R.drawable.unsuccess);
                         // go to main menu
                         onBackPressed();
                    } else {
                         Utils.showCustomToast(ASettings.this, "fileds cannot be empty", R.drawable.unsuccess);
                    }

                    break;
          }
     }
}
