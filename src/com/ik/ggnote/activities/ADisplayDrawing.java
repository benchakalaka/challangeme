package com.ik.ggnote.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.custom.CDrawingView;
import com.ik.ggnote.model.PathSerializable;

@EActivity ( R.layout.activity_display_drawing ) public class ADisplayDrawing extends FragmentActivity {

     // ================================= VIEWS
     @ViewById ImageButton  ibBack;
     @ViewById ImageView    ivLock;

     // ================================= VARIABLES
     // canvas
     @ViewById CDrawingView cDisplayDrawingView;

     @AfterViews void afterViews() {
          try {
               ObjectInputStream oin = new ObjectInputStream(new FileInputStream(new File(ANoteDetails.note.pathToDrawing)));
               @SuppressWarnings ( "unchecked" )
               List <PathSerializable> paths = (List <PathSerializable>) oin.readObject();
               oin.close();
               cDisplayDrawingView.setPaths(paths);
               cDisplayDrawingView.invalidate();
               cDisplayDrawingView.setColor(Color.TRANSPARENT);
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     @Click void ivLock() {
          final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

          dialogBuilder.withButton1Text("Ok").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("GGNote").withMessage("You cannot modify this note").setButton1Click(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          }).show();
     }

     @Click void ibBack() {
          onBackPressed();
     }

}