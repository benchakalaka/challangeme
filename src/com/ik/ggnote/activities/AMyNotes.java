package com.ik.ggnote.activities;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

import com.ik.ggnote.R;
import com.ik.ggnote.custom.CListViewItem;
import com.ik.ggnote.custom.CListViewItem_;
import com.ik.ggnote.model.ModelNote;

@EActivity ( R.layout.activity_my_notes ) public class AMyNotes extends FragmentActivity {
     // =================================================== VIEWS
     @ViewById LinearLayout   llMyNotes;

     // =================================================== VARIABLES
     private List <ModelNote> myNotes;

     // =================================================== METHODS
     @AfterViews void afterViews() {
          loadNotesAndCreateViews();
     }

     /**
      * Load notes from database
      */
     private void loadNotesAndCreateViews() {
          myNotes = ModelNote.listAll(ModelNote.class);
          for ( ModelNote note : myNotes ) {
               CListViewItem item = CListViewItem_.build(this, note);
               llMyNotes.addView(item);
          }
     }

}
