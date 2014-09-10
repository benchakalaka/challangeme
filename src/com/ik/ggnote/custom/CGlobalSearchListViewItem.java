package com.ik.ggnote.custom;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ik.ggnote.R;
import com.ik.ggnote.activities.ANoteDetails;
import com.ik.ggnote.activities.ANoteDetails_;
import com.ik.ggnote.model.ModelNote;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EViewGroup ( R.layout.custom_list_item_global_search ) public class CGlobalSearchListViewItem extends RelativeLayout implements android.view.View.OnClickListener {

     // ============================================== VIEWS
     // image on left hand side
     @ViewById public ImageView image , ivCompleted;
     // description of the current note
     @ViewById public TextView  text , twTime;

     // ============================================== VARIABLES
     private final ModelNote    note;
     private final Context      context;

     // ============================================== METHODS
     public CGlobalSearchListViewItem ( Context context , ModelNote modelNote ) {
          super(context);
          this.note = modelNote;
          setOnClickListener(this);
          this.context = context;
     }

     @AfterViews void afterViews() {
          setUpNote();
     }

     /**
      * Set current note, display image and description
      * 
      * @param modelNote
      */
     private void setUpNote() {
          if ( null != note ) {
               int id = Utils.getNoteImageIdFromNoteType(note.noteType);
               if ( -1 != id ) {
                    image.setBackgroundResource(id);
               }
               twTime.setText(note.date);
               text.setText(note.description);

               if ( note.isCompleted ) {
                    ivCompleted.setImageResource(R.drawable.ok48selected);
               }
          }
     }

     /**
      * Handle click on list item
      */
     @Override public void onClick(View v) {
          v.startAnimation(AnimationManager.load(R.anim.fade_in));
          ANoteDetails.note = note;
          context.startActivity(new Intent(context, ANoteDetails_.class));
     }
}
