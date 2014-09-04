package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devspark.appmsg.AppMsg;
import com.ik.ggnote.R;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

@EActivity ( R.layout.activity_notes_order ) public class ANotesOrder extends ActionBarActivity implements OnClickListener {

     // ============================================= VIEWS
     @ViewById LinearLayout      llRoot;
     @ViewById RelativeLayout    rl1 , rl2 , rl3 , rl4 , rl5 , rlWork , rlReminder , rlUrgent , rlEvent , rlSimple;
     @ViewById ImageView         ivSimpleUp , ivSimpleDown , ivEventUp , ivEventDown , ivUrgentUp , ivUrgentDown , ivReminderUp , ivReminderDown , ivWorkUp , ivWorkDown;

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
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.change_notes_order);

          // remove all views from llRoot
          llRoot.removeAllViews();

          for ( int i = 0; i < 5; i++ ) {
               View view = getOrderViewByPosition(i);
               llRoot.addView(view);
          }

          ivSimpleUp.setOnClickListener(this);
          ivSimpleDown.setOnClickListener(this);
          ivEventUp.setOnClickListener(this);
          ivEventDown.setOnClickListener(this);
          ivUrgentUp.setOnClickListener(this);
          ivUrgentDown.setOnClickListener(this);
          ivReminderUp.setOnClickListener(this);
          ivReminderDown.setOnClickListener(this);
          ivWorkUp.setOnClickListener(this);
          ivWorkDown.setOnClickListener(this);
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Click void rl1() {
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(rl1);
          Utils.showStickyNotification(ANotesOrder.this, R.string.this_type_will_be_displayed_first, AppMsg.STYLE_CONFIRM, 1500);
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(llRoot.getChildAt(0));
     }

     @Click void rl2() {
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(rl2);
          Utils.showStickyNotification(ANotesOrder.this, R.string.this_type_will_be_displayed_second, AppMsg.STYLE_CONFIRM, 1500);
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(llRoot.getChildAt(1));
     }

     @Click void rl3() {
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(rl3);
          Utils.showStickyNotification(ANotesOrder.this, R.string.this_type_will_be_displayed_third, AppMsg.STYLE_CONFIRM, 1500);
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(llRoot.getChildAt(2));
     }

     @Click void rl4() {
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(rl4);
          Utils.showStickyNotification(ANotesOrder.this, R.string.this_type_will_be_displayed_fourth, AppMsg.STYLE_CONFIRM, 1500);
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(llRoot.getChildAt(3));
     }

     @Click void rl5() {
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(rl5);
          Utils.showStickyNotification(ANotesOrder.this, R.string.this_type_will_be_displayed_fifth, AppMsg.STYLE_CONFIRM, 1500);
          YoYo.with(Techniques.BounceIn).duration(1000).playOn(llRoot.getChildAt(4));
     }

     // TODO: replace with switch
     private View getOrderViewByPosition(int position) {
          if ( position == appPref.orderNumberSimple().get() ) { return rlSimple; }
          if ( position == appPref.orderNumberEvent().get() ) { return rlEvent; }
          if ( position == appPref.orderNumberUrgent().get() ) { return rlUrgent; }
          if ( position == appPref.orderNumberReminder().get() ) { return rlReminder; }
          if ( position == appPref.orderNumberWork().get() ) { return rlWork; }
          return null;
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          appPref.edit().orderNumberSimple().put(llRoot.indexOfChild(rlSimple)).apply();
          appPref.edit().orderNumberEvent().put(llRoot.indexOfChild(rlEvent)).apply();
          appPref.edit().orderNumberUrgent().put(llRoot.indexOfChild(rlUrgent)).apply();
          appPref.edit().orderNumberReminder().put(llRoot.indexOfChild(rlReminder)).apply();
          appPref.edit().orderNumberWork().put(llRoot.indexOfChild(rlWork)).apply();
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     void moveViewDown(final View viewToMove) {
          // get current index
          final int indexOfViewToMove = llRoot.indexOfChild(viewToMove);

          // first position, cannot go up
          if ( indexOfViewToMove == llRoot.getChildCount() - 1 ) {
               YoYo.with(Techniques.Shake).duration(1000).withListener(new AnimatorListener() {

                    @Override public void onAnimationStart(Animator arg0) {
                         Utils.showStickyNotification(ANotesOrder.this, R.string.is_last_item, AppMsg.STYLE_INFO, 1000);
                    }

                    @Override public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override public void onAnimationEnd(Animator arg0) {

                    }

                    @Override public void onAnimationCancel(Animator arg0) {
                    }
               }).playOn(viewToMove);
               return;
          }

          // remove bottom view
          final View view = llRoot.getChildAt(indexOfViewToMove + 1);

          YoYo.with(Techniques.BounceInDown).duration(1000).withListener(new AnimatorListener() {

               @Override public void onAnimationStart(Animator arg0) {
                    // remove view
                    llRoot.removeView(viewToMove);

                    // remove bottomView
                    llRoot.removeView(view);

                    // add simple view
                    llRoot.addView(viewToMove, indexOfViewToMove);
                    llRoot.addView(view, indexOfViewToMove);
                    YoYo.with(Techniques.BounceInUp).duration(1000).playOn(view);

               }

               @Override public void onAnimationRepeat(Animator arg0) {
               }

               @Override public void onAnimationEnd(Animator arg0) {
               }

               @Override public void onAnimationCancel(Animator arg0) {
               }
          }).playOn(viewToMove);

     }

     void moveViewUp(final View viewToMove) {
          // get current index
          final int indexOfViewToMove = llRoot.indexOfChild(viewToMove);

          // first position, cannot go up
          if ( indexOfViewToMove == 0 ) {
               YoYo.with(Techniques.Shake).duration(1000).withListener(new AnimatorListener() {

                    @Override public void onAnimationStart(Animator arg0) {
                         Utils.showStickyNotification(ANotesOrder.this, R.string.is_first_item, AppMsg.STYLE_INFO, 1000);
                    }

                    @Override public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override public void onAnimationEnd(Animator arg0) {

                    }

                    @Override public void onAnimationCancel(Animator arg0) {
                    }
               }).playOn(viewToMove);
               return;
          }

          // remove bottom view
          final View view = llRoot.getChildAt(indexOfViewToMove - 1);

          YoYo.with(Techniques.BounceInUp).duration(1000).withListener(new AnimatorListener() {

               @Override public void onAnimationStart(Animator arg0) {
                    // remove simple
                    llRoot.removeView(viewToMove);

                    // remove bottomView
                    llRoot.removeView(view);

                    // add simple view
                    llRoot.addView(viewToMove, indexOfViewToMove - 1);
                    llRoot.addView(view, indexOfViewToMove);
                    YoYo.with(Techniques.BounceInDown).duration(1000).playOn(view);
               }

               @Override public void onAnimationRepeat(Animator arg0) {
               }

               @Override public void onAnimationEnd(Animator arg0) {
               }

               @Override public void onAnimationCancel(Animator arg0) {
               }
          }).playOn(viewToMove);
     }

     @Override public void onClick(final View v) {
          // view go up
          if ( v.getTag().toString().equals("up") ) {
               // it's only up or down button, so get parent in order to take relative layout
               moveViewUp((View) v.getParent());
          } else {
               // view go down, it's only up or down button, so get parent in order to take relative layout
               moveViewDown((View) v.getParent());
          }
     }
}
