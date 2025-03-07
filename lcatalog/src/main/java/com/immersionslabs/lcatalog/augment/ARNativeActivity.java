package com.immersionslabs.lcatalog.augment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.FrameLayout;

import com.immersionslabs.lcatalog.R;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.assets.AssetHelper;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.immersionslabs.lcatalog.augment.ARNativeApplication.getInstance;

public class ARNativeActivity extends ARActivity {

//    String Article_AR_ZipFileLocation, Article_AR_ExtractLocation;
//    File article_ar_zip_file;
//    private boolean zip_ar_downloaded = true;

    public static final String TAG = "ARNativeActivity";

    private ARNativeRenderer arNativeRenderer = new ARNativeRenderer();

    @Override
    public void onStart() {
        super.onStart();

        disableNetwork();
    }

    public void checkNetworkStatus() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo wifiInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean wifiConnected = wifiInfo.getState() == NetworkInfo.State.CONNECTED;

        NetworkInfo mobileInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean mobileConnected = mobileInfo.getState() == NetworkInfo.State.CONNECTED;

        Log.e(TAG, "\n Wifi Network Connected: " + wifiConnected);
        Log.e(TAG, "\n Mobile Network Connected: " + mobileConnected);
    }

    public void disableNetwork() {

        ToggleWifi(false);
        ToggleMobileData(false);

        checkNetworkStatus();
    }

    public void enableNetwork() {

        ToggleWifi(true);
        ToggleMobileData(true);

        checkNetworkStatus();
    }

    // True to Connect - False to disconnect
    private void ToggleWifi(boolean enabled) {

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        wifiManager.setWifiEnabled(enabled);
    }

    // True to Connect - False to disconnect
    private void ToggleMobileData(boolean enabled) {

        try {
            ConnectivityManager connMgr = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connMgr != null;
            final Class conmanClass = Class.forName(connMgr.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(connMgr);
            final Class iConnectivityManagerClass = Class.forName(
                    iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);

            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

        } catch (ClassNotFoundException | InvocationTargetException |
                NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arnative);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        Article_AR_ZipFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/ar_files.zip";
//        Log.e(TAG, "onCreate: ZipLocation" + Article_AR_ZipFileLocation);
//        Article_AR_ExtractLocation = Environment.getExternalStorageState() + "/L_CATALOG/cache/Data/models/";
//        Log.e(TAG, "ExtractLocation--" + Article_AR_ExtractLocation);//
//
//        article_ar_zip_file = new File(Article_AR_ZipFileLocation);
//
//        if (article_ar_zip_file.exists()) {
//            Log.e("AR_FILE_HANDLER", "File Already Exists, no need to download again");
//            zip_ar_downloaded = false;
//        } else {
//            try {
//                addCacheFolder();
//                Log.e("AR_FILE_HANDLER", "File doesn't Exist, downloading now");
//                String FILE_URL_AR = EnvConstants.APP_BASE_URL + "/upload/objfiles/ar_files.zip";
//                new DownloadManager_AR(FILE_URL_AR);
//                zip_ar_downloaded = true;
//
//            } catch (IOException e) {
//                Log.e("AR_FILE_HANDLER", "Problem Creating AR Folder Structure");
//                e.printStackTrace();
//            }
//        }
//
//        if (zip_ar_downloaded) {
//             new UnzipUtil(Article_AR_ZipFileLocation, Article_AR_ExtractLocation);
//        }
//
//        Toast.makeText(this, "AR Zip File Downloaded and Unzipped", Toast.LENGTH_SHORT).show();

        initializeInstance();
    }

//    private void addCacheFolder() throws IOException {
//        String state = Environment.getExternalStorageState();
//
//        File folder = null;
//        if (state.contains(Environment.MEDIA_MOUNTED)) {
//            folder = new File(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/");
//        }
//        assert folder != null;
//        if (!folder.exists()) {
//            boolean wasSuccessful = folder.mkdirs();
//            Log.e(TAG, "AR  Directory is Created --- '" + wasSuccessful + "' Thank You !!");
//        }
//    }

    public void onStop() {
        ARNativeRenderer.demoShutdown();
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();

        enableNetwork();
    }

    @Override
    protected ARRenderer supplyRenderer() {
        return arNativeRenderer;
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.arFrameLayout);
    }

    // Here we do one-off initialisation which should apply to all activities
    // in the application.
    protected void initializeInstance() {

        locations(Environment.getExternalStorageDirectory() + "/L_CATALOG/Screenshots/",
                Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/camera_para.dat");

        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the AndroidManifest.xml file.
        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(getInstance(),
                "Data",
                Environment.getExternalStorageDirectory().toString() + "/L_CATALOG/cache/");
    }
}