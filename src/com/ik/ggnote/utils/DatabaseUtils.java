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
     public static String       SELECT_ORDER_BY_NOTE_TYPE               = "SELECT * FROM MODEL_NOTE WHERE date LIKE '%s%%' ORDER BY  NOTE_TYPE='%s', NOTE_TYPE='%s', NOTE_TYPE='%s', NOTE_TYPE='%s', NOTE_TYPE='%s';";

     /**
      * Select all records, where date starts from yearMonth_YYYY_MM
      * example yearMonth_YYYY_MM_DD = 2014/05/05, date = 2014/05/23 12:23:13,
      */
     public static List <ModelNote> getAllNotesByDate(String yearMonth_YYYY_MM_DD, String[] arrNotesNamesOrdered) {
          return ModelNote.findWithQuery(ModelNote.class, String.format(SELECT_ORDER_BY_NOTE_TYPE, yearMonth_YYYY_MM_DD, arrNotesNamesOrdered[4], arrNotesNamesOrdered[3], arrNotesNamesOrdered[2], arrNotesNamesOrdered[1], arrNotesNamesOrdered[0]));
     }

     /**
      * Select all records, where date starts from yearMonth_YYYY_MM
      * example yearMonth_YYYY_MM_DD = 2014/05/05, date = 2014/05/23 12:23:13,
      */
     public static List <ModelNote> getAllNotesCompletedByDate(String yearMonth_YYYY_MM_DD, boolean completed) {
          return Select.from(ModelNote.class).where(Condition.prop("date").like(yearMonth_YYYY_MM_DD + "%"), Condition.prop("IS_COMPLETED").eq(completed)).list();
     }

     /**
      * Select all records from ModelNote table
      * example yearMonth_YYYY_MM_DD = 2014/05/05, date = 2014/05/23 12:23:13,
      */
     public static List <ModelNote> getAllNotes() {
          return ModelNote.findWithQuery(ModelNote.class, "SELECT * FROM MODEL_NOTE;");
     }

     /**
      * Select all records from ModelNote table
      * example yearMonth_YYYY_MM_DD = 2014/05/05, date = 2014/05/23 12:23:13,
      */
     public static List <ModelNote> getAllNotesOrderedByDatesAsc() {
          return ModelNote.findWithQuery(ModelNote.class, "SELECT * FROM MODEL_NOTE ORDERED BY date ASC;");
     }

     /**
      * Select all records from ModelNote table
      * example yearMonth_YYYY_MM_DD = 2014/05/05, date = 2014/05/23 12:23:13,
      */
     public static List <ModelNote> getAllNotesOrderedByDatesDesc() {
          return ModelNote.findWithQuery(ModelNote.class, "SELECT * FROM MODEL_NOTE ORDERED BY date DESC;");
     }
}