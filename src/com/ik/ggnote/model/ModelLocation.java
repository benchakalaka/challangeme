package com.ik.ggnote.model;

import android.content.Context;

import com.orm.SugarRecord;

public class ModelLocation extends SugarRecord <ModelLocation> {

     // ------------------------ FIELDS
     public double latitude;
     public double longitude;
     public String address;

     /**
      * Default constructor
      * 
      * @param context
      */
     public ModelLocation ( Context context ) {
          super(context);
     }
}