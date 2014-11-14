package com.goviami.bullseye.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.goviami.bullseye.service.DartBackgroundService;
import com.goviami.bullseye.util.AppConstants;
import com.goviami.bullseye.util.PreferencesManager;

/**
 * Created by subbu on 11/9/2014.
 */
public class ManageLauncherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize shared preferences.
        PreferencesManager.initializeInstance(this);
        // Get default activity screen.
        PreferencesManager prefManager = PreferencesManager.getInstance();
        prefManager.putValue(AppConstants.SCREEN_ACTIVE_KEY, "Y");
        int currActivity = prefManager.getIntValue(AppConstants.LAUNCHER_ACTIVITY_KEY);
        Intent intent;
        if (currActivity == AppConstants.HOME_ACTIVITY_KEY) {
            intent = new Intent();
        } else if (currActivity == AppConstants.TERMS_ACTIVITY_KEY) {
            intent = new Intent(this, TermsActivity.class);
        } else if (currActivity == AppConstants.REGISTER_ACTIVITY_KEY) {
            intent = new Intent(this, RegisterActivity.class);
        } else if (currActivity == AppConstants.USER_PROFILE_ACTIVITY_KEY) {
            intent = new Intent(this, UserProfileActivity.class);
        } else {
            intent = new Intent();
        }
        startActivity(intent);
    }



    @Override
    public void onStop(){
        super.onStop();
        PreferencesManager prefManager = PreferencesManager.getInstance();
        prefManager.putValue(AppConstants.SCREEN_ACTIVE_KEY, "N");
    }
}
