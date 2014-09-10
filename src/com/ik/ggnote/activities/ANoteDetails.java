package com.ik.ggnote.activities;

import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.time.DateUtils;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.ReminderManager;
import com.ik.ggnote.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

@EActivity ( R.layout.activity_note_details ) public class ANoteDetails extends ActionBarActivity implements OnTimeSetListener {

     public static ModelNote    note;
     // =============================================== VIEWS
     @ViewById TextView         twTime , twNoteType , twDescription , twAlarm;
     @ViewById ImageView        ivCreatedDateAndTime , ivAlarm , ivInfo , ivCloseInfo , ibViewPinPhoto , ibViewPinOnMap , ibViewDraw , ivViewPinPhotoDone , ivNoteType , ivViewPinOnMapDone , ivViewDrawDone;
     @ViewById LinearLayout     llBottomDescritption;
     // dialog type of note
     private NiftyDialogBuilder dialogBuilder;

     // =============================================== VARIABLES
     AnimatorListener           animationListenerInvisibleInTheEnd = new AnimatorListener() {

                                                                        @Override public void onAnimationStart(Animator arg0) {
                                                                             // TODO Auto-generated method stub

                                                                        }

                                                                        @Override public void onAnimationRepeat(Animator arg0) {
                                                                             // TODO Auto-generated method stub

                                                                        }

                                                                        @Override public void onAnimationEnd(Animator arg0) {
                                                                             llBottomDescritption.setVisibility(View.GONE);
                                                                        }

                                                                        @Override public void onAnimationCancel(Animator arg0) {
                                                                             // TODO Auto-generated method stub

                                                                        }
                                                                   };
     private ReminderManager    alarm;

     // =============================================== METHODS
     @AfterViews void afterViews() {

          if ( TextUtils.isEmpty(note.alarmString) ) {
               twAlarm.setText(R.string.alarm_not_set);
          } else {
               twAlarm.setText(note.alarmString);
          }

          twTime.setText(note.date);
          twDescription.setText(note.description);
          twNoteType.setText(note.noteType);
          ivNoteType.setBackgroundResource(Utils.getNoteImageIdFromNoteType(note.noteType));
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
          ((TextView) actionBar.getCustomView().findViewById(R.id.text1)).setText(R.string.note_detail);

          ibViewPinPhoto.setOnTouchListener(Utils.touchListener);
          ibViewPinOnMap.setOnTouchListener(Utils.touchListener);
          ibViewDraw.setOnTouchListener(Utils.touchListener);
          ivCreatedDateAndTime.setOnTouchListener(Utils.touchListener);
          ivInfo.setOnTouchListener(Utils.touchListener);

          dialogBuilder = new NiftyDialogBuilder(this);
     }

     @Click void ivAlarm() {
          // ////////////////////////////////////////////////// Note has no alarm
          if ( TextUtils.isEmpty(note.alarmString) ) {
               dialogBuilder.withButton1Text(getResources().getString(android.R.string.cancel)).withButton2Text(getResources().getString(android.R.string.ok)).withIcon(R.drawable.alarm).withEffect(Effectstype.Slit).withTitle(getResources().getString(R.string.alarm_not_set))
                         .withMessage(R.string.do_you_want_to_set_alarm).setButton1Click(new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                   dialogBuilder.dismiss();

                              }
                         }).setButton2Click(new OnClickListener() {

                              @Override public void onClick(View v) {
                                   Calendar calendar = Calendar.getInstance();
                                   final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ANoteDetails.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
                                   timePickerDialog.setVibrate(true);
                                   timePickerDialog.setCloseOnSingleTapMinute(false);
                                   timePickerDialog.show(getSupportFragmentManager(), getString(R.string.app_name));
                                   dialogBuilder.dismiss();
                              }

                         }).show();
               // //////////////////////////////////////////// Note has alarm
          } else {
               dialogBuilder.withButton1Text(getResources().getString(android.R.string.cancel)).withButton2Text(getResources().getString(R.string.set_alarm)).withIcon(R.drawable.alarm).withEffect(Effectstype.Slit).withTitle(getResources().getString(R.string.alarm_not_set))
                         .withMessage(R.string.do_you_want_to_set_alarm).setButton1Click(new View.OnClickListener() {
                              @Override public void onClick(View v) {
                                   dialogBuilder.dismiss();
                              }
                         }).setButton2Click(new OnClickListener() {

                              @Override public void onClick(View v) {
                                   Calendar calendar = Calendar.getInstance();
                                   calendar.set(Calendar.HOUR_OF_DAY, note.alarmHour);
                                   calendar.set(Calendar.MINUTE, note.alarmMinute);

                                   final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ANoteDetails.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
                                   timePickerDialog.setVibrate(true);
                                   timePickerDialog.setCloseOnSingleTapMinute(false);
                                   timePickerDialog.show(getSupportFragmentManager(), getString(R.string.app_name));
                                   dialogBuilder.dismiss();
                              }
                         }).show();
          }
     }

     @Click void ivCreatedDateAndTime() {
          Utils.showStickyNotification(this, R.string.displays_created_time, AppMsg.STYLE_INFO, 1500);
     }

     @Click void ivInfo() {
          llBottomDescritption.setVisibility(View.VISIBLE);
          YoYo.with(Techniques.FlipInX).duration(1200).playOn(llBottomDescritption);
     }

     @Click void ivCloseInfo() {
          YoYo.with(Techniques.FlipOutX).duration(1200).withListener(animationListenerInvisibleInTheEnd).playOn(llBottomDescritption);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          // startActivity(new Intent(ANoteDetails.this, AMyNotes_.class));
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override protected void onResume() {
          super.onResume();
          displayDoneImages();
     }

     @Click void ibViewPinOnMap() {
          if ( null != note.location ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayOnMap_.class));
          } else {
               Utils.showStickyNotification(this, R.string.there_is_no_location, AppMsg.STYLE_INFO, 1500);
          }
     }

     private void displayDoneImages() {
          if ( null != note ) {
               // display only if user set proper location
               if ( null != note.location ) {
                    ivViewPinOnMapDone.setVisibility(View.VISIBLE);
               }

               if ( !TextUtils.isEmpty(note.pathToDrawing) ) {
                    ivViewDrawDone.setVisibility(View.VISIBLE);
               }

               if ( !TextUtils.isEmpty(note.pathToPhoto) ) {
                    ivViewPinPhotoDone.setVisibility(View.VISIBLE);
               }
          }
     }

     /**
      * View existing drawing, if user has drawn something before and attached to the note
      */
     @Click void ibViewDraw() {
          if ( !TextUtils.isEmpty(note.pathToDrawing) ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayDrawing_.class));
          } else {
               Utils.showStickyNotification(this, R.string.there_is_no_drawing, AppMsg.STYLE_INFO, 1500);
          }
     }

     @Click void ibViewPinPhoto() {
          if ( !TextUtils.isEmpty(note.pathToPhoto) ) {
               startActivity(new Intent(ANoteDetails.this, ADisplayPhoto_.class));
          } else {
               Utils.showStickyNotification(this, R.string.there_is_no_photo, AppMsg.STYLE_INFO, 1500);
          }
     }

     @Override public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
          note.alarmString = hourOfDay + ":" + ((minute < 10) ? ("0" + minute) : minute);
          note.alarmHour = hourOfDay;
          note.alarmMinute = minute;
          note.save();
          twAlarm.setText(note.alarmString);

          if ( null == alarm ) {
               alarm = new ReminderManager();
          }

          Calendar now = Calendar.getInstance();
          Calendar alarmTime = (Calendar) now.clone();
          try {
               Date date = DateUtils.parseDate(note.date, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
               date.setHours(note.alarmHour);
               date.setMinutes(note.alarmMinute);
               alarmTime.setTime(date);
               if ( alarmTime.compareTo(now) == 1 ) {
                    alarm.setOnetimeTimer(this, alarmTime.getTimeInMillis(), note.getId().intValue(), PendingIntent.FLAG_UPDATE_CURRENT);
               } else {
                    Utils.showCustomToast(this, R.string.date_in_past_alarm_not_set, R.drawable.alarm);
               }
          } catch (Exception e) {
               e.printStackTrace();
               Utils.showCustomToast(this, R.string.date_in_past_alarm_not_set, R.drawable.alarm);
          }
     }
}