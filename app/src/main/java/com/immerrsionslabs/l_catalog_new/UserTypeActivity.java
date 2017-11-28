package com.immerrsionslabs.l_catalog_new;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.immerrsionslabs.l_catalog_new.Utils.CustomMessage;
import com.immerrsionslabs.l_catalog_new.Utils.NetworkConnectivity;
import com.immerrsionslabs.l_catalog_new.Utils.PrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTypeActivity extends AppCompatActivity {

    private static final String TAG = "UserTypeActivity";
    private static final int MY_PERMISSIONS_REQUEST = 10;
    private static final int REQUEST_USERTYPE = 0;

    TextView app_name,
            welcome_aboard,
            who_are_you,
            powered;
    ImageButton _customer,
            _newCustomer,
            _shopper;
    ImageView delete_cache;

    private PrefManager prefManager1;
    private boolean success = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        RequestPermissions();

        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name = findViewById(R.id.application_name);
        app_name.setTypeface(custom_font1);

        welcome_aboard = findViewById(R.id.welcome_aboard);
        welcome_aboard.setTypeface(custom_font2);

        who_are_you = findViewById(R.id.who_are_you);
        who_are_you.setTypeface(custom_font2);

        powered = findViewById(R.id.immersionslabs);
        powered.setTypeface(custom_font2);

        delete_cache = findViewById(R.id.icon);
        delete_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean delete_models = false;
                boolean delete_patterns = false;
                boolean delete_data = false;

                File dir_models = new File(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/models");
                File dir_patterns = new File(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/patterns");
                File dir_data = new File(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data");

                if (dir_models.isDirectory()) {
                    String[] children_models = dir_models.list();

                    Log.e(TAG, "" + Arrays.toString(children_models));

                    for (int i = 0; i < children_models.length; i++) {
                        delete_models = new File(dir_models, children_models[i]).delete();
                    }
                    Log.e(TAG, "Files inside Models Folder deleted : " + delete_models);
                }

                if (dir_patterns.isDirectory()) {
                    String[] children_patterns = dir_patterns.list();

                    Log.e(TAG, "" + Arrays.toString(children_patterns));

                    for (int i = 0; i < children_patterns.length; i++) {
                        delete_patterns = new File(dir_patterns, children_patterns[i]).delete();
                    }
                    Log.e(TAG, "Files inside Patterns Folder deleted : " + delete_patterns);
                }

                if (dir_data.isDirectory()) {
                    String[] children_data = dir_data.list();

                    Log.e(TAG, "" + Arrays.toString(children_data));

                    for (int i = 0; i < children_data.length; i++) {
                        delete_data = new File(dir_data, children_data[i]).delete();
                    }
                    Log.e(TAG, "Files inside Data Folder deleted : " + delete_data);
                }

                if (delete_models || delete_patterns || delete_data) {
                    Toast.makeText(getBaseContext(), "Debugging: Cache Files Removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Debugging: Cache doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        _customer = findViewById(R.id.btn_customer);
        _customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
                startActivity(intent);

                if (!isExternalStorageReadOnly() && isExternalStorageAvailable()) {
                    Log.e(TAG, "Enter this loop of Folder Creation");
                    CreateFolderStructure();
                }
                finish();
            }
        });

        _newCustomer = findViewById(R.id.btn_new_customer);
        _newCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, SignupActivity.class);
                startActivity(intent);

                if (!isExternalStorageReadOnly() && isExternalStorageAvailable()) {
                    Log.e(TAG, "Enter this loop of Folder Creation");
                    CreateFolderStructure();
                }
                finish();
            }
        });

        _shopper = findViewById(R.id.btn_shopper);
        _shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, GuestActivity.class);
                startActivity(intent);

                if (!isExternalStorageReadOnly() && isExternalStorageAvailable()) {
                    Log.e(TAG, "Enter this loop of Folder Creation");
                    CreateFolderStructure();
                }
                finish();
            }
        });

        prefManager1 = new PrefManager(this);
        Log.e(TAG, "" + prefManager1.UserTypeActivityScreenLaunch());
        if (prefManager1.UserTypeActivityScreenLaunch()) {
            ShowcaseView();
        }

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // checkInternetConnection();
        if (NetworkConnectivity.checkInternetConnection(UserTypeActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(UserTypeActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    private void CreateFolderStructure() {
        String root_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOG";
        String models_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOG/Models";
        String screenshots_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOG/Screenshots";
        String cache_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOG/cache";

        File Root_Folder, Models_Folder, Screenshots_Folder, Cache_Folder;

        if (Environment.getExternalStorageState().contains(Environment.MEDIA_MOUNTED)) {
            Root_Folder = new File(root_Path);
            Models_Folder = new File(models_Path);
            Screenshots_Folder = new File(screenshots_Path);
            Cache_Folder = new File(cache_Path);
        } else {
            Root_Folder = new File(root_Path);
            Models_Folder = new File(models_Path);
            Screenshots_Folder = new File(screenshots_Path);
            Cache_Folder = new File(cache_Path);
        }

        if (Root_Folder.exists()) {
            CustomMessage.getInstance().CustomMessage(UserTypeActivity.this, "Welcome Back !!");
        } else {

            if (!Root_Folder.exists()) {
                success = Root_Folder.mkdirs();
            }
            if (!Models_Folder.exists()) {
                success = Models_Folder.mkdirs();
            }
            if (!Screenshots_Folder.exists()) {
                success = Screenshots_Folder.mkdirs();
            }
            if (!Cache_Folder.exists()) {
                success = Cache_Folder.mkdirs();
            }
            if (success) {
                CustomMessage.getInstance().CustomMessage(UserTypeActivity.this, "Get Set Go !!");

            }
        }
    }

    /*Showcaseview for the Signup, Login, Guest*/
    private void ShowcaseView() {
        prefManager1.setUserTypeActivityScreenLaunch();
        Log.e(TAG, "Show case views are Implemented here" + prefManager1.UserTypeActivityScreenLaunch());

        final TapTargetSequence sequence = new TapTargetSequence(this).targets(
                TapTarget.forView(findViewById(R.id.btn_new_customer), "Click here if you want to signup with us...")
                        .tintTarget(false)
                        .targetRadius(25)
                        .transparentTarget(true)
                        .cancelable(false)
                        .textColor(R.color.white)
                        .id(1),
                TapTarget.forView(findViewById(R.id.btn_customer), "Click here if you visited us before")
                        .tintTarget(false)
                        .transparentTarget(true)
                        .cancelable(false)
                        .targetRadius(25)
                        .textColor(R.color.white)
                        .id(2),
                TapTarget.forView(findViewById(R.id.btn_shopper), "Click here if you are a Onetime User")
                        .tintTarget(false)
                        .transparentTarget(true)
                        .textColor(R.color.white)
                        .targetRadius(25)
                        .cancelable(false)
                        .id(3)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        });
        sequence.start();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage("Press OK to get out of this App");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                UserTypeActivity.super.onDestroy();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USERTYPE) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

     /*Permissions Required for the app and granted*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void RequestPermissions() {

        int PermissionCamera = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.CAMERA);
        int PermissionReadStorage = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int PermissionWriteStorage = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (PermissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (PermissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (PermissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.e(TAG, "Permission callback called-------");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with all three permissions
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check all three permissions
                    if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Camera and Storage(Read and Write) permission granted");
                        // Process the normal flow
                        // Else any one or both the permissions are not granted
                    } else {
                        Log.e(TAG, "Some permissions are not granted ask again ");

                        // Permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // ShouldShowRequestPermissionRationale will return true
                        // Show the dialog or SnackBar saying its necessary and try again otherwise proceed with setup.

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showDialogOK(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    RequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    android.os.Process.killProcess(android.os.Process.myPid());
                                                    System.exit(0);
                                                    break;
                                            }
                                        }
                                    });
                        }
                        // permission is denied (and never ask again is checked)
                        // shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            // Proceed with logic by disabling the related features or quit the app.
                        }
                    } // other 'case' lines to check for other permissions this app might request
                }
            }
        }
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Storage and Camera Services are Mandatory for this Application")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
