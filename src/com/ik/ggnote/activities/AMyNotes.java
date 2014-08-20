package com.ik.ggnote.activities;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
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
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.custom.CListViewItem;
import com.ik.ggnote.custom.CListViewItem_;
import com.ik.ggnote.custom.CSlideMenu;
import com.ik.ggnote.custom.CSlideMenu_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_my_notes ) public class AMyNotes extends ActionBarActivity implements OnClickListener {
     // =================================================== VIEWS
     @ViewById LinearLayout                               llMyNotes;
     @ViewById ImageButton                                ibBack;
     @ViewById TextView                                   twDate , twMyNotes , twCompleted;
     @ViewById ImageView                                  ivPrevDay , ivNextDay , ivFilter;

     // =================================================== VARIABLES
     private List <ModelNote>                             myNotes;
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();
     private Date                                         currentDate;
     private CSlideMenu                                   menu;
     private static boolean                               loadCompleted        = true;
     private int                                          selectedColor;

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

     // =================================================== METHODS
     @AfterViews void afterViews() {
          selectedColor = getResources().getColor(R.color.slide_menu_background);
          currentDate = new Date();
          // configure persistant bundle for displaying calendar view
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, "Select date");
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
          menu = CSlideMenu_.build(this);

          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_my_notes, null);

          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // You customization
          actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#98AFC7")));

          actionBar.setIcon(R.drawable.list);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivCreateNote).setOnClickListener(this);
     }

     @Override protected void onResume() {
          super.onResume();
          setUpCurrentDate(currentDate);
          loadNotesAndCreateViews(currentDate, loadCompleted);
          Animation anim = AnimationManager.load(R.anim.fade_in);
          anim.setDuration(200);
          if ( loadCompleted ) {
               twMyNotes.setBackgroundColor(selectedColor);
               twCompleted.setBackgroundColor(getResources().getColor(R.color.unselected_color));
               twCompleted.startAnimation(anim);
          } else {
               twCompleted.setBackgroundColor(selectedColor);
               twMyNotes.setBackgroundColor(getResources().getColor(R.color.unselected_color));
               twMyNotes.startAnimation(anim);
          }
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
               twMyNotes.setBackgroundColor(selectedColor);
               twCompleted.setBackgroundColor(getResources().getColor(R.color.unselected_color));
               YoYo.with(Techniques.ZoomIn).duration(500).playOn(twMyNotes);
          } else {
               twCompleted.setBackgroundColor(selectedColor);
               twMyNotes.setBackgroundColor(getResources().getColor(R.color.unselected_color));
               YoYo.with(Techniques.ZoomIn).duration(500).playOn(twCompleted);
          }
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          Utils.logw("rabotalo");
          if ( menu.getMenu().isMenuShowing() ) {
               menu.getMenu().showContent();
          } else {
               menu.getMenu().showMenu();
          }
          return super.onOptionsItemSelected(item);
     }

     @Click void twDate() {
          calendar = new CaldroidFragment();
          calendar.setCaldroidListener(onDateChangeListener);
          calendar.setArguments(bundle);
          // highlight dates in calendar with blue color
          calendar.show(getSupportFragmentManager(), Global.TAG);
     }

     private void setUpCurrentDate(Date dateToSetUp) {
          twDate.setText(Utils.formatDate(dateToSetUp, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD));
     }

     @Click void ivPrevDay() {
          ivPrevDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate_right));
          currentDate = DateUtils.addDays(currentDate, -1);
          loadNotesAndCreateViews(currentDate, loadCompleted);
     }

     @Click void ivNextDay() {
          ivNextDay.startAnimation(AnimationManager.load(R.anim.fade_in));
          twDate.startAnimation(AnimationManager.load(R.anim.rotate_left));
          currentDate = DateUtils.addDays(currentDate, 1);
          loadNotesAndCreateViews(currentDate, loadCompleted);
     }

     /**
      * Filter by name of note
      */
     @Click public void ivFilter() {

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

          llMyNotes.removeAllViews();
          String dateNowInStringFormat = Utils.formatDate(date, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD);
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat);
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
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat);
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
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat);
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

          dialogBuilder.withButton1Text("Ok").withButton2Text("Cancel").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Welcome to GGNote").withMessage("Do you really want logout?").setButton1Click(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    startActivity(new Intent(AMyNotes.this, AStart_.class));
                    dialogBuilder.dismiss();
               }
          }).setButton2Click(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          }).show();
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivCreateNote:
                    // TODO : load from resources androidannootaiioton
                    Animation animation = AnimationManager.load(R.anim.rotate_right);
                    animation.setAnimationListener(new AnimationListener() {
                         @Override public void onAnimationStart(Animation animation) {
                         }

                         @Override public void onAnimationRepeat(Animation animation) {
                         }

                         @Override public void onAnimationEnd(Animation animation) {
                              startActivity(new Intent(AMyNotes.this, ACreateNote_.class));
                         }
                    });
                    getSupportActionBar().getCustomView().findViewById(R.id.ivCreateNote).startAnimation(animation);

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