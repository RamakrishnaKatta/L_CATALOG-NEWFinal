package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.ProjectpartDetailsActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectpartDetailsAdapter extends RecyclerView.Adapter<ProjectpartDetailsAdapter.ViewHolder> {
    private static final String TAG = "ProjectpartDetailsAdapter";
    private Activity activity;

    private ArrayList<String> part_articles_id;
    private ArrayList<String> part_article_name;
    private ArrayList<String> part_article_images;


    public ProjectpartDetailsAdapter(ProjectpartDetailsActivity activity,
                                     ArrayList<String> part_articles_id,
                                     ArrayList<String> part_article_name,
                                     ArrayList<String> part_article_images) {
        this.part_articles_id = part_articles_id;
        this.part_article_name = part_article_name;
        this.part_article_images = part_article_images;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project_part_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectpartDetailsAdapter.ViewHolder holder, int position) {
        final Context[] context = new Context[1];
        String im1 = null;
        String get_image = part_article_images.get(position);
        Log.e(TAG, "project_images" + get_image);

        holder.article_name.setText(part_article_name.get(position));
        try {
            JSONObject images_json = new JSONObject(get_image);
            im1 = images_json.getString("img");
            Log.e(TAG, "onBindViewHolder: image" + im1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.article_image);
    }

    @Override
    public int getItemCount() {
        return part_article_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView article_name;
        private ImageView article_image;
        private RelativeLayout part_articel_container;

        public ViewHolder(View itemView) {

            super(itemView);
            article_name = itemView.findViewById(R.id.part_article_name);
            article_image = itemView.findViewById(R.id.part_article_image);


        }
    }
}
