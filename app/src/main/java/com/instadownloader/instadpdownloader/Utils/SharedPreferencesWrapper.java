package com.instadownloader.instadpdownloader.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ayush on 27/8/17.
 */

public class SharedPreferencesWrapper {
    private static Context context;
    private SharedPreferences.Editor editor;
    public SharedPreferencesWrapper(Context context) {
        this.context = context;
    }

    public static void putData(String table, String attribute, Object data) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sharedPreferencesTable.edit();
        if(data instanceof String)
            ed1.putString(attribute, data.toString());
        else if(data instanceof Boolean)
            ed1.putBoolean(attribute, (Boolean)data);
        ed1.commit();
    }
    public static void putData(Context context,String table, String attribute, Object data) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sharedPreferencesTable.edit();
        if(data instanceof String)
            ed1.putString(attribute, data.toString());
        else if(data instanceof Boolean)
            ed1.putBoolean(attribute, (Boolean)data);
        ed1.commit();
    }

    public static void putSingleData(String table, String attribute,Object data) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sharedPreferencesTable.edit();
        if(data instanceof String)
            ed1.putString(attribute, data.toString());
        else if(data instanceof Boolean)
            ed1.putBoolean(attribute, (Boolean)data);
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
    public static String getString(String table, String attribute, String defaultValue) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, 0);
        return (sharedPreferencesTable.getString(attribute,defaultValue));
    }

    public static boolean getBoolean(String table, String attribute, boolean defaultValue) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, 0);
        return (sharedPreferencesTable.getBoolean(attribute,defaultValue));
    }
    public static boolean getBoolean(Context context,String table, String attribute, boolean defaultValue) {
        SharedPreferences sharedPreferencesTable = context.getSharedPreferences(table, 0);
        return (sharedPreferencesTable.getBoolean(attribute,defaultValue));
    }
}
