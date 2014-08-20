package com.ik.ggnote.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ik.ggnote.R;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;

public class Utils {
     private final static SimpleDateFormat dateFormat = new SimpleDateFormat();

     /**
      * @param monthNumber
      *             Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
      * @param year
      * @return amount of days in specific month
      */
     public static int getDaysInMonth(int monthNumber, int year) {
          int days = 0;
          if ( monthNumber >= 0 && monthNumber < 12 ) {
               try {
                    Calendar calendar = Calendar.getInstance();
                    int date = 1;
                    calendar.set(year, monthNumber, date);
                    days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
               } catch (Exception e) {
                    if ( e != null ) {
                         e.printStackTrace();
                    }
               }
          }
          return days;
     }

     public static String formatDate(Date dateToFormat, String pattern) {
          dateFormat.applyPattern(pattern);
          return dateFormat.format(dateToFormat).toString();
     }

     /**
      * "EEEE" - Thursday /////// "MMM" - Jun /////// "MM" - 06 /////// "yyyy" - 2013 /////// "dd"- 20 /////////
      * 
      * @param dateToFormat
      *             Date object
      * @param pattern
      *             one of EEEE, MMM, MM, yyyy, dd values
      */
     public static String getSpecificDateValue(Date dateToFormat, String pattern) {
          return android.text.format.DateFormat.format(pattern, dateToFormat).toString();
     }

     public static void showCustomToast(final Activity activity, final String message, final BitmapDrawable background) {
          activity.runOnUiThread(new Runnable() {

               @SuppressWarnings ( "deprecation" ) @Override public void run() {
                    try {
                         View layout = activity.getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

                         ImageView image = ((ImageView) layout.findViewById(R.id.toast_image_view));
                         TextView text = (TextView) layout.findViewById(R.id.toast_text_view);

                         text.setText(message);
                         image.setBackgroundDrawable(background);

                         Toast toast = new Toast(activity.getApplicationContext());

                         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                         toast.setDuration(Toast.LENGTH_SHORT);
                         toast.setView(layout);

                         toast.show();
                         // text.startAnimation(AnimationManager.load(android.R.anim.slide_in_left));
                    } catch (Exception e) {
                         e.printStackTrace();
                         Utils.showToast(activity.getApplicationContext(), message, true);
                    }
               }
          });
     }

     /**
      * Show short toast on UI thread
      * 
      * @param context
      *             application context or activity's context
      * @param message
      *             text representation of message to display
      * @param isShort
      *             define toast duration
      */
     public static void showToast(final Context context, final String message, boolean isShort) {
          final int toastDuration = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
          // Define toast duration
          try {
               Toast.makeText(context, message, toastDuration).show();
          } catch (Exception ex) {
               Activity activity = (Activity) context;
               activity.runOnUiThread(new Runnable() {

                    @Override public void run() {
                         Toast.makeText(context, message, toastDuration).show();
                    }
               });
          }
     }

     /**
      * Show custom toast with coloured background instead of image
      * 
      * @param activity
      *             current activity
      * @param message
      *             message to show
      * @param background
      *             value which represents color
      */
     public static void showCustomToastWithBackgroundColour(final Activity activity, final String message, final int background) {
          activity.runOnUiThread(new Runnable() {
               @Override public void run() {
                    try {
                         View layout = activity.getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

                         ImageView image = (ImageView) layout.findViewById(R.id.toast_image_view);
                         TextView text = (TextView) layout.findViewById(R.id.toast_text_view);

                         text.setText(message);
                         image.setBackgroundColor(background);

                         Toast toast = new Toast(activity.getApplicationContext());

                         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                         toast.setDuration(Toast.LENGTH_SHORT);
                         toast.setView(layout);

                         toast.show();
                         text.startAnimation(AnimationManager.load(android.R.anim.slide_in_left));
                    } catch (Exception e) {
                         Utils.logw(e.getMessage());
                         Utils.showToast(activity.getApplicationContext(), message, true);
                    }
               }
          });
     }

     public static void showCustomToast(final Activity activity, final String message, final int imageResourcesId) {
          activity.runOnUiThread(new Runnable() {

               @Override public void run() {
                    try {
                         View layout = activity.getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

                         ImageView image = ((ImageView) layout.findViewById(R.id.toast_image_view));
                         TextView text = (TextView) layout.findViewById(R.id.toast_text_view);

                         text.setText(message);
                         image.setImageResource(imageResourcesId);

                         Toast toast = new Toast(activity.getApplicationContext());

                         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                         toast.setDuration(Toast.LENGTH_SHORT);
                         toast.setView(layout);

                         toast.show();
                         // text.startAnimation(AnimationManager.load(android.R.anim.slide_in_left));
                    } catch (Exception e) {
                         e.printStackTrace();
                         Utils.showToast(activity.getApplicationContext(), message, true);
                    }
               }
          });
     }

     public static void showCustomToast(final Activity activity, final int messageResourcesId, final int imageResourcesId) {

          activity.runOnUiThread(new Runnable() {

               @Override public void run() {
                    try {
                         View layout = activity.getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

                         ImageView image = ((ImageView) layout.findViewById(R.id.toast_image_view));
                         TextView text = (TextView) layout.findViewById(R.id.toast_text_view);
                         text.setText(activity.getResources().getString(messageResourcesId));
                         image.setImageResource(imageResourcesId);

                         Toast toast = new Toast(activity.getApplicationContext());

                         toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                         toast.setDuration(Toast.LENGTH_SHORT);
                         toast.setView(layout);

                         toast.show();
                         // text.startAnimation(AnimationManager.load(android.R.anim.slide_in_left));
                    } catch (Exception e) {
                         e.printStackTrace();
                    }
               }
          });
     }

     public static boolean isNull(Object object) {
          return (null == object);
     }

     public static void logw(String message) {
          Log.d(Global.TAG, String.valueOf(message));
     }

     public static Resources getResources() {

          return null;
     }

     /**
      * Provide animation utils
      * 
      * @author Karpachev Ihor
      */
     public static class AnimationManager {

          private AnimationManager () {

          }

          /**
           * Load animation with default duration defined in xml file which describes animation
           * 
           * @param id
           * @return
           */
          public static Animation load(int id) {
               Animation animation = null;
               if ( null != ActiveRecord.context ) {
                    animation = AnimationUtils.loadAnimation(ActiveRecord.context, id);
               }
               return animation;
          }

          /**
           * Animate menu (appear/dissapear)
           * 
           * @param view
           * @param visibility
           * @param animationId
           * @param duration
           */
          public static void animateMenu(final View view, final int visibility, int animationId, int duration) {
               Animation animation = AnimationManager.load(animationId, duration);
               animation.setAnimationListener(new AnimationListener() {

                    @Override public void onAnimationEnd(Animation animation) {
                         view.setVisibility(visibility);
                    }

                    @Override public void onAnimationRepeat(Animation animation) {
                    }

                    @Override public void onAnimationStart(Animation animation) {
                    }
               });
               view.startAnimation(animation);
          }

          /**
           * Return animation with some duration, when duration equal 0, work same as AnimationManager.loadAnimation(id)
           * 
           * @param id
           * @param duration
           *             set animation duration or in case of 0 return default duration
           * @return animation to be played
           */
          public static Animation load(int id, int duration) {
               Animation animation = load(id);
               if ( null != animation ) {
                    if ( 0 == duration ) {
                         return animation;
                    } else {
                         animation.setDuration(duration);
                    }
               }
               return animation;
          }

          /**
           * Return animation with presetted duration
           * 
           * @param id
           * @param duration
           *             set animation duration or in case of 0 return default duration
           * @param startOffset
           *             set start offset animation in seconds
           * @return animation to be played
           */
          public static Animation load(int id, int duration, int startOffset) {
               Animation animation = null;
               if ( null != ActiveRecord.context ) {
                    animation = AnimationUtils.loadAnimation(ActiveRecord.context, id);
                    if ( 0 != duration ) {
                         animation.setDuration(duration);
                    }
                    animation.setStartOffset(startOffset);
               }
               return animation;
          }
     }

     /**
      * Get image id by note type
      * 
      * @param noteType
      *             string representation of note
      * @return image id if note type is correct, -1 otherwise
      */
     public static int getNoteImageIdFromNoteType(String noteType) {
          // EVENT IMAGE
          if ( noteType.equals(Global.NOTES.EVENT_STR) ) { return R.drawable.event; }
          // REMINDER IMAGE
          if ( noteType.equals(Global.NOTES.REMINDER_STR) ) { return R.drawable.reminder; }
          // SIMPLE IMAGE
          if ( noteType.equals(Global.NOTES.SIMPLE_STR) ) { return R.drawable.simple; }
          // URGENT IMAGE
          if ( noteType.equals(Global.NOTES.URGENT_STR) ) { return R.drawable.urgent; }
          // WORK IMAGE
          if ( noteType.equals(Global.NOTES.WORK_STR) ) { return R.drawable.work; }
          return -1;
     }

     /**
      * Get image id by note type
      * 
      * @param noteType
      *             string representation of note
      * @return image id if note type is correct, -1 otherwise
      */
     public static int getRadioButtonIdFromNoteType(String noteType) {
          // EVENT IMAGE
          if ( noteType.equals(Global.NOTES.EVENT_STR) ) { return R.id.rbEvent; }
          // REMINDER IMAGE
          if ( noteType.equals(Global.NOTES.REMINDER_STR) ) { return R.id.rbReminder; }
          // SIMPLE IMAGE
          if ( noteType.equals(Global.NOTES.SIMPLE_STR) ) { return R.id.rbSimple; }
          // URGENT IMAGE
          if ( noteType.equals(Global.NOTES.URGENT_STR) ) { return R.id.rbUrgent; }
          // WORK IMAGE
          if ( noteType.equals(Global.NOTES.WORK_STR) ) { return R.id.rbWork; }
          return -1;
     }

}
