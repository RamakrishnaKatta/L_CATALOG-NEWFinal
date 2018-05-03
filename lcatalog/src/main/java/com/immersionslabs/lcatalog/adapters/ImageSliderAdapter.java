package com.immersionslabs.lcatalog.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {

    private static final String TAG = "ImageSliderAdapter";

    private ArrayList<String> Images;
    private LayoutInflater inflater;
    private Context context;

    public ImageSliderAdapter(Context context, ArrayList<String> Images) {
        this.context = context;
        this.Images = Images;
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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.fragment_product_images, container, false);
        AppCompatImageView images = v.findViewById(R.id.article_image_view);
        String urls = Images.get(position);
        Log.e(TAG, "instantiateItem:urls" + urls);

        Glide.with(context)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + urls)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(images);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
