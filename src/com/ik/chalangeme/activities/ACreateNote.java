package com.ik.chalangeme.activities;

import java.util.Calendar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ik.chalangeme.R;
import com.ik.chalangeme.constants.ActiveRecord;
import com.ik.chalangeme.constants.Global;
import com.ik.chalangeme.model.ModelNote;
import com.ik.chalangeme.utils.DatabaseUtils;
import com.ik.chalangeme.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;

@EActivity ( R.layout.activity_create_note ) public class ACreateNote extends FragmentActivity {
     // ============================== VIEWS
     @ViewById ImageView                                  ivDate;
     @ViewById ImageView                                  ivDraw;
     @ViewById ImageView                                  ivPinOnMap;
     @ViewById Button                                     btnCreateNote;
     @ViewById EditText                                   etDescription;

     // ============================== VARIABLES
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

     // ============================== METHODS
     @AfterViews void afterViews() {
          bundle.putString(com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE, "Select date");
          bundle.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
          ActiveRecord.currentNote = new ModelNote(getApplicationContext());
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

          ActiveRecord.currentNote.pathToDrawing = "empty for now";
          ActiveRecord.currentNote.pathToPhoto = "not ready for now";
          ActiveRecord.currentNote.save();

          List <ModelNote> notes = ModelNote.listAll(ModelNote.class);

          for ( ModelNote i : notes ) {
               Utils.logw(i.date);
          }
     }
}