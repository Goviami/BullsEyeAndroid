package com.goviami.bullseye.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;
import com.goviami.bullseye.R;
import com.goviami.bullseye.util.AppConstants;
import com.goviami.bullseye.util.ImagePickUpUtil;
import com.goviami.bullseye.util.PreferencesManager;

import java.io.File;


public class UserProfileActivity extends Activity {

    private ImageView imageView;
    private EditText profileNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        imageView = (ImageView) findViewById(R.id.profilePicIcon);
        profileNameText = (EditText) findViewById(R.id.profileName);
        // Load Image if already exists.
        String profilePicPath = PreferencesManager.getInstance().getStringValue(AppConstants.PROFILE_PIC_PATH_KEY);
        if(profilePicPath!=null){
            loadImage(profilePicPath);
        }
        // Load Profile Name if already exists.
        String profileName = PreferencesManager.getInstance().getStringValue(AppConstants.PROFILE_NAME_KEY);
        if(profileName!=null){
            profileNameText.setText(profileName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_next:
                loadMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File croppedImageFile = new File(getFilesDir(), AppConstants.PROFILE_PIC_NAME);

        if ((requestCode == AppConstants.REQUEST_PICTURE) && (resultCode == RESULT_OK)) {
            // When the user is done picking a picture, let's start the CropImage Activity,
            // setting the output image file and size to 256*256 pixels square.
            Uri croppedImage = Uri.fromFile(croppedImageFile);

            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(AppConstants.PROFILE_PIC_DIMEN, AppConstants.PROFILE_PIC_DIMEN, croppedImage);
            cropImage.setOutputQuality(AppConstants.PROFILE_PIC_QUALITY);
            cropImage.setSourceImage(data.getData());

            startActivityForResult(cropImage.getIntent(this), AppConstants.REQUEST_CROP_PICTURE);
        } else if ((requestCode == AppConstants.REQUEST_CROP_PICTURE) && (resultCode == RESULT_OK)) {
            // When we are done cropping, display it in the ImageView.
            loadImage(croppedImageFile.getAbsolutePath());
            PreferencesManager.getInstance().putValue(AppConstants.PROFILE_PIC_PATH_KEY, croppedImageFile.getAbsolutePath());
        }
    }

    /**
     * Called When user clicks on profile pic.
     *
     * @param view
     */
    public void addProfilePic(View view){
        ImagePickUpUtil imagePickUpUtil = new ImagePickUpUtil(this);
        imagePickUpUtil.openImageSelector();
    }

    public void loadMainActivity(){
        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(profileNameText.getWindowToken(), 0);
        //Get entered number
        String profileName = profileNameText.getText().toString();
        if(validateProfile(profileName)){
            PreferencesManager.getInstance().putValue(AppConstants.PROFILE_NAME_KEY, profileName);
        }
    }

    private void loadImage(String path){
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    private boolean validateProfile(String profileName){
        if(profileName!=null && profileName.trim().isEmpty()){
            Toast toast = Toast.makeText(this, R.string.toast_inavlid_profile_name, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }
}
