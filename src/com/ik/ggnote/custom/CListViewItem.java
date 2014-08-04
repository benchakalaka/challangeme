package com.ik.ggnote.custom;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ik.chalangeme.R;

@EViewGroup ( R.layout.list_item ) public class CListViewItem extends RelativeLayout {

     // ============================================== VIEWS
     @ViewById ImageView image;
     @ViewById TextView  text;

     // ============================================== VARIABLES

     // ============================================== METHODS
     public CListViewItem ( Context context ) {
          super(context);
     }
}
