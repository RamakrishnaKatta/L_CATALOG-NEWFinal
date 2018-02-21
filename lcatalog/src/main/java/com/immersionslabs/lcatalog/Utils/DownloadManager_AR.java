package com.immersionslabs.lcatalog.Utils;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager_AR {

    private String DOWNLOAD_URL;

    public DownloadManager_AR(String url) {

        DOWNLOAD_URL = url;
        try {
            Download();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Download() throws IOException {
        URL u = new URL(DOWNLOAD_URL);
        URLConnection conn = u.openConnection();
        int contentLength = conn.getContentLength();

        DataInputStream stream = new DataInputStream(u.openStream());

        byte[] buffer = new byte[contentLength];
        stream.readFully(buffer);
        stream.close();

        DataOutputStream file_out = new DataOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/ar_files.zip"));
        file_out.write(buffer);
        file_out.flush();
        file_out.close();
    }
}

//public class DownloadImages_Vendor extends AsyncTask<String, Void, Bitmap> {
//
//    private static final String TAG = "DownloadImages_Vendor";
//    @SuppressLint("StaticFieldLeak")
//    private ImageView bmImage;
//
//    public DownloadImages_Vendor(ImageView bmImage) {
//        this.bmImage = bmImage;
//    }
//
//    protected Bitmap doInBackground(String... urls) {
//        String urldisplay = EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + urls[0];
//        Log.e(TAG, "Vendor_image1URL : " + urldisplay);
//        Bitmap mIcon = null;
//        try {
//
//            InputStream in = new URL(urldisplay).openStream();
//
//            mIcon = BitmapFactory.decodeStream(in);
//
//        } catch (Exception e) {
//            Log.e("Error", "" + e.getMessage());
//
//            e.printStackTrace();
//        }
//        return mIcon;
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        if (result != null) {
//            bmImage.setImageBitmap(result);
//        }
//    }
//}
