package com.ik.ggnote.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref ( value = SharedPref.Scope.UNIQUE ) public interface AppSharedPreferences {
     // user password
     @DefaultString ( "" ) String password();

     // user email
     @DefaultString ( "" ) String email();

}
