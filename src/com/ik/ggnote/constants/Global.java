package com.ik.ggnote.constants;

import java.io.File;

import android.os.Environment;

public class Global {
     public static final String TAG                  = "Jenote";
     public static final String UNKNOWN_STRING       = "";
     public static final double EMPTY_INT_VALUE      = -1;
     public static final float  EMPTY_FLOAT_VALUE    = -1;
     public static final String APP_PHOTO_DIRECTORY  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "note_photo" + File.separator;
     public static final int    CAPTURE_CAMERA_PHOTO = 100;

     public interface NOTES {
          public static final String SIMPLE_STR   = "Simple";
          public static final String EVENT_STR    = "Event";
          public static final String URGENT_STR   = "Urgent";
          public static final String REMINDER_STR = "Reminder";
          public static final String WORK_STR     = "Work";

          public static final int    SIMPLE_INT   = 0;
          public static final int    EVENT_INT    = 1;
          public static final int    URGENT_INT   = 2;
          public static final int    REMINDER_INT = 3;
          public static final int    WORK_INT     = 4;

     }
}
