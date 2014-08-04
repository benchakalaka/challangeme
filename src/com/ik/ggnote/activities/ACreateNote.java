package com.ik.ggnote.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ik.chalangeme.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_create_note ) public class ACreateNote extends FragmentActivity {
     // ===================================================================================== VIEWS
     @ViewById ImageView                                  ivDate;
     @ViewById ImageView                                  ivDraw;
     @ViewById ImageView                                  ivPinOnMap;
     @ViewById ImageView                                  ivPinPhoto;
     @ViewById Button                                     btnCreateNote;
     @ViewById EditText                                   etDescription;

     // ===================================================================================== VARIABLES
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();

     // Setup listener
     public final com.roomorama.caldroid.CaldroidListener onDateChangeListener = new com.roomorama.caldroid.CaldroidListener() {
                                                                                    @Override public void onSelectDate(final Date date, View view) {
                                                                                         Calendar now = Calendar.getInstance();
                                                                                         date.setHours(now.HOUR);
                                                                                         date.setMinutes(now.MINUTE);
                                                                                         date.setSeconds(now.SECOND);
                                                                                         ActiveRecord.currentNote.date = Utils.formatDate(date, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
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
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, "Select date");
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
          ActiveRecord.currentNote = new ModelNote(getApplicationContext());
     }

     @Click void ivPinPhoto() {
          captureCameraPhoto(this);
     }

     /**
      * If date of note different from today, user can set it
      */
     @Click void ivDate() {
          calendar = new CaldroidFragment();
          calendar.setCaldroidListener(onDateChangeListener);
          calendar.setArguments(bundle);
          // highlight dates in calendar with blue color
          calendar.show(getSupportFragmentManager(), Global.TAG);
     }

     /**
      * Draw and pin drawing to note
      */
     @Click void ivDraw() {
          startActivity(new Intent(this, ADrawingView_.class));
     }

     /**
      * Select location and pin to note
      */
     @Click void ivPinOnMap() {
          startActivity(new Intent(this, AMap_.class));
     }

     /**
      * Create ready note
      */
     @Click void btnCreateNote() {
          ActiveRecord.currentNote.description = etDescription.getText().toString();
          ActiveRecord.currentNote.pathToPhoto = "empty";
          if ( null != ActiveRecord.currentNote.location ) {
               ActiveRecord.currentNote.location.save();
          }
          ActiveRecord.currentNote.save();
          List <ModelNote> notes = ModelNote.listAll(ModelNote.class);
          for ( ModelNote i : notes ) {
               Utils.logw(i.toString());
          }
     }

     @OnActivityResult ( Global.CAPTURE_CAMERA_PHOTO ) void onResult(int resultCode, Intent data) {
          if ( resultCode == Activity.RESULT_OK ) {
               File photo = new File(ActiveRecord.currentNote.pathToPhoto);
               if ( (null != photo) && (photo.length() != 0) ) {
                    Utils.showCustomToast(this, R.string.success, R.drawable.triangle);
               } else {
                    Utils.showCustomToast(this, R.string.failed, R.drawable.triangle);
               }
          }
     }

     public void captureCameraPhoto(Activity activity) {
          try {
               // Create date formatter for filename
               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.ENGLISH);
               // format current date to string format
               String date = dateFormat.format(new Date());
               // construct filename
               ActiveRecord.currentNote.pathToPhoto = Global.APP_PHOTO_DIRECTORY + date + ".jpg";
               // craete file with constructed filename
               File photoFile = new File(ActiveRecord.currentNote.pathToPhoto);
               photoFile.createNewFile();

               // Construct Intent and show camera
               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
               activity.startActivityForResult(cameraIntent, Global.CAPTURE_CAMERA_PHOTO);
          } catch (Exception e) {
               Utils.logw(e.getMessage());
               Utils.showCustomToast(activity, R.string.failed, R.drawable.triangle);
          }
     }
}