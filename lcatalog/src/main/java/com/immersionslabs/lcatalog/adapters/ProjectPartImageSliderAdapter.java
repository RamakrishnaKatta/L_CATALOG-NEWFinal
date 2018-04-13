package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.ProjectPartDetailsActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ProjectPartImageSliderAdapter extends PagerAdapter {

    private ArrayList<String> Images;
    private Activity activity;
    Bitmap mIcon = null;

    String project_id;
    String TAG = "ProjectPartImageSliderAdapter";

    public ProjectPartImageSliderAdapter(ProjectPartDetailsActivity activity,
                                         ArrayList<String> slider_images,
                                         String project_id) {
        this.activity = activity;
        this.Images = slider_images;
        this.project_id = project_id;
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_project_part_details, container, false);
        Log.e(TAG, "projectidpartimage  " + project_id);
        ImageView images = v.findViewById(R.id.project_part_image_view);
        Bitmap b = download_images(Images.get(position));
        images.setImageBitmap(b);
        container.addView(v);
        return v;
    }

    private Bitmap download_images(String urls) {
        String urldisplay = EnvConstants.APP_BASE_URL + "/upload/projectpartimages/partimages_" + project_id + "_" + urls;
        Log.e(TAG, "download_images:partimage  " + urldisplay);

        try {
            RequestQueue requestQueue= Volley.newRequestQueue(activity.getApplicationContext());
            ImageRequest imageRequest=new ImageRequest(urldisplay, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    mIcon=response;
                    Log.e(TAG, "Bitmap_response "+response );
                }
            },0,0,ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(imageRequest);
        } catch (Exception e) {

            Log.e(TAG, "Error" + e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
