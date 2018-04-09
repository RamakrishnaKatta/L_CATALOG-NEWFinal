package com.immersionslabs.lcatalog.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ProjectImageAdapter extends PagerAdapter {


    String TAG = "ProjectImageAdapter";

    private ArrayList<String> Images;
    private LayoutInflater inflater;
    private Context context;
    String project_id;

//  ArrayList<String> project_id = EnvConstants.project_image_id;

    public ProjectImageAdapter(Context context, ArrayList<String> Images) {

        this.context = context;
        this.Images = Images;
//        this.project_id = EnvConstants.project_image_id;
        inflater = LayoutInflater.from(context);
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
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_project_page, container, false);
        ImageView images = v.findViewById(R.id.article_image_view);
        Bitmap b = download_images(Images.get(position));
        project_id = EnvConstants.project_image_id.get(position);
        images.setImageBitmap(b);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

    private Bitmap download_images(String urls) {
        String urldisplay = EnvConstants.APP_BASE_URL + "/upload/projectimages/" + project_id + urls;
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.d("Error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
