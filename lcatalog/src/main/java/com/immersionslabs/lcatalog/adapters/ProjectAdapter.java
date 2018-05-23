package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.ProjectActivity;
import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private static final String TAG = "ProjectAdapter";
    private Activity activity;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_name;
    private ArrayList<String> project_description;
    private ArrayList<String> project_subDescription;
    private ArrayList<String> project_images;
    private ArrayList<String> project_3ds;

    public ProjectAdapter(ProjectActivity activity,
                          ArrayList<String> project_ids,
                          ArrayList<String> project_name,
                          ArrayList<String> project_description,
                          ArrayList<String> project_subDescription,
                          ArrayList<String> project_images,
                          ArrayList<String> project_3ds) {

        this.project_ids = project_ids;
        this.project_name = project_name;
        this.project_description = project_description;
        this.project_subDescription = project_subDescription;
        this.project_images = project_images;
        this.project_3ds = project_3ds;

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, final int position) {

        final Context[] context = new Context[1];

        String im1 = null;
        String get_image = project_images.get(position);
        String get_project_id = project_ids.get(position);
        Log.e(TAG, " projectid  " + get_project_id);

        try {
            JSONArray images_json = new JSONArray(get_image);
            if (images_json.length() > 0) {
                im1 = images_json.getString(0);
                Log.e(TAG, "ProjectImage " + im1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/projectimages/" + get_project_id + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.campaign_image);

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
                b.putString("images", project_images.get(position));
                b.putString("projectView_3d", project_3ds.get(position));

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
        AppCompatImageView campaign_image;

        private LinearLayout campaign_container;

        public ViewHolder(View itemView) {
            super(itemView);
            campaign_container = itemView.findViewById(R.id.project_container);
            campaign_name = itemView.findViewById(R.id.project_title);
            campaign_description = itemView.findViewById(R.id.project_data);
            campaign_image = itemView.findViewById(R.id.project_image);
        }
    }
}
