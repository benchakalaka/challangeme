package com.ik.ggnote.activities;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.custom.CGlobalSearchListViewItem;
import com.ik.ggnote.custom.CGlobalSearchListViewItem_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.AppSharedPreferences_;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_global_search ) public class AGlobalSearch extends ActionBarActivity {
     // =================================================== VIEWS
     @ViewById LinearLayout      llMyNotes;
     @ViewById ImageView         ivFilter , ivTreeStateFilter;
     @ViewById CheckBox          cbSortByDate;
     @ViewById EditText          etSearch;

     // =================================================== VARIABLES
     private List <ModelNote>    myNotes;
     @Pref AppSharedPreferences_ appPref;

     ArrayAdapter <String>       adapter;

     private int                 threeStateButtonImageId = R.drawable.all;

     // =================================================== METHODS
     @AfterViews void afterViews() {
          getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
          ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.text1)).setText(R.string.search);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setVisibility(View.INVISIBLE);

          ivFilter.setOnTouchListener(Utils.touchListener);

          adapter = new ArrayAdapter <String>(AGlobalSearch.this, R.layout.custom_spinner_item, getResources().getStringArray(R.array.array_notes_types));
          myNotes = DatabaseUtils.getAllNotes();
     }

     @CheckedChange void cbSortByDate(CompoundButton cb, boolean isChecked) {
          if ( isChecked ) {
               sortViewsByDateAsc();
               Utils.showStickyNotification(AGlobalSearch.this, R.string.sort_by_dates_old_first, AppMsg.STYLE_CONFIRM, 1500);
          } else {
               sortViewsByDateDesc();
               Utils.showStickyNotification(AGlobalSearch.this, R.string.sort_by_dates_new_first, AppMsg.STYLE_CONFIRM, 1500);
          }
     }

     @Click void ivTreeStateFilter() {
          switch (threeStateButtonImageId) {
               case R.drawable.all:
                    Utils.showStickyNotification(AGlobalSearch.this, R.string.filter_show_not_complete, AppMsg.STYLE_CONFIRM, 1500);
                    ivTreeStateFilter.setImageResource(R.drawable.ok48);
                    threeStateButtonImageId = R.drawable.ok48;
                    applyFilterByComletence(false);
                    break;

               case R.drawable.ok48:
                    Utils.showStickyNotification(AGlobalSearch.this, R.string.filter_show_complete, AppMsg.STYLE_CONFIRM, 1500);
                    ivTreeStateFilter.setImageResource(R.drawable.ok48selected);
                    threeStateButtonImageId = R.drawable.ok48selected;
                    applyFilterByComletence(true);
                    break;

               case R.drawable.ok48selected:
                    Utils.showStickyNotification(AGlobalSearch.this, R.string.filter_show_all, AppMsg.STYLE_CONFIRM, 1500);
                    ivTreeStateFilter.setImageResource(R.drawable.all);
                    threeStateButtonImageId = R.drawable.all;
                    loadNotesAndCreateViews();
                    break;
          }
     }

     @Click public void ivFilter() {
          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.setContentView(R.layout.dialog_notes_filter);
          ((Spinner) dialogBuilder.findViewById(R.id.spinnerByType)).setAdapter(adapter);

          dialogBuilder.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {

                    String spinnerValue = ((Spinner) dialogBuilder.findViewById(R.id.spinnerByType)).getSelectedItem().toString();
                    applyFilterByType(spinnerValue);
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

     @AfterTextChange void etSearch() {
          applyFilterByWord(etSearch.getText().toString());
     }

     @Override protected void onResume() {
          super.onResume();
          loadNotesAndCreateViews();
     }

     @Override protected void onPause() {
          super.onPause();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackPressed();
          return super.onOptionsItemSelected(item);
     }

     /**
      * Load notes from database
      * 
      * @param loadCompleted
      */
     private void loadNotesAndCreateViews() {
          llMyNotes.removeAllViews();
          for ( ModelNote note : myNotes ) {
               CGlobalSearchListViewItem item = CGlobalSearchListViewItem_.build(AGlobalSearch.this, note);
               llMyNotes.addView(item);
          }
          for ( int i = 0; i < llMyNotes.getChildCount(); i++ ) {
               Animation animation = AnimationManager.load(R.anim.bounce);
               animation.setStartOffset(i * 100);
               llMyNotes.getChildAt(i).startAnimation(animation);
          }
     }

     /**
      * Load notes from database
      * 
      * @param loadCompleted
      */
     private void sortViewsByDateAsc() {
          llMyNotes.removeAllViews();
          Collections.sort(myNotes, new Comparator <ModelNote>() {
               @Override public int compare(ModelNote note1, ModelNote note2) {
                    return note1.date.compareToIgnoreCase(note2.date);
               }
          });

          for ( ModelNote note : myNotes ) {
               CGlobalSearchListViewItem item = null;
               if ( threeStateButtonImageId == R.drawable.ok48 && note.isCompleted == false ) {
                    item = CGlobalSearchListViewItem_.build(this, note);
               }

               if ( threeStateButtonImageId == R.drawable.ok48selected && note.isCompleted == true ) {
                    item = CGlobalSearchListViewItem_.build(this, note);
               }

               if ( threeStateButtonImageId == R.drawable.all ) {
                    item = CGlobalSearchListViewItem_.build(this, note);
               }
               if ( null != item ) {
                    llMyNotes.addView(item);
               }
          }
          for ( int i = 0; i < llMyNotes.getChildCount(); i++ ) {
               Animation animation = AnimationManager.load(R.anim.bounce);
               animation.setStartOffset(i * 100);
               llMyNotes.getChildAt(i).startAnimation(animation);
          }
     }

     /**
      * Load notes from database
      * 
      * @param loadCompleted
      */
     private void sortViewsByDateDesc() {
          llMyNotes.removeAllViews();
          Collections.sort(myNotes, new Comparator <ModelNote>() {
               @Override public int compare(ModelNote note1, ModelNote note2) {
                    return note2.date.compareToIgnoreCase(note1.date);
               }
          });

          for ( ModelNote note : myNotes ) {
               CGlobalSearchListViewItem item = null;
               if ( threeStateButtonImageId == R.drawable.ok48 && note.isCompleted == false ) {
                    item = CGlobalSearchListViewItem_.build(this, note);
               }

               if ( threeStateButtonImageId == R.drawable.ok48selected && note.isCompleted == true ) {
                    item = CGlobalSearchListViewItem_.build(this, note);
               }

               if ( threeStateButtonImageId == R.drawable.all ) {
                    item = CGlobalSearchListViewItem_.build(this, note);
               }
               if ( null != item ) {
                    llMyNotes.addView(item);
               }
          }
          for ( int i = 0; i < llMyNotes.getChildCount(); i++ ) {
               Animation animation = AnimationManager.load(R.anim.bounce);
               animation.setStartOffset(i * 100);
               llMyNotes.getChildAt(i).startAnimation(animation);
          }
     }

     /**
      * Apply filter by type
      */
     private void applyFilterByType(String type) {
          llMyNotes.removeAllViews();
          for ( ModelNote note : myNotes ) {
               if ( type.equals("Display all") || type.equals(note.noteType) ) {
                    CGlobalSearchListViewItem item = CGlobalSearchListViewItem_.build(this, note);
                    llMyNotes.addView(item);
               }
          }

          for ( int i = 0; i < llMyNotes.getChildCount(); i++ ) {
               Animation animation = AnimationManager.load(R.anim.bounce);
               animation.setStartOffset(i * 100);
               llMyNotes.getChildAt(i).startAnimation(animation);
          }
     }

     /**
      * Apply filter by type
      */
     private void applyFilterByComletence(boolean isCompleted) {
          llMyNotes.removeAllViews();
          for ( ModelNote note : myNotes ) {
               if ( isCompleted == note.isCompleted ) {
                    CGlobalSearchListViewItem item = CGlobalSearchListViewItem_.build(this, note);
                    llMyNotes.addView(item);
               }
          }
          for ( int i = 0; i < llMyNotes.getChildCount(); i++ ) {
               Animation animation = AnimationManager.load(R.anim.bounce);
               animation.setStartOffset(i * 100);
               llMyNotes.getChildAt(i).startAnimation(animation);
          }
     }

     /**
      * Apply filter by type
      */
     private void applyFilterByWord(String word) {
          llMyNotes.removeAllViews();
          for ( ModelNote note : myNotes ) {
               if ( note.description.contains(word) ) {
                    CGlobalSearchListViewItem item = null;
                    if ( threeStateButtonImageId == R.drawable.ok48 && note.isCompleted == false ) {
                         item = CGlobalSearchListViewItem_.build(this, note);
                    }

                    if ( threeStateButtonImageId == R.drawable.ok48selected && note.isCompleted == true ) {
                         item = CGlobalSearchListViewItem_.build(this, note);
                    }

                    if ( threeStateButtonImageId == R.drawable.all ) {
                         item = CGlobalSearchListViewItem_.build(this, note);
                    }
                    if ( null != item ) {
                         llMyNotes.addView(item);
                    }
               }
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
               e.printStackTrace();
               // TODO: handle exception
          }
          return 0;
     }
}