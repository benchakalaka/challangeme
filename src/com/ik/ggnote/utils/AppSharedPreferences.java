package com.ik.ggnote.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref ( value = SharedPref.Scope.UNIQUE ) public interface AppSharedPreferences {
     // user password
     @DefaultString ( "password" ) String password();

     // user login
     @DefaultString ( "login" ) String login();

     // remember me settings
     @DefaultBoolean ( false ) boolean rememberMe();
}
