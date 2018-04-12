package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.immersionslabs.lcatalog.ProductPageActivity;
import com.immersionslabs.lcatalog.ProjectPartDetailsActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.util.ArrayList;

public class ProjectpartDetailsAdapter extends RecyclerView.Adapter<ProjectpartDetailsAdapter.ViewHolder> {
    private static final String TAG = "ProjectpartDetailsAdapter";
    private Activity activity;

    private ArrayList<String> part_articles_id;
    private ArrayList<String> part_article_name;
    private ArrayList<String> part_article_images;

    public ProjectpartDetailsAdapter(ProjectPartDetailsActivity activity,
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
        View view = inflater.inflate(R.layout.item_project_part_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectpartDetailsAdapter.ViewHolder holder, final int position) {
        final Context[] context = new Context[1];
        String im1 = null;
        String get_image = part_article_images.get(position);
        Log.e(TAG, "project_images" + get_image);

        holder.article_name.setText(part_article_name.get(position));
        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + get_image)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.article_image);

        holder.part_article_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPageActivity.class);
                Bundle b = new Bundle();

                b.putString("article_id", part_articles_id.get(position));
                intent.putExtras(b);
                context[0].startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return part_article_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView article_name;
        private ImageView article_image;
        private RelativeLayout part_article_container;

        public ViewHolder(View itemView) {

            super(itemView);
            article_name = itemView.findViewById(R.id.part_article_name);
            article_image = itemView.findViewById(R.id.part_article_image);
            part_article_container = itemView.findViewById(R.id.part_article_container);

        }
    }
}
