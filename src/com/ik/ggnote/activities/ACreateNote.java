package com.ik.ggnote.activities;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.time.DateUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.ReminderManager;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

@EActivity ( R.layout.activity_create_note ) public class ACreateNote extends ActionBarActivity implements OnClickListener , OnTimeSetListener {
     // ===================================================================================== VIEWS
     @ViewById ImageView                                  ibDraw , ibPinOnMap , ibPinPhoto , ibNoteType , ibNoteTypeDone , ivDrawDone , ivPrevDay , ivNextDay , ivPinOnMapDone , ivPinPhotoDone;

     @ViewById EditText                                   etDescription;
     @ViewById TextView                                   twDate;

     // ===================================================================================== VARIABLES
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();
     // set up current date
     private static Date                                  currentDate          = new Date();
     // displaying progress
     private ProgressDialog                               progressDialog;
     // dialog type of note
     private NiftyDialogBuilder                           dialogBuilder , dialogBuilderBackPress;
     // alarm manager
     private ReminderManager                              alarm;
     // Setup listener
     public final com.roomorama.caldroid.CaldroidListener onDateChangeListener = new com.roomorama.caldroid.CaldroidListener() {
                                                                                    @Override public void onSelectDate(final Date date, View view) {
                                                                                         currentDate = date;
                                                                                         setUpCurrentDate(currentDate);
                                                                                         calendar.dismiss();
                                                                                    }

                                                                                    @Override public void onChangeMonth(int month, int year) {
                                                                                    }

                                                                                    @Override public void onLongClickDate(Date date, View view) {
                                                                                    }

                                                                                    @Override public void onCaldroidViewCreated() {
                                                                                    }
                                                                               };

     // ======================================================================================= METHODS
     @AfterViews void afterViews() {
          getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

          // configure persistant bundle for displaying calendar view
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, getResources().getString(R.string.select_date));
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);

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
          ((ImageView) actionBar.getCustomView().findViewById(R.id.ivRightOkButton)).setImageResource(R.drawable.ok);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnTouchListener(Utils.touchListener);
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.add_note);

          progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
          progressDialog.setMessage(getResources().getString(R.string.please_wait));
          progressDialog.setCancelable(true);

          ibDraw.setOnTouchListener(Utils.touchListener);
          ibPinOnMap.setOnTouchListener(Utils.touchListener);
          ibPinPhoto.setOnTouchListener(Utils.touchListener);
          ibNoteType.setOnTouchListener(Utils.touchListener);

          dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.setContentView(R.layout.dialog_type_of_note);

          dialogBuilderBackPress = new NiftyDialogBuilder(this);
     }

     /**
      * Change date
      */
     @Click void twDate() {
          YoYo.with(Techniques.BounceIn).duration(1000).withListener(new AnimatorListener() {

               @Override public void onAnimationStart(Animator arg0) {
               }

               @Override public void onAnimationRepeat(Animator arg0) {
               }

               @Override public void onAnimationEnd(Animator arg0) {
                    List <ModelNote> notes = DatabaseUtils.getAllNotes();
                    HashMap <Date, Integer> datesToHighligt = new HashMap <Date, Integer>();
                    calendar = new CaldroidFragment();
                    if ( null != notes && !notes.isEmpty() ) {

                         calendar.setCaldroidListener(onDateChangeListener);
                         calendar.setArguments(bundle);
                         for ( ModelNote note : notes ) {
                              try {
                                   Date key = DateUtils.parseDate(note.date, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
                                   datesToHighligt.put(key, R.color.ab_background);
                              } catch (ParseException e) {
                                   e.printStackTrace();
                              }
                         }
                         calendar.setBackgroundResourceForDates(datesToHighligt);
                    }
                    // highlight dates in calendar with blue color
                    calendar.show(getSupportFragmentManager(), Global.TAG);
               }

               @Override public void onAnimationCancel(Animator arg0) {
               }
          }).playOn(twDate);
     }

     @Click void ivPrevDay() {
          ivPrevDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate));
          currentDate = DateUtils.addDays(currentDate, -1);
          setUpCurrentDate(currentDate);
     }

     @Click void ivNextDay() {
          ivNextDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate));
          currentDate = DateUtils.addDays(currentDate, 1);
          setUpCurrentDate(currentDate);
     }

     @Click void ibPinPhoto() {
          ibPinPhoto.startAnimation(AnimationManager.load(R.anim.fade_in));
          captureCameraPhoto(this);
     }

     @Override protected void onResume() {
          super.onResume();
          setUpCurrentDate(currentDate);
          displayDoneImages();
          playButtonAnimations();
     }

     private void playButtonAnimations() {
          try {
               YoYo.with(Techniques.Landing).delay(400).duration(700).playOn(ibNoteType);
               YoYo.with(Techniques.Landing).delay(500).duration(700).playOn(ibPinPhoto);
               YoYo.with(Techniques.Landing).delay(600).duration(700).playOn(ibPinOnMap);
               YoYo.with(Techniques.Landing).delay(700).duration(700).playOn(ibDraw);
          } catch (Exception ex) {
               ex.printStackTrace();
          }
     }

     private void displayDoneImages() {
          if ( null != ActiveRecord.currentNote ) {
               //
               ibNoteTypeDone.setVisibility(View.VISIBLE);
               // display only if user set proper location
               if ( null != ActiveRecord.currentNote.location ) {
                    ivPinOnMapDone.setVisibility(View.VISIBLE);
               }

               if ( !TextUtils.isEmpty(ActiveRecord.currentNote.pathToDrawing) ) {
                    ivDrawDone.setVisibility(View.VISIBLE);
               }

               if ( !TextUtils.isEmpty(ActiveRecord.currentNote.pathToPhoto) ) {
                    ivPinPhotoDone.setVisibility(View.VISIBLE);
               }
          }
     }

     private void askSaveJenote() {
          boolean needToAsk = false;
          if ( null != ActiveRecord.currentNote ) {
               // display only if user set proper location
               if ( null != ActiveRecord.currentNote.location ) {
                    needToAsk = true;
               }

               if ( !TextUtils.isEmpty(ActiveRecord.currentNote.pathToDrawing) ) {
                    needToAsk = true;
               }

               if ( !TextUtils.isEmpty(etDescription.getText().toString()) ) {
                    needToAsk = true;
               }

               if ( !TextUtils.isEmpty(ActiveRecord.currentNote.pathToPhoto) ) {
                    needToAsk = true;
               }

               if ( needToAsk ) {
                    dialogBuilderBackPress.withButton1Text(getResources().getString(android.R.string.cancel)).withButton2Text(getResources().getString(R.string.save)).withIcon(R.drawable.jenote).withEffect(Effectstype.Slit).withTitle(getResources()
                              .getString(R.string.jenote_hasnot_been_saved)).withMessage(getResources().getString(R.string.do_you_want_to_save_jenote)).setButton1Click(new View.OnClickListener() {
                         @Override public void onClick(View v) {
                              dialogBuilder.dismiss();
                              startActivity(new Intent(ACreateNote.this, AMyNotes_.class));
                         }
                    }).setButton2Click(new OnClickListener() {

                         @Override public void onClick(View v) {
                              ibCreateNote();
                         }

                    }).show();
               } else {
                    startActivity(new Intent(this, AMyNotes_.class));
               }
          }
     }

     /**
      * Draw and pin drawing to note
      */
     @Click void ibDraw() {
          // ibDraw.startAnimation(AnimationManager.load(R.anim.fade_in));
          startActivity(new Intent(this, ADrawingView_.class));
     }

     /**
      * Select location and pin to note
      */
     @Click void ibPinOnMap() {
          // ibPinOnMap.startAnimation(AnimationManager.load(R.anim.fade_in));
          progressDialog.show();
          startActivity(new Intent(this, AMap_.class));
          progressDialog.dismiss();
     }

     private void setUpCurrentDate(Date dateToSetUp) {
          twDate.setText(Utils.formatDate(dateToSetUp, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD));
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          askSaveJenote();
          return super.onOptionsItemSelected(item);
     }

     /**
      * Create ready note
      */
     private void ibCreateNote() {
          // set up selected date
          ActiveRecord.currentNote.date = Utils.formatDate(currentDate, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
          // set note description
          ActiveRecord.currentNote.description = etDescription.getText().toString();
          // if user set location, save it (sugar lib not saving it by cascade)
          if ( null != ActiveRecord.currentNote.location ) {
               ActiveRecord.currentNote.location.save();
          }
          // just created note mark as incompleted
          ActiveRecord.currentNote.isCompleted = false;
          // save note
          ActiveRecord.currentNote.save();

          createNotification(ActiveRecord.currentNote);

          Utils.showCustomToast(ACreateNote.this, R.string.note_has_been_created, R.drawable.book);

          startActivity(new Intent(ACreateNote.this, AMyNotes_.class));
     }

     private void createNotification(ModelNote noteToSet) {
          if ( !TextUtils.isEmpty(noteToSet.alarmString) ) {

               if ( null == alarm ) {
                    alarm = new ReminderManager();
               }

               Calendar calendar = Calendar.getInstance();
               calendar.set(Calendar.HOUR_OF_DAY, noteToSet.alarmHour);
               calendar.set(Calendar.MINUTE, noteToSet.alarmMinute);
               if ( alarm != null ) {
                    alarm.setOnetimeTimer(this, calendar.getTimeInMillis(), noteToSet.getId().intValue(), 0);
               }
          }
     }

     // private boolean isDateInPast(Date currDate, ModelNote noteToSet) {
     // // get now time and date
     // Date now = new Date();
     // // if currentdate (IS NOT >) today then date in past, exit, not creating notification
     // if ( currDate.after(now) ) { return false; }
     //
     // Utils.logw("ALARM IN PAST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
     // return true;
     // }

     @OnActivityResult ( Global.CAPTURE_CAMERA_PHOTO ) void onResult(int resultCode, Intent data) {
          if ( resultCode == Activity.RESULT_OK ) {
               try {
                    File photo = new File(ActiveRecord.currentNote.pathToPhoto);
                    if ( (null != photo) && (photo.length() != 0) ) {
                         Utils.showCustomToast(this, R.string.photo_saved, R.drawable.ok);
                    } else {
                         Utils.showCustomToast(this, R.string.photo_saving_failed, R.drawable.warning);
                    }
               } catch (Exception ex) {
                    ex.printStackTrace();
                    Utils.showStickyNotification(this, R.string.file_cannot_be_created, AppMsg.STYLE_ALERT, 2000);

                    ActiveRecord.currentNote.pathToPhoto = "";
               }
          } else {
               ActiveRecord.currentNote.pathToPhoto = "";
          }
     }

     public void captureCameraPhoto(Activity activity) {
          try {
               // Check, if directory with photos exist, if so skip this if
               // otherwise create directories hierarchy
               File photosDir = new File(Global.APP_PHOTO_DIRECTORY);
               if ( !photosDir.exists() ) {
                    photosDir.mkdirs();
               }
               // Create date formatter for filename
               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.ENGLISH);
               // format current date to string format
               String date = dateFormat.format(new Date());
               // construct filename
               ActiveRecord.currentNote.pathToPhoto = Global.APP_PHOTO_DIRECTORY + date + ".jpg";
               // craete file with constructed filename
               File photoFile = new File(ActiveRecord.currentNote.pathToPhoto);
               if ( photoFile.createNewFile() ) {
                    // Construct Intent and show camera
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    activity.startActivityForResult(cameraIntent, Global.CAPTURE_CAMERA_PHOTO);
               } else {
                    Utils.showStickyNotification(this, R.string.file_cannot_be_created, AppMsg.STYLE_ALERT, 1000);
               }
          } catch (Exception e) {
               Utils.logw(e.getMessage());
               Utils.showStickyNotification(this, R.string.sd_card_is_busy, AppMsg.STYLE_ALERT, 1000);
               ActiveRecord.currentNote.pathToPhoto = "";
          }
     }

     @Click void ibNoteType() {
          showNoteTypePopup();
     }

     private void showNoteTypePopup() {
          int imageId = Utils.getNoteImageIdFromNoteType(ActiveRecord.currentNote.noteType);
          int buttonId = Utils.getRadioButtonIdFromNoteType(ActiveRecord.currentNote.noteType);
          if ( imageId != -1 ) {
               ((ImageView) dialogBuilder.findViewById(R.id.ivNoteType)).setBackgroundResource(imageId);
               ((RadioGroup) dialogBuilder.findViewById(R.id.rgTypeOfNote)).check(buttonId);
          }

          ((RadioGroup) dialogBuilder.findViewById(R.id.rgTypeOfNote)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

               @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int imageId = Utils.getNoteImageIdFromNoteType(dialogBuilder.findViewById(checkedId).getTag().toString());
                    ((ImageView) dialogBuilder.findViewById(R.id.ivNoteType)).setBackgroundResource(imageId);
               }
          });

          dialogBuilder.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    int rbCheckedId = ((RadioGroup) dialogBuilder.findViewById(R.id.rgTypeOfNote)).getCheckedRadioButtonId();
                    String buttonTag = dialogBuilder.findViewById(rbCheckedId).getTag().toString();
                    ActiveRecord.currentNote.noteType = buttonTag;
                    dialogBuilder.dismiss();
               }
          });

          dialogBuilder.findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          });

          // if ( !isDateInPast(currentDate, ActiveRecord.currentNote) ) {
          dialogBuilder.findViewById(R.id.buttonAlarm).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();

                    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ACreateNote.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

                    timePickerDialog.setVibrate(true);
                    timePickerDialog.setCloseOnSingleTapMinute(false);
                    timePickerDialog.show(getSupportFragmentManager(), getString(R.string.app_name));
               }
          });
          // } else {
          // dialogBuilder.findViewById(R.id.buttonAlarm).setEnabled(false);
          // }

          dialogBuilder.show();
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    ibCreateNote();
                    break;
          }

     }

     @Override public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
          ActiveRecord.currentNote.alarmHour = hourOfDay;
          ActiveRecord.currentNote.alarmMinute = minute;
          ActiveRecord.currentNote.alarmString = hourOfDay + ":" + ((minute < 10) ? ("0" + minute) : minute);

          ((TextView) dialogBuilder.findViewById(R.id.buttonAlarm)).setText(ActiveRecord.currentNote.alarmString);
     }
}