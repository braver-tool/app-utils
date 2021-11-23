/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static final String PREFERENCE_MAIN = PreferenceUtils.class.getPackage().getName();


    private final SharedPreferences prefManager;
    private static PreferenceUtils prefInstance;

    private PreferenceUtils(Context context) {
        prefManager = context.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceUtils getInstance(Context context) {
        if (prefInstance == null) {
            prefInstance = new PreferenceUtils(context.getApplicationContext());
        }
        return prefInstance;
    }

    public void setStringValue(String keyName, String value) {
        prefManager.edit().putString(keyName, value).apply();
    }

    public String getStringValue(String keyName) {
        return prefManager.getString(keyName, "");
    }


    public void setBooleanValue(String keyName, boolean value) {
        prefManager.edit().putBoolean(keyName, value).apply();
    }

    public Boolean getBooleanValue(String keyName) {
        return prefManager.getBoolean(keyName, false);
    }

    public void setIntValue(String keyName, int value) {
        prefManager.edit().putInt(keyName, value).apply();
    }

    public int getIntValue(String keyName) {
        return prefManager.getInt(keyName, 0);
    }


    public void removePref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void removePref(String val) {
        SharedPreferences.Editor editor = prefManager.edit();
        editor.remove(val);
        editor.apply();
    }

}