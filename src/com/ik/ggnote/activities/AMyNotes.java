package com.ik.ggnote.activities;

import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ik.ggnote.R;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.custom.CListViewItem;
import com.ik.ggnote.custom.CListViewItem_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.DatabaseUtils;
import com.ik.ggnote.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_my_notes ) public class AMyNotes extends FragmentActivity {
     // =================================================== VIEWS
     @ViewById LinearLayout                               llMyNotes;
     @ViewById ImageButton                                ibBack , ibDate , ibFilter;

     // =================================================== VARIABLES
     private List <ModelNote>                             myNotes;
     private CaldroidFragment                             calendar;
     private final Bundle                                 bundle               = new Bundle();

     // Setup listener
     public final com.roomorama.caldroid.CaldroidListener onDateChangeListener = new com.roomorama.caldroid.CaldroidListener() {
                                                                                    @Override public void onSelectDate(final Date date, View view) {
                                                                                         loadNotesAndCreateViews(date);
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
          loadNotesAndCreateViews(new Date());
          // configure persistant bundle for displaying calendar view
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, "Select date");
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
     }

     /**
      * Back from my notes to menu
      */
     @Click void ibBack() {
          startActivity(new Intent(AMyNotes.this, AMenu_.class));
     }

     /**
      * Filter by name of note
      */
     @Click void ibFilter() {
          Utils.showCustomToast(AMyNotes.this, "FILTER", R.drawable.filter);
     }

     /**
      * Change date
      */
     @Click void ibDate() {
          calendar = new CaldroidFragment();
          calendar.setCaldroidListener(onDateChangeListener);
          calendar.setArguments(bundle);
          // highlight dates in calendar with blue color
          calendar.show(getSupportFragmentManager(), Global.TAG);
     }

     /**
      * Load notes from database
      */
     private void loadNotesAndCreateViews(Date date) {
          llMyNotes.removeAllViews();
          String dateNowInStringFormat = Utils.formatDate(date, DatabaseUtils.DATE_PATTERN_YYYY_MM_DD);
          myNotes = DatabaseUtils.getAllNotesByDate(dateNowInStringFormat);
          for ( ModelNote note : myNotes ) {
               CListViewItem item = CListViewItem_.build(this, note);
               llMyNotes.addView(item);
          }
     }
}