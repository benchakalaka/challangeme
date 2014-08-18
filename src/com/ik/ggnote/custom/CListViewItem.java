package com.ik.ggnote.custom;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ik.ggnote.R;
import com.ik.ggnote.activities.ANoteDetails;
import com.ik.ggnote.activities.ANoteDetails_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EViewGroup ( R.layout.custom_list_item ) public class CListViewItem extends RelativeLayout implements android.view.View.OnClickListener {

     // ============================================== VIEWS
     // image on left hand side
     @ViewById public ImageView image;
     // description of the current note
     @ViewById public TextView  text;
     // delete note iv
     @ViewById public ImageView ivDeleteNote;

     // ============================================== VARIABLES
     private final ModelNote    note;
     private final Context      context;

     // ============================================== METHODS
     public CListViewItem ( Context context , ModelNote modelNote ) {
          super(context);
          this.note = modelNote;
          setOnClickListener(this);
          this.context = context;
     }

     @AfterViews void afterViews() {
          setNote();
     }

     /**
      * Set current note, display image and description
      * 
      * @param modelNote
      */
     private void setNote() {
          if ( null != note ) {
               int id = Utils.getNoteImageIdFromNoteType(note.noteType);
               if ( -1 != id ) {
                    image.setBackgroundResource(id);
               }
               text.setText(note.description);
          } else {
               Utils.logw("CListViewItem :: setNote : note == null");
          }
     }

     /**
      * Delete note from database
      */
     public void deleteNote() {
          if ( null != note ) {
               Animation animation = AnimationManager.load(R.anim.fade_out);
               animation.setDuration(2000);
               animation.setAnimationListener(new AnimationListener() {

                    @Override public void onAnimationStart(Animation animation) {
                    }

                    @Override public void onAnimationRepeat(Animation animation) {
                    }

                    @Override public void onAnimationEnd(Animation animation) {
                         note.delete();
                         setVisibility(View.GONE);
                    }
               });
               startAnimation(animation);
          }
     }

     @Click void ivDeleteNote() {
          deleteNote();
     }

     @Override public void onClick(View v) {
          ANoteDetails.note = note;
          context.startActivity(new Intent(context, ANoteDetails_.class));
     }
}
