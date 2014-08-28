package com.ik.ggnote.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_create_note ) public class ACreateNote extends ActionBarActivity implements OnClickListener {
     // ===================================================================================== VIEWS
     @ViewById ImageButton                                ibDraw , ibPinOnMap , ibPinPhoto , ibNoteType;

     @ViewById ImageView                                  ibNoteTypeDone , ivDrawDone , ivPrevDay , ivNextDay , ivPinOnMapDone , ivPinPhotoDone;

     @ViewById EditText                                   etDescription;
     @ViewById TextView                                   twDate;

     // ===================================================================================== VARIABLES
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();
     // set up current date
     private static Date                                  currentDate          = new Date();
     // displaying progress
     private ProgressDialog                               progressDialog;

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
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.ok);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text)).setText(R.string.add_note);

          progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
          progressDialog.setMessage(getResources().getString(R.string.please_wait));
          progressDialog.setCancelable(true);
     }

     /**
      * Change date
      */
     @Click void twDate() {
          calendar = new CaldroidFragment();
          calendar.setCaldroidListener(onDateChangeListener);
          calendar.setArguments(bundle);
          // highlight dates in calendar with blue color
          calendar.show(getSupportFragmentManager(), Global.TAG);
     }

     @Click void ivPrevDay() {
          ivPrevDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate_right));
          currentDate = DateUtils.addDays(currentDate, -1);
          setUpCurrentDate(currentDate);
     }

     @Click void ivNextDay() {
          ivNextDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate_left));
          currentDate = DateUtils.addDays(currentDate, 1);
          setUpCurrentDate(currentDate);
     }

     @Click void ibPinPhoto() {
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
               // TODO: load from resources
               Animation anim1 = AnimationManager.load(R.anim.bounce);
               Animation anim2 = AnimationManager.load(R.anim.bounce);
               Animation anim3 = AnimationManager.load(R.anim.bounce);
               Animation anim4 = AnimationManager.load(R.anim.bounce);

               anim1.setStartOffset(100);
               anim2.setStartOffset(200);
               anim3.setStartOffset(300);
               anim4.setStartOffset(400);

               anim1.setDuration(800);
               anim2.setDuration(800);
               anim3.setDuration(800);
               anim4.setDuration(800);

               ibDraw.startAnimation(anim1);
               ibPinOnMap.startAnimation(anim2);
               ibPinPhoto.startAnimation(anim3);
               ibNoteType.startAnimation(anim4);
               // YoYo.with(Techniques.SlideInLeft).delay(400).duration(500).playOn(ibNoteType);
               // YoYo.with(Techniques.SlideInLeft).delay(600).duration(500).playOn(ibPinPhoto);
               // YoYo.with(Techniques.SlideInLeft).delay(800).duration(500).playOn(ibPinOnMap);
               // YoYo.with(Techniques.SlideInLeft).delay(1000).duration(500).playOn(ibDraw);
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

     /**
      * Draw and pin drawing to note
      */
     @Click void ibDraw() {
          startActivity(new Intent(this, ADrawingView_.class));
     }

     /**
      * Select location and pin to note
      */
     @Click void ibPinOnMap() {
          progressDialog.show();
          startActivity(new Intent(this, AMap_.class));
          progressDialog.dismiss();
     }

     private void setUpCurrentDate(Date dateToSetUp) {
          twDate.setText(Utils.formatDate(dateToSetUp, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD));
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          startActivity(new Intent(this, AMyNotes_.class));
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

          Utils.showCustomToast(ACreateNote.this, R.string.note_has_been_created, R.drawable.book);

          startActivity(new Intent(ACreateNote.this, AMyNotes_.class));
     }

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
          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.setContentView(R.layout.dialog_type_of_note);

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

          dialogBuilder.show();
     }

     /*
      * @Override protected void onPause() {
      * super.onPause();
      * overridePendingTransition(R.anim.slide_right, 0);
      * }
      */

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    // TODO : load from resources androidannootaiioton
                    Animation animation = AnimationManager.load(R.anim.bounce);
                    animation.setAnimationListener(new AnimationListener() {
                         @Override public void onAnimationStart(Animation animation) {
                         }

                         @Override public void onAnimationRepeat(Animation animation) {
                         }

                         @Override public void onAnimationEnd(Animation animation) {
                              ibCreateNote();
                         }
                    });
                    getSupportActionBar().getCustomView().findViewById(R.id.ivRightOkButton).startAnimation(animation);

                    break;
          }

     }
}