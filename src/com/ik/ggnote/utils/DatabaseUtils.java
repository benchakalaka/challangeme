package com.ik.ggnote.utils;

import java.util.List;

import com.ik.ggnote.model.ModelNote;
import com.orm.query.Condition;
import com.orm.query.Select;

public class DatabaseUtils {
     public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS        = "yyyy/MM/dd HH:mm:ss";
     public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS_MILLIS = "yyyy/MM/dd HH:mm:ss:SSS";
     public static final String DATE_PATTERN_HH_MM_SS                   = "HH:mm:ss";
     public static final String DATE_PATTERN_HH_MM_SS_MILLIS            = "HH:mm:ss:SSS";
     public static final String DATE_PATTERN_YYYY_MM_DD                 = "yyyy/MM/dd";
     public static final String DATE_PATTERN_YYYY_MM                    = "yyyy/MM";
     public static final String DATE_PATTERN_YYYY                       = "yyyy";

     /**
      * Select all records, where date starts from yearMonth_YYYY_MM
      * example yearMonth_YYYY_MM_DD = 2014/05/05, date = 2014/05/23 12:23:13,
      */
     public static List <ModelNote> getAllNotesByDate(String yearMonth_YYYY_MM_DD) {
          return Select.from(ModelNote.class).where(Condition.prop("date").like(yearMonth_YYYY_MM_DD + "%")).list();
     }

}