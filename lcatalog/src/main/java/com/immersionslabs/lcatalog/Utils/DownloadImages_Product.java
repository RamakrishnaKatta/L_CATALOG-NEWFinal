package com.immersionslabs.lcatalog.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownloadImages_Product extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadImages_Product";
    @SuppressLint("StaticFieldLeak")
    private ImageView bmImage;

    public DownloadImages_Product(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = "http://35.154.150.204:4000/upload/images/" + urls[0];
        Log.e(TAG, "Product_image1URL : " + urldisplay);
        Bitmap mIcon = null;
        try {

            InputStream in = new URL(urldisplay).openStream();

            mIcon = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error", "" + e.getMessage());

            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            bmImage.setImageBitmap(result);
        }
    }
}