package com.ik.ggnote.custom;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ik.ggnote.R;
import com.ik.ggnote.activities.AMyNotes;
import com.ik.ggnote.activities.ANoteDetails;
import com.ik.ggnote.activities.ANoteDetails_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

@EViewGroup ( R.layout.custom_list_item ) public class CListViewItem extends RelativeLayout implements android.view.View.OnClickListener {

     // ============================================== VIEWS
     // image on left hand side
     @ViewById public ImageView image;
     // description of the current note
     @ViewById public TextView  text;
     // delete note iv
     @ViewById public ImageView ivDeleteOrCompleteNote;

     // ============================================== VARIABLES
     private final ModelNote    note;
     private final Context      context;

     private boolean            isInDeletedMode = false;

     // ============================================== METHODS
     public CListViewItem ( Context context , ModelNote modelNote , boolean deleteMode ) {
          super(context);
          this.note = modelNote;
          setOnClickListener(this);
          this.context = context;
          isInDeletedMode = deleteMode;
     }

     @AfterViews void afterViews() {
          setNote();
          if ( isInDeletedMode ) {
               ivDeleteOrCompleteNote.setBackgroundResource(R.drawable.delete);
          }
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
      * Mark as finished
      */
     public void completeItem() {
          if ( null != note ) {

               YoYo.with(Techniques.FlipOutY).duration(700).withListener(new AnimatorListener() {

                    @Override public void onAnimationStart(Animator arg0) {
                    }

                    @Override public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override public void onAnimationEnd(Animator arg0) {
                         // item go to completed list
                         note.isCompleted = true;
                         note.save();
                         setVisibility(View.GONE);
                         ((AMyNotes) context).setUpAmountOfCompletedAndAciveNotes();
                    }

                    @Override public void onAnimationCancel(Animator arg0) {
                    }
               }).playOn(this);
          }
     }

     /**
      * Delete note from database
      */
     public void deleteNote() {
          if ( null != note ) {

               YoYo.with(Techniques.RollOut).duration(700).withListener(new AnimatorListener() {

                    @Override public void onAnimationStart(Animator arg0) {
                    }

                    @Override public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override public void onAnimationEnd(Animator arg0) {
                         note.delete();
                         setVisibility(View.GONE);
                         ((AMyNotes) context).setUpAmountOfCompletedAndAciveNotes();
                    }

                    @Override public void onAnimationCancel(Animator arg0) {
                    }
               }).playOn(this);
          }
     }

     /**
      * Delete or complete item, depends on screen (if list of my notes - > complete item, otherwise delete item)
      */
     @Click void ivDeleteOrCompleteNote() {
          if ( isInDeletedMode ) {
               deleteNote();
          } else {
               completeItem();
          }
     }

     @Override public void onClick(View v) {
          ANoteDetails.note = note;
          context.startActivity(new Intent(context, ANoteDetails_.class));
     }
}
