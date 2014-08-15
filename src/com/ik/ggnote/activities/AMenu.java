package com.ik.ggnote.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;

@EActivity ( R.layout.activity_menu ) public class AMenu extends Activity {

     // =============================================== VIEWS
     @ViewById ImageButton ibCreateNote , ibLogout , ibExistingNotes;

     // =============================================== VARIABLES

     // =============================================== METHODS
     /**
      * Create new note
      */
     @Click void ibCreateNote() {
          startActivity(new Intent(this, ACreateNote_.class));
     }

     /**
      * View list of existing notes
      */
     @Click void ibExistingNotes() {
          startActivity(new Intent(this, AMyNotes_.class));
     }

     @Click void ibLogout() {
          final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

          dialogBuilder.withButton1Text("Ok").withButton2Text("Cancel").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Welcome to GGNote").withMessage("Do you really want logout?").setButton1Click(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    startActivity(new Intent(AMenu.this, AStart_.class));
                    dialogBuilder.dismiss();
               }
          }).setButton2Click(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          }).show();
     }
}