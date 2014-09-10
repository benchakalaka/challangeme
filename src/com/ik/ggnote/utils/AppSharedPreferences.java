package com.ik.ggnote.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref ( value = SharedPref.Scope.APPLICATION_DEFAULT ) public interface AppSharedPreferences {
     // user password
     @DefaultString ( "" ) String password();

     // user email
     @DefaultString ( "" ) String email();

     // ask password
     @DefaultBoolean ( false ) boolean askPassword();

     // default position for grouping simple
     @DefaultInt ( 0 ) int orderNumberSimple();

     // default postion of event
     @DefaultInt ( 1 ) int orderNumberEvent();

     // default postion of urgent
     @DefaultInt ( 2 ) int orderNumberUrgent();

     // default postion of reminder
     @DefaultInt ( 3 ) int orderNumberReminder();

     // default postion of work
     @DefaultInt ( 4 ) int orderNumberWork();

     // display text of jenote in notification bar
     @DefaultBoolean ( true ) boolean displayMessageText();

     // vibrate on notification alarm
     @DefaultBoolean ( true ) boolean vibrateOnNotification();
}
