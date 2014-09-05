package com.ik.ggnote.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ik.ggnote.R;
import com.ik.ggnote.activities.ANoteDetails;
import com.ik.ggnote.activities.ANoteDetails_;
import com.ik.ggnote.activities.AStart_;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.constants.Global;
import com.ik.ggnote.model.ModelNote;

public class ReminderManager extends BroadcastReceiver {

     final public static String ONE_TIME = "onetime";

     @Override public void onReceive(Context context, Intent intent) {
          PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
          PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Global.TAG);
          // Acquire the lock
          wl.acquire();

          int requestCode = intent.getExtras().getInt("requestCode");
          ModelNote note = ModelNote.findById(ModelNote.class, Long.valueOf(requestCode));

          NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

          mBuilder.setContentTitle(context.getResources().getString(R.string.app_name));
          mBuilder.setSmallIcon(R.drawable.ic_launcher);

          if ( null != note ) {
               SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

               ActiveRecord.context = context.getApplicationContext();
               ANoteDetails.note = note;
               Intent resultIntent = new Intent(context, ANoteDetails_.class);
               TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
               stackBuilder.addParentStack(AStart_.class);

               // Adds the Intent that starts the Activity to the top of the stack
               stackBuilder.addNextIntent(resultIntent);
               PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
               mBuilder.setContentIntent(resultPendingIntent);

               NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
               mBuilder.setAutoCancel(true);

               boolean displayMessage = preferences.getBoolean("displayMessageText", true);
               boolean vibrateOnNotification = preferences.getBoolean("vibrateOnNotification", true);

               // if display message in notification set in settings, display message
               if ( displayMessage ) {
                    mBuilder.setContentText(note.description);
               } else {
                    // display default message otherwise
                    mBuilder.setContentText(context.getResources().getString(R.string.jenote_reminder));
               }

               // if settings saved as vibrate, vibrate for 1 sec
               if ( vibrateOnNotification ) {
                    // Get instance of Vibrator from current Context
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate
                    v.vibrate(1000);
               }
               // notificationID allows you to update the notification later on.
               mNotificationManager.notify(note.getId().intValue(), mBuilder.build());
          }

          // Release the lock
          wl.release();
     }

     public void cancelAlarm(Context context, int alarmId) {
          Intent intent = new Intent(context, ReminderManager.class);
          intent.putExtra(ONE_TIME, Boolean.TRUE);
          intent.putExtra("requestCode", alarmId);
          PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, 0);
          AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
          alarmManager.cancel(sender);
     }

     public void setOnetimeTimer(Context context, long timeTriggerAt, int alarmId, int flag) {
          AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
          Intent intent = new Intent(context, ReminderManager.class);
          intent.putExtra(ONE_TIME, Boolean.TRUE);
          intent.putExtra("requestCode", alarmId);
          PendingIntent pi = PendingIntent.getBroadcast(context, alarmId, intent, flag);
          am.set(AlarmManager.RTC_WAKEUP, timeTriggerAt, pi);
     }

}