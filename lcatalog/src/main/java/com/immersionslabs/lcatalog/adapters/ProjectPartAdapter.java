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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.CampaignActivity;
import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.ProjectpartDetailsActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProjectPartAdapter extends RecyclerView.Adapter<ProjectPartAdapter.ViewHolder> {


    private static final String TAG = "ProjectPartAdapter";
    private Activity activity;

    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_ids;

    public ProjectPartAdapter(ProjectDetailActivity activity,
                              ArrayList<String> project_part,
                              ArrayList<String> project_partName,
                              ArrayList<String> project_partDesc,
                              ArrayList<String> project_partimages,
                              ArrayList<String> project_part_articlesIds,
                              ArrayList<String> project_part_articlesData,
                              ArrayList<String> project_ids) {

        this.project_part = project_part;
        this.project_partName = project_partName;
        this.project_partDesc = project_partDesc;
        this.project_part_articlesIds = project_part_articlesIds;
        this.project_part_articlesData = project_part_articlesData;
        this.project_partimages = project_partimages;
        this.project_ids = project_ids;
        this.activity = activity;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project_parts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectPartAdapter.ViewHolder holder, final int position) {
        final Context[] context = new Context[1];
        String im1 = null;
        String get_image = project_partimages.get(position);
        String get_project_id = project_ids.get(position);
        Log.e(TAG, " project_ids" + get_project_id);


        holder.projectpart_name.setText(project_partName.get(position));
        holder.projectpart_Desc.setText(project_partDesc.get(position));
        try {
            JSONArray images_json = new JSONArray(get_image);
            if (images_json.length() > 0) {
                im1 = images_json.getString(0);
                Log.e(TAG, "project_part_Image" + im1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/projectpartimages/partimages_" + get_project_id + "_" + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.projectpart_image);

        holder.projectpart_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context[0] = v.getContext();
                Intent intent = new Intent(context[0], ProjectpartDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("_id", project_ids.get(position));
                b.putString("part", project_part.get(position));
                b.putString("partName", project_partName.get(position));
                b.putString("partDesc", project_partDesc.get(position));
                b.putString("partimages", project_partimages.get(position));
                b.putString("articlesId", project_part_articlesIds.get(position));
                b.putString("articlesData", project_part_articlesData.get(position));

                intent.putExtras(b);
                context[0].startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return project_partName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView projectpart_name, projectpart_Desc;
        private ImageView projectpart_image;
        private RelativeLayout projectpart_container;

        public ViewHolder(View itemView) {
            super(itemView);
            projectpart_container = itemView.findViewById(R.id.project_part_container);
            projectpart_name = itemView.findViewById(R.id.project_part_name);
            projectpart_Desc = itemView.findViewById(R.id.project_part_description);
            projectpart_image = itemView.findViewById(R.id.project_part_image);
        }
    }
}
