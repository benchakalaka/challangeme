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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_create_note ) public class ACreateNote extends FragmentActivity {
     // ===================================================================================== VIEWS
     @ViewById ImageButton                                ibDate;
     @ViewById ImageButton                                ibDraw;
     @ViewById ImageButton                                ibPinOnMap;
     @ViewById ImageButton                                ibPinPhoto;
     @ViewById ImageButton                                ibNoteType;
     @ViewById ImageButton                                ibCreateNote;

     @ViewById ImageView                                  ivDateDone;
     @ViewById ImageView                                  ivDrawDone;
     @ViewById ImageView                                  ivPinOnMapDone;
     @ViewById ImageView                                  ivPinPhotoDone;

     @ViewById EditText                                   etDescription;

     // ===================================================================================== VARIABLES
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();

     // Setup listener
     public final com.roomorama.caldroid.CaldroidListener onDateChangeListener = new com.roomorama.caldroid.CaldroidListener() {
                                                                                    @Override public void onSelectDate(final Date date, View view) {
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
          // configure persistant bundle for displaying calendar view
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, "Select date");
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
          // create note object
          ActiveRecord.currentNote = new ModelNote(getApplicationContext());
          ActiveRecord.currentNote.noteType = Global.NOTES.SIMPLE_STR;
     }

     @Click void ibPinPhoto() {
          captureCameraPhoto(this);
     }

     /**
      * If date of note different from today, user can set it
      */
     @Click void ibDate() {
          calendar = new CaldroidFragment();
          calendar.setCaldroidListener(onDateChangeListener);
          calendar.setArguments(bundle);
          // highlight dates in calendar with blue color
          calendar.show(getSupportFragmentManager(), Global.TAG);
     }

     @Override protected void onResume() {
          super.onResume();
          // set up current date
          Date now = new Date();
          // set date of creating note to current date by default
          ActiveRecord.currentNote.date = Utils.formatDate(now, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);

          displayDoneImages();
     }

     private void displayDoneImages() {
          if ( null != ActiveRecord.currentNote ) {
               // display all time
               ivDateDone.setVisibility(View.VISIBLE);
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
          startActivity(new Intent(this, AMap_.class));
     }

     /**
      * Create ready note
      */
     @Click void ibCreateNote() {
          // set note description
          ActiveRecord.currentNote.description = etDescription.getText().toString();
          // if user set location, save it (sugar lib not saving it by cascade)
          if ( null != ActiveRecord.currentNote.location ) {
               ActiveRecord.currentNote.location.save();
          }
          // save note
          ActiveRecord.currentNote.save();

          final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

          dialogBuilder.withButton1Text("Ok").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Success").withMessage("Note has been created successfully.").setButton1Click(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
                    startActivity(new Intent(ACreateNote.this, AMenu_.class));
               }
          }).show();
     }

     @OnActivityResult ( Global.CAPTURE_CAMERA_PHOTO ) void onResult(int resultCode, Intent data) {
          if ( resultCode == Activity.RESULT_OK ) {
               try {
                    File photo = new File(ActiveRecord.currentNote.pathToPhoto);
                    if ( (null != photo) && (photo.length() != 0) ) {
                         Utils.showCustomToast(this, R.string.success, R.drawable.triangle);
                    } else {
                         Utils.showCustomToast(this, R.string.failed, R.drawable.triangle);
                    }
               } catch (Exception ex) {
                    ex.printStackTrace();
                    Utils.showCustomToast(ACreateNote.this, "Ne sozdalos' photo", R.drawable.unsuccess);
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
                    Utils.showCustomToast(ACreateNote.this, R.string.password, R.drawable.share);
               }
          } catch (Exception e) {
               Utils.logw(e.getMessage());
               Utils.showCustomToast(activity, R.string.failed, R.drawable.triangle);
          }
     }

     @Click void ibNoteType() {
          showNoteTypePopup();
     }

     private void showNoteTypePopup() {
          final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
          dialogBuilder.setContentView(R.layout.dialog_type_of_note);

          int imageId = Utils.getNoteImageIdFromNoteType(ActiveRecord.currentNote.noteType);
          int buttonId = Utils.getRadioButtonIdFromNoteType(ActiveRecord.currentNote.noteType);
          if ( imageId != -1 ) {
               ((ImageView) dialogBuilder.findViewById(R.id.ivNoteType)).setBackgroundResource(imageId);
               ((RadioGroup) dialogBuilder.findViewById(R.id.rgTypeOfNote)).check(buttonId);
               // switch (imageId) {
               // case R.drawable.simple:
               //
               // break;
               //
               // case R.drawable.event:
               // break;
               //
               // case R.drawable.urgent:
               // break;
               //
               // case R.drawable.work:
               // break;
               //
               // case R.drawable.reminder:
               // break;
               // }
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
}