package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.immersionslabs.lcatalog.CampaignActivity;
import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.util.ArrayList;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.ViewHolder> {

    private static final String TAG = "CampaignAdapter";
    private Activity activity;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_name;
    private ArrayList<String> project_description;
    private ArrayList<String> project_subDescription;
    private ArrayList<String> project_images;

    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;

    public CampaignAdapter(CampaignActivity activity,
                           ArrayList<String> project_ids,
                           ArrayList<String> project_name,
                           ArrayList<String> project_description,
                           ArrayList<String> project_subDescription,
                           ArrayList<String> project_images) {

        this.project_ids = project_ids;
        this.project_name = project_name;
        this.project_description = project_description;
        this.project_subDescription = project_subDescription;
        this.project_images = project_images;


        this.activity = activity;
        Log.e(TAG, " ids-------" + project_ids);
        Log.e(TAG, " Name-------" + project_name);
        Log.e(TAG, " Description-------" + project_description);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CampaignAdapter.ViewHolder holder, final int position) {

        final Context[] context = new Context[1];

//        Glide.with(activity)
//                .load(EnvConstants.APP_BASE_URL + "/upload/projectimages/" + project_ids + project_images.get(position))
//                .placeholder(R.drawable.dummy_icon)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.campaign_image);
        holder.campaign_name.setText(project_name.get(position));
        holder.campaign_description.setText(project_description.get(position));

        holder.campaign_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context[0] = v.getContext();
                Intent intent = new Intent(context[0], ProjectDetailActivity.class);

                Bundle b = new Bundle();
                b.putString("_id", project_ids.get(position));
                b.putString("projectName", project_name.get(position));
                b.putString("projectDescription", project_description.get(position));
                b.putString("projectSubDescription", project_subDescription.get(position));
//                b.putString("images", project_images.get(position));
//                b.putString("part", project_part.get(position));
//                b.putString("partName", project_partName.get(position));
//                b.putString("partDesc", project_partDesc.get(position));
//                b.putString("partimages", project_partimages.get(position));
//                b.putString("articlesId", project_part_articlesIds.get(position));
//                b.putString("articlesData", project_part_articlesData.get(position));
//                b.putString("part", project_part.get(position));

                intent.putExtras(b);
                context[0].startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return project_name.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView campaign_name, campaign_description;
        ImageView campaign_image;

        private LinearLayout campaign_container;

        public ViewHolder(View itemView) {
            super(itemView);
            campaign_container = itemView.findViewById(R.id.project_container);
            campaign_name = itemView.findViewById(R.id.project_title);
            campaign_description = itemView.findViewById(R.id.project_data);

        }
    }
}
