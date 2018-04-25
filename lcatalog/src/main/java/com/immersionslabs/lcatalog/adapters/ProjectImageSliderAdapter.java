package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.util.ArrayList;

public class ProjectImageSliderAdapter extends PagerAdapter {

    private ArrayList<String> Images;
    private LayoutInflater inflater;
    private Activity activity;
    private AppCompatImageView images;
    private String project_id;
    private static final String TAG = "ProjectImageSliderAdapter";

    public ProjectImageSliderAdapter(ProjectDetailActivity activity,
                                     ArrayList<String> slider_images,
                                     String project_id) {
        this.activity = activity;
        this.Images = slider_images;
        this.project_id = project_id;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_project_details, container, false);
        Log.e(TAG, "projectimage  " + project_id);

        images = v.findViewById(R.id.project_image_view);
        String urls = Images.get(position);
        Log.e(TAG, "instantiateItem:urls" + urls);

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/projectimages/" + project_id + urls)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(images);

        container.addView(v);
        return v;
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
