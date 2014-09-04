//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.ik.ggnote.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.androidannotations.api.sharedpreferences.BooleanPrefEditorField;
import org.androidannotations.api.sharedpreferences.BooleanPrefField;
import org.androidannotations.api.sharedpreferences.EditorHelper;
import org.androidannotations.api.sharedpreferences.IntPrefEditorField;
import org.androidannotations.api.sharedpreferences.IntPrefField;
import org.androidannotations.api.sharedpreferences.SharedPreferencesHelper;
import org.androidannotations.api.sharedpreferences.StringPrefEditorField;
import org.androidannotations.api.sharedpreferences.StringPrefField;

public final class AppSharedPreferences_
    extends SharedPreferencesHelper
{

    private Context context_;

    public AppSharedPreferences_(Context context) {
        super(PreferenceManager.getDefaultSharedPreferences(context));
        this.context_ = context;
    }

    public AppSharedPreferences_.AppSharedPreferencesEditor_ edit() {
        return new AppSharedPreferences_.AppSharedPreferencesEditor_(getSharedPreferences());
    }

    public BooleanPrefField askPassword() {
        return booleanField("askPassword", true);
    }

    public BooleanPrefField displayMessageText() {
        return booleanField("displayMessageText", true);
    }

    public StringPrefField email() {
        return stringField("email", "");
    }

    public IntPrefField orderNumberEvent() {
        return intField("orderNumberEvent", 1);
    }

    public IntPrefField orderNumberReminder() {
        return intField("orderNumberReminder", 3);
    }

    public IntPrefField orderNumberSimple() {
        return intField("orderNumberSimple", 0);
    }

    public IntPrefField orderNumberUrgent() {
        return intField("orderNumberUrgent", 2);
    }

    public IntPrefField orderNumberWork() {
        return intField("orderNumberWork", 4);
    }

    public StringPrefField password() {
        return stringField("password", "");
    }

    public BooleanPrefField vibrateOnNotification() {
        return booleanField("vibrateOnNotification", true);
    }

    public final static class AppSharedPreferencesEditor_
        extends EditorHelper<AppSharedPreferences_.AppSharedPreferencesEditor_>
    {


        AppSharedPreferencesEditor_(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        public BooleanPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> askPassword() {
            return booleanField("askPassword");
        }

        public BooleanPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> displayMessageText() {
            return booleanField("displayMessageText");
        }

        public StringPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> email() {
            return stringField("email");
        }

        public IntPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> orderNumberEvent() {
            return intField("orderNumberEvent");
        }

        public IntPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> orderNumberReminder() {
            return intField("orderNumberReminder");
        }

        public IntPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> orderNumberSimple() {
            return intField("orderNumberSimple");
        }

        public IntPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> orderNumberUrgent() {
            return intField("orderNumberUrgent");
        }

        public IntPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> orderNumberWork() {
            return intField("orderNumberWork");
        }

        public StringPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> password() {
            return stringField("password");
        }

        public BooleanPrefEditorField<AppSharedPreferences_.AppSharedPreferencesEditor_> vibrateOnNotification() {
            return booleanField("vibrateOnNotification");
        }

    }

}
