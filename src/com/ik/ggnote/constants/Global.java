package com.ik.ggnote.constants;

import java.io.File;

import android.os.Environment;

public class Global {
     public static final String TAG                  = "APP!!!!!!!!!";
     public static final String UNKNOWN_STRING       = "";
     public static final double EMPTY_INT_VALUE      = -1;
     public static final float  EMPTY_FLOAT_VALUE    = -1;
     public static final String APP_PHOTO_DIRECTORY  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "note_photo" + File.separator;
     public static final int    CAPTURE_CAMERA_PHOTO = 100;
}
