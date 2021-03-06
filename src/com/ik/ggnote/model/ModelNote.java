package com.ik.ggnote.model;

import android.content.Context;

import com.orm.SugarRecord;

public class ModelNote extends SugarRecord <ModelNote> {

     // --------------------- FIELDS
     public ModelLocation location;
     // note description
     public String        description;
     // selected date
     public String        date;
     // path to taken photo
     public String        pathToPhoto;
     // path to drawing
     public String        pathToDrawing;
     // type of note
     public String        noteType;
     // is item mark as completed
     public boolean       isCompleted;
     // alarm time hour
     public int           alarmHour;
     // alarm time minute
     public int           alarmMinute;
     // string representation of time
     public String        alarmString;

     /**
      * Default constructor
      * 
      * @param context
      */
     public ModelNote ( Context context ) {
          super(context);
     }

     @Override public String toString() {
          return "Descr: " + description + " , date " + date + " photo " + pathToPhoto + " , drawing " + pathToDrawing + " , location = " + ((location == null) ? "null" : location.latitude);
     }

}
