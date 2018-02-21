package com.immersionslabs.lcatalog.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.Fragment_Overview;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainListViewAdapter extends RecyclerView.Adapter<MainListViewAdapter.ViewHolder> {

    private static final String TAG = "MainListViewAdapter";

    private Fragment_Overview activity;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_images;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_descriptions;

    public MainListViewAdapter(Fragment_Overview activity, ArrayList<String> item_ids,
                               ArrayList<String> item_names,
                               ArrayList<String> item_images,
                               ArrayList<String> item_prices,
                               ArrayList<String> item_discounts,
                               ArrayList<String> item_descriptions) {
        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_images = item_images;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_descriptions = item_descriptions;

        Log.e(TAG, "ids----" + item_ids);
        Log.e(TAG, "Images----" + item_images);
        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);
        Log.e(TAG, "descriptions----" + item_descriptions);

        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater(null);
        View view = inflater.inflate(R.layout.content_display, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainListViewAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.item_image.setImageResource(R.drawable.dummy_icon);

        String im1 = null;
        String get_image = item_images.get(position);
        try {

            JSONArray images_json = new JSONArray(get_image);
//            for (int i = 0; i < images_json.length(); i++) {
            if (images_json.length() > 0) {
                im1 = images_json.getString(0);
                Log.e(TAG, "image1 >>>>" + im1);
            }
//        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.item_image);
        viewHolder.item_name.setText(item_names.get(position));
        viewHolder.item_description.setText(item_descriptions.get(position));

    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        LinearLayout main_container;
        private TextView item_name, item_description, item_price, item_discount, item_price_new;

        ViewHolder(View view) {
            super(view);

            main_container = view.findViewById(R.id.main_container);
            item_image = view.findViewById(R.id.main_image);
            item_name = view.findViewById(R.id.main_title);
            item_description = view.findViewById(R.id.main_data);

            Typeface custom_font = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/Graduate-Regular.ttf");
            Typeface custom_font2 = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/Cookie-Regular.ttf");
            item_name.setTypeface(custom_font);
            item_description.setTypeface(custom_font2);

        }
    }
}
