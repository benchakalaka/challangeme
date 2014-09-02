package com.ik.ggnote.activities;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.commons.lang3.time.DateUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.custom.CListViewItem;
import com.ik.ggnote.custom.CListViewItem_;
import com.ik.ggnote.custom.CSlideMenuNotes;
import com.ik.ggnote.custom.CSlideMenuNotes_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_my_notes ) public class AMyNotes extends ActionBarActivity implements OnClickListener {
     // =================================================== VIEWS
     @ViewById LinearLayout                               llMyNotes;
     @ViewById TextView                                   twDate , twMyNotes , twCompleted , twAmoutNotes , twAmoutFinished;
     @ViewById ImageView                                  ivPrevDay , ivNextDay;

     // =================================================== VARIABLES
     private List <ModelNote>                             myNotes;
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();
     private static Date                                  currentDate          = new Date();
     private CSlideMenuNotes                              menu;
     private static boolean                               loadCompleted        = true;
     @Pref AppSharedPreferences_                          appPref;

     // Setup listener
     public final com.roomorama.caldroid.CaldroidListener onDateChangeListener = new com.roomorama.caldroid.CaldroidListener() {
                                                                                    @Override public void onSelectDate(final Date date, View view) {
                                                                                         currentDate = date;
                                                                                         loadNotesAndCreateViews(date, loadCompleted);
                                                                                         calendar.dismiss();
                                                                                    }

                                                                                    @Override public void onChangeMonth(int month, int year) {
                                                                                    }

                                                                                    @Override public void onLongClickDate(Date date, View view) {
                                                                                    }

                                                                                    @Override public void onCaldroidViewCreated() {
                                                                                    }
                                                                               };
     private String[]                                     ORDERE_NOTES_ARRAY;

     // =================================================== METHODS
     @AfterViews void afterViews() {
          menu = CSlideMenuNotes_.build(this);
          setUpAmountOfCompletedAndAciveNotes();

          // configure persistant bundle for displaying calendar view
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, getResources().getString(R.string.select_date));
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);

          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // You customization
          actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ab_background)));

          actionBar.setIcon(R.drawable.menu);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.plus);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
     }

     /**
      * Listener on back press, show dialog - logout
      */
     @Override public void onBackPressed() {
          logout();
     }

     public void setUpAmountOfCompletedAndAciveNotes() {
          String dateNowInStringFormat = Utils.formatDate(currentDate, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD);
          // TODO: get from linearlayout, instead of db
          final int amountOfNotes = DatabaseUtils.getAllNotesCompletedByDate(dateNowInStringFormat, false).size();
          final int amountOfCompleted = DatabaseUtils.getAllNotesCompletedByDate(dateNowInStringFormat, true).size();

          menu.setUpAmountOfNotesAndCompleted(amountOfNotes, amountOfCompleted);

          YoYo.with(Techniques.ZoomIn).duration(700).withListener(new AnimatorListener() {

               @Override public void onAnimationStart(Animator arg0) {
                    twAmoutNotes.setText(String.valueOf(amountOfNotes));
               }

               @Override public void onAnimationRepeat(Animator arg0) {
               }

               @Override public void onAnimationEnd(Animator arg0) {
               }

               @Override public void onAnimationCancel(Animator arg0) {
               }
          }).playOn(twAmoutNotes);
          YoYo.with(Techniques.ZoomIn).duration(700).delay(200).withListener(new AnimatorListener() {

               @Override public void onAnimationStart(Animator arg0) {
                    twAmoutFinished.setText(String.valueOf(amountOfCompleted));

               }

               @Override public void onAnimationRepeat(Animator arg0) {
               }

               @Override public void onAnimationEnd(Animator arg0) {
               }

               @Override public void onAnimationCancel(Animator arg0) {
               }
          }).playOn(twAmoutFinished);
     }

     @Override protected void onResume() {
          super.onResume();
          ORDERE_NOTES_ARRAY = getNotesOrder();
          menu.setSelectedNotesItem();
          setUpCurrentDate(currentDate);
          loadNotesAndCreateViews(currentDate, loadCompleted);
          Animation anim = AnimationManager.load(R.anim.fade_in);
          anim.setDuration(200);
          if ( loadCompleted ) {
               ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.my_notes);
               twMyNotes.setTextColor(Color.BLACK);
               twMyNotes.setBackgroundColor(getResources().getColor(R.color.ab_background));
               twCompleted.setBackgroundColor(Color.TRANSPARENT);
               twCompleted.startAnimation(anim);
          } else {
               ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.completed);
               twCompleted.setTextColor(Color.WHITE);
               twCompleted.setBackgroundColor(getResources().getColor(R.color.ab_background));
               twMyNotes.setBackgroundColor(Color.TRANSPARENT);
               twMyNotes.startAnimation(anim);
          }

     }

     private String[] getNotesOrder() {
          String[] result = new String[5];
          result[appPref.orderNumberEvent().get()] = Global.NOTES.EVENT_STR;
          result[appPref.orderNumberReminder().get()] = Global.NOTES.REMINDER_STR;
          result[appPref.orderNumberSimple().get()] = Global.NOTES.SIMPLE_STR;
          result[appPref.orderNumberUrgent().get()] = Global.NOTES.URGENT_STR;
          result[appPref.orderNumberWork().get()] = Global.NOTES.WORK_STR;
          return result;
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.slide_left, 0);
     }

     @Click void twMyNotes() {
          if ( !loadCompleted ) {
               selectActiveOrFinishedNotes();
               loadNotesAndCreateViews(currentDate, loadCompleted);
          }
     }

     @Click void twCompleted() {
          if ( loadCompleted ) {
               selectActiveOrFinishedNotes();
               loadNotesAndCreateViews(currentDate, loadCompleted);
          }
     }

     private void selectActiveOrFinishedNotes() {
          Animation anim = AnimationManager.load(R.anim.fade_in);
          anim.setDuration(200);
          loadCompleted = !loadCompleted;
          // My Notes selected
          if ( loadCompleted ) {
               ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.my_notes);
               YoYo.with(Techniques.FlipInX).duration(500).playOn(getSupportActionBar().getCustomView().findViewById(R.id.text1));
               twMyNotes.setTextColor(Color.BLACK);
               twCompleted.setTextColor(Color.WHITE);
               twMyNotes.setBackgroundColor(getResources().getColor(R.color.ab_background));
               twCompleted.setBackgroundColor(Color.TRANSPARENT);
               YoYo.with(Techniques.ZoomIn).duration(300).playOn(twMyNotes);
          } else {
               ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.completed);
               YoYo.with(Techniques.FlipInX).duration(500).playOn(getSupportActionBar().getCustomView().findViewById(R.id.text1));
               twMyNotes.setTextColor(Color.WHITE);
               twCompleted.setTextColor(Color.BLACK);
               twCompleted.setBackgroundColor(getResources().getColor(R.color.ab_background));
               twMyNotes.setBackgroundColor(Color.TRANSPARENT);
               YoYo.with(Techniques.ZoomIn).duration(300).playOn(twCompleted);
          }
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          if ( menu.getMenu().isMenuShowing() ) {
               menu.getMenu().showContent();
          } else {
               menu.getMenu().showMenu();
          }
          return super.onOptionsItemSelected(item);
     }

     @Click void twDate() {
          YoYo.with(Techniques.BounceIn).duration(1000).withListener(new AnimatorListener() {

               @Override public void onAnimationStart(Animator arg0) {
               }

               @Override public void onAnimationRepeat(Animator arg0) {
                    // TODO Auto-generated method stub

               }

               @Override public void onAnimationEnd(Animator arg0) {
                    List <ModelNote> notes = DatabaseUtils.getAllNotes();
                    HashMap <Date, Integer> datesToHighligt = new HashMap <Date, Integer>();
                    if ( null != notes && !notes.isEmpty() ) {
                         calendar = new CaldroidFragment();
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
                    // TODO Auto-generated method stub

               }
          }).playOn(twDate);
     }

     private void setUpCurrentDate(Date dateToSetUp) {
          twDate.setText(Utils.formatDate(dateToSetUp, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD));
     }

     @Click void ivPrevDay() {
          ivPrevDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate));
          currentDate = DateUtils.addDays(currentDate, -1);
          loadNotesAndCreateViews(currentDate, loadCompleted);
     }

     @Click void ivNextDay() {
          ivNextDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate));
          currentDate = DateUtils.addDays(currentDate, 1);
          loadNotesAndCreateViews(currentDate, loadCompleted);
     }

     /**
      * Filter by name of note
      */
     public void ivFilter() {

          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.setContentView(R.layout.dialog_notes_filter);
          dialogBuilder.show();

          ((RadioGroup) dialogBuilder.findViewById(R.id.rgFilterType)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

               @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                         case R.id.rbByWord:
                              dialogBuilder.findViewById(R.id.etByWord).setVisibility(View.VISIBLE);
                              dialogBuilder.findViewById(R.id.spinnerByType).setVisibility(View.GONE);
                              dialogBuilder.findViewById(R.id.ivExpandFilter).setVisibility(View.GONE);
                              break;

                         case R.id.rbByType:
                              dialogBuilder.findViewById(R.id.ivExpandFilter).setVisibility(View.VISIBLE);
                              dialogBuilder.findViewById(R.id.spinnerByType).setVisibility(View.VISIBLE);
                              ArrayAdapter <String> adapter = new ArrayAdapter <String>(AMyNotes.this, R.layout.custom_spinner_item, getResources().getStringArray(R.array.array_notes_types));
                              ((Spinner) dialogBuilder.findViewById(R.id.spinnerByType)).setAdapter(adapter);
                              dialogBuilder.findViewById(R.id.etByWord).setVisibility(View.GONE);
                              break;
                    }
               }
          });

          dialogBuilder.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    switch (((RadioGroup) dialogBuilder.findViewById(R.id.rgFilterType)).getCheckedRadioButtonId()) {
                         case R.id.rbByWord:
                              String word = ((EditText) dialogBuilder.findViewById(R.id.etByWord)).getText().toString();
                              applyFilterByWord(word);
                              break;

                         case R.id.rbByType:
                              String spinnerValue = ((Spinner) dialogBuilder.findViewById(R.id.spinnerByType)).getSelectedItem().toString();
                              applyFilterByType(spinnerValue);
                              break;
                    }

                    dialogBuilder.dismiss();
               }
          });

          dialogBuilder.findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          });

     }

     /**
      * Load notes from database
      * 
      * @param loadCompleted
      */
     private void loadNotesAndCreateViews(final Date date, final boolean loadCompleted) {
          setUpCurrentDate(currentDate);
          setUpAmountOfCompletedAndAciveNotes();
          llMyNotes.removeAllViews();
          String dateNowInStringFormat = Utils.formatDate(date, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD);
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat, ORDERE_NOTES_ARRAY);
          for ( ModelNote note : myNotes ) {
               if ( note.isCompleted != loadCompleted ) {
                    CListViewItem item = CListViewItem_.build(AMyNotes.this, note, note.isCompleted);
                    llMyNotes.addView(item);
               }
          }
          for ( int i = 0; i < llMyNotes.getChildCount(); i++ ) {
               Animation animation = AnimationManager.load(R.anim.bounce);
               animation.setStartOffset(i * 150);
               llMyNotes.getChildAt(i).startAnimation(animation);
          }
     }

     /**
      * Apply filter by type
      */
     private void applyFilterByType(String type) {
          llMyNotes.removeAllViews();
          String dateNowInStringFormat = Utils.formatDate(currentDate, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD);
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat, ORDERE_NOTES_ARRAY);
          for ( ModelNote note : myNotes ) {
               if ( type.equals("Display all") || type.equals(note.noteType) ) {
                    CListViewItem item = CListViewItem_.build(this, note, false);
                    llMyNotes.addView(item);
               }
          }
          menu.getMenu().showContent();
     }

     /**
      * Apply filter by type
      */
     private void applyFilterByWord(String word) {
          llMyNotes.removeAllViews();
          String dateNowInStringFormat = Utils.formatDate(currentDate, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD);
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat, ORDERE_NOTES_ARRAY);
          for ( ModelNote note : myNotes ) {
               if ( note.description.contains(word) ) {
                    CListViewItem item = CListViewItem_.build(this, note, false);
                    llMyNotes.addView(item);
               }
          }
          menu.getMenu().showContent();
     }

     public void logout() {
          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);

          dialogBuilder.withButton1Text(getResources().getString(R.string.cancel)).withButton2Text(getResources().getString(android.R.string.ok)).withIcon(R.drawable.book).withEffect(Effectstype.Slit).withTitle(getResources().getString(R.string.logout))
                    .withMessage(getResources().getString(R.string.do_you_really_want_logout)).setButton1Click(new OnClickListener() {

                         @Override public void onClick(View v) {
                              dialogBuilder.dismiss();
                         }
                    }).setButton2Click(new View.OnClickListener() {
                         @Override public void onClick(View v) {
                              startActivity(new Intent(AMyNotes.this, AStart_.class));
                              dialogBuilder.dismiss();
                         }
                    }).show();
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    YoYo.with(Techniques.BounceIn).duration(500).withListener(new AnimatorListener() {

                         @Override public void onAnimationStart(Animator arg0) {
                         }

                         @Override public void onAnimationRepeat(Animator arg0) {
                         }

                         @Override public void onAnimationEnd(Animator arg0) {
                              // create note object
                              ActiveRecord.currentNote = new ModelNote(getApplicationContext());
                              ActiveRecord.currentNote.noteType = Global.NOTES.SIMPLE_STR;
                              startActivity(new Intent(AMyNotes.this, ACreateNote_.class));
                         }

                         @Override public void onAnimationCancel(Animator arg0) {
                         }
                    }).playOn(v);

                    break;
          }
     }

     @SuppressLint ( "NewApi" ) public static int getBackgroundColor(TextView textView) {
          ColorDrawable drawable = (ColorDrawable) textView.getBackground();
          if ( Build.VERSION.SDK_INT >= 11 ) { return drawable.getColor(); }
          try {
               Field field = drawable.getClass().getDeclaredField("mState");
               field.setAccessible(true);
               Object object = field.get(drawable);
               field = object.getClass().getDeclaredField("mUseColor");
               field.setAccessible(true);
               return field.getInt(object);
          } catch (Exception e) {
               // TODO: handle exception
          }
          return 0;
     }

}