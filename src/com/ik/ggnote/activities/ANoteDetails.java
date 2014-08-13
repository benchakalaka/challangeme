package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.ik.ggnote.R;
import com.ik.ggnote.custom.CDrawingView;
import com.ik.ggnote.model.ModelNote;

@EActivity ( R.layout.activity_note_details ) public class ANoteDetails extends FragmentActivity {

     public static ModelNote note;
     // =============================================== VIEWS
     @ViewById TextView      twTime;
     @ViewById TextView      twDescription;
     @ViewById CDrawingView  drawingView;

     // =============================================== VARIABLES

     // =============================================== METHODS
     @AfterViews void afterViews() {
          twTime.setText(note.date);
          twDescription.setText(note.description);
     }
}