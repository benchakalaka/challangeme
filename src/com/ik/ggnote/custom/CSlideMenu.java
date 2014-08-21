package com.ik.ggnote.custom;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ik.ggnote.R;
import com.ik.ggnote.activities.AMyNotes;
import com.ik.ggnote.activities.ASettings_;
import com.ik.ggnote.utils.Utils.AnimationManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

@EViewGroup ( R.layout.menu_drawing_companies_activity ) public class CSlideMenu extends ScrollView implements OnOpenListener , OnCloseListener {

     // Parent activity
     private final AMyNotes   activity;
     // Side slide menu
     private SlidingMenu      menu;

     // load views
     @ViewById RelativeLayout notes;
     @ViewById RelativeLayout filter;
     @ViewById RelativeLayout settings;
     @ViewById RelativeLayout logout;

     /**
      * Default constructor
      * 
      * @param context
      *             object of parent activity
      */
     public CSlideMenu ( Context context ) {
          super(context);
          this.activity = (AMyNotes) context;
     }

     @AfterViews void afterViews() {
          // configure the SlidingMenu
          // Point size = new Point();
          // activity.getWindowManager().getDefaultDisplay().getSize(size);
          menu = new SlidingMenu(activity);
          menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
          menu.setShadowWidthRes(R.dimen.shadow_width);
          menu.setBehindOffset(/* (int) (size.x * 0.80) */100);
          menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
          menu.setMenu(this);
          menu.setAlwaysDrawnWithCacheEnabled(true);
          menu.setMode(SlidingMenu.LEFT);

          menu.setOnOpenListener(this);
          menu.setOnCloseListener(this);
     }

     /**
      * Return menu object
      * 
      * @return SlidingMenu
      */
     public SlidingMenu getMenu() {
          return menu;
     }

     /**
      * Undo operation for drawing
      */
     @Click void notes() {
          notes.startAnimation(AnimationManager.load(android.R.anim.fade_out));
          this.activity.onOptionsItemSelected(null);
     }

     /**
      * Redo operation for drawing
      */
     @Click void filter() {
          filter.startAnimation(AnimationManager.load(android.R.anim.fade_out));
          this.activity.ivFilter();
     }

     /**
      * Change floor/area
      */
     @Click void settings() {
          settings.startAnimation(AnimationManager.load(android.R.anim.fade_out));
          this.activity.startActivity(new Intent(this.activity, ASettings_.class));
     }

     /**
      * Diaplay change brush size menu
      */
     @Click void logout() {
          logout.startAnimation(AnimationManager.load(android.R.anim.fade_out));
          this.activity.logout();
     }

     /**
      * Callback which will be invoked when menu is start close
      */
     @Override public void onClose() {
     }

     /**
      * Callback which will be invoked when menu is start open
      */
     @Override public void onOpen() {
     }
}
