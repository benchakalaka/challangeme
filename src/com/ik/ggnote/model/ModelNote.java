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
