package com.goviami.bullseye.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goviami.bullseye.R;
import com.goviami.bullseye.adapter.CountrySpinnerAdapter;
import com.goviami.bullseye.model.Country;
import com.goviami.bullseye.util.AppConstants;
import com.goviami.bullseye.util.CountryListUtil;
import com.goviami.bullseye.util.PreferencesManager;


public class RegisterActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private String number;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        populateSpinner();
        inflater = this.getLayoutInflater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_next:
                verifyPhoneNumber();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Called on Selection of an Item from the Country list dropdown.
     *
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Country country = (Country) parent.getItemAtPosition(pos);
        EditText phoneCodeText = (EditText) findViewById(R.id.phoneCode);
        phoneCodeText.setText("+" + country.getPhoneCode());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Called when user clicks on "Next" Button
     */
    private void verifyPhoneNumber() {
        EditText phoneCodeText = (EditText) findViewById(R.id.phoneCode);
        EditText phoneNumberText = (EditText) findViewById(R.id.phoneNumber);
        String enteredNumber = phoneNumberText.getText().toString();
        if(validatePhoneNumber(enteredNumber)) {
            number = phoneCodeText.getText() + " " + enteredNumber;
            //TODO: send sms for verification

            // Hide Keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(phoneNumberText.getWindowToken(), 0);
            openDialog();
        }
    }

    private boolean validatePhoneNumber(String phoneText){
        if(phoneText== null || phoneText.trim().isEmpty()){
            Toast toast = Toast.makeText(this, R.string.toast_inavlid_number, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * Populate the Country Spinner Dropdown.
     */
    private void populateSpinner() {
        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(this, R.layout.country_spinner_row, CountryListUtil.getAllCountries(this));
        Spinner countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(this);
        countrySpinner.setSelection(countrySpinnerAdapter.getPosition(CountryListUtil.currentCountry));
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getDialogView())
                .setCustomTitle(getTitleView())
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Update LAUNCHER_ACTIVITY code in SharedPreferences to next screen
                        PreferencesManager.getInstance().putValue(AppConstants.LAUNCHER_ACTIVITY_KEY, AppConstants.USER_PROFILE_ACTIVITY_KEY);
                        // Start UserProfile Activity.
                        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.change_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private View getTitleView() {
        View view = inflater.inflate(R.layout.title_orange_view, null);
        //Set Title
        TextView dTitle = (TextView) view.findViewById(R.id.dialogTitle);
        dTitle.setText(R.string.confirm_number_title);
        return view;
    }

    private View getDialogView() {
        View view = inflater.inflate(R.layout.confirm_number_dialog, null);
        // Set Line 1
        TextView dLine1 = (TextView) view.findViewById(R.id.dialogLine1);
        dLine1.setText("" + number);
        // Set Line2
        TextView dLine2 = (TextView) view.findViewById(R.id.dialogLine2);
        dLine2.setText(R.string.confirm_number_line2);
        return view;
    }
}
