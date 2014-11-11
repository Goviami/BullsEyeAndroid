package com.goviami.bullseye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.goviami.bullseye.R;
import com.goviami.bullseye.util.AppConstants;
import com.goviami.bullseye.util.PreferencesManager;


public class TermsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
    }

    /**
     * Called when user clicks on "Accept and continue" Button
     *
     * @param view view
     */
    public void acceptTerms(View view) {
        // Update LAUNCHER_ACTIVITY code in SharedPreferences to next screen
        PreferencesManager.getInstance().putValue(AppConstants.LAUNCHER_ACTIVITY_KEY, AppConstants.REGISTER_ACTIVITY_KEY);
        // Start Register Activity.
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
