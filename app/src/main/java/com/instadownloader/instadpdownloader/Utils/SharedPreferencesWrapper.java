package com.instadownloader.instadpdownloader.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ayush on 27/8/17.
 */

public class SharedPreferencesWrapper {
    private Context context;
    private SharedPreferences.Editor editor;
    public SharedPreferencesWrapper(Context context) {
        this.context = context;
    }

    public void putData(String table, String attribute, String data) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sharedPreferencesTable.edit();
        ed1.putString(attribute, data);
        ed1.commit();
    }

    public void putSingleData(String table, String attribute, String data) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sharedPreferencesTable.edit();
        ed1.putString(attribute, data);
        ed1.commit();
    }

    public void putBundleData(String table, String attribute, String data) {
        if(editor==null)
        {
            SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, Context.MODE_PRIVATE);
            editor = sharedPreferencesTable.edit();
        }
        editor.putString(attribute, data);
    }

    public void commit() {
        if (editor != null) {
            editor.commit();
        }

    }
    public String getString(String table, String attribute, String defaultValue) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, 0);
        return (sharedPreferencesTable.getString(attribute,defaultValue));
    }
}
