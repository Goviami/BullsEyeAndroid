package com.goviami.bullseye.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by subbu on 11/9/2014.
 */
public class PreferencesManager {
    /**
     * Instance.
     */
    private static PreferencesManager sInstance;
    /**
     * Application shared preference
     */
    private SharedPreferences sPref;

    /**
     * Constructor.
     *
     * @param context
     */
    public PreferencesManager(Context context) {
        this.sPref = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void putValue(String key, int value) {
        sPref.edit().putInt(key, value).commit();
    }

    public void putValue(String key, String value) {
        sPref.edit().putString(key, value).commit();
    }

    public int getIntValue(String key) {
        return sPref.getInt(key, 0);
    }

    public String getStringValue(String key) {
        return sPref.getString(key, null);
    }

    public void remove(String key) {
        sPref.edit().remove(key).commit();
    }

    public boolean clear() {
        return sPref.edit().clear().commit();
    }
}
