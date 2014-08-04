package com.ik.ggnote.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import com.ik.chalangeme.R;

@EActivity ( R.layout.activity_menu ) public class AMenu extends Activity {

     // =============================================== VIEWS
     @ViewById Button btnCreateNote;
     @ViewById Button btnExistingNotes;

     // =============================================== VARIABLES

     // =============================================== METHODS
     /**
      * Create new note
      */
     @Click void btnCreateNote() {
          startActivity(new Intent(this, ACreateNote_.class));
     }

     /**
      * View list of existing notes
      */
     @Click void btnExistingNotes() {
          startActivity(new Intent(this, AMyNotes_.class));
     }
}