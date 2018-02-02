package com.immersionslabs.lcatalog.augment;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.FrameLayout;

import com.immersionslabs.lcatalog.R;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.assets.AssetHelper;
import org.artoolkit.ar.base.rendering.ARRenderer;

import static com.immersionslabs.lcatalog.augment.ARNativeApplication.getInstance;

public class ARNativeActivity extends ARActivity {

//    String Article_AR_ZipFileLocation, Article_AR_ExtractLocation;
//    File article_ar_zip_file;
//    private boolean zip_ar_downloaded = true;

    private ARNativeRenderer arNativeRenderer = new ARNativeRenderer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arnative);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//
//        Article_AR_ZipFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/ar_files.zip";
//        Article_AR_ExtractLocation = Environment.getExternalStorageState() + "/L_CATALOG/cache/Data/models/";
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
//                String FILE_URL_AR = "http://35.154.150.204:4000/upload/objfiles/ar_files.zip";
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
//            new UnzipUtil(Article_AR_ZipFileLocation, Article_AR_ExtractLocation);
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
//            folder = new File(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data");
//        }
//        assert folder != null;
//        if (!folder.exists()) {
//            boolean wasSuccessful = folder.mkdirs();
//            Log.e(TAG, "AR_Data Directory is Created --- '" + wasSuccessful + "' Thank You !!");
//        }
//    }

    public void onStop() {
        ARNativeRenderer.demoShutdown();
        super.onStop();
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

        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the AndroidManifest.xml file.
        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(getInstance(), "Data");
    }
}
