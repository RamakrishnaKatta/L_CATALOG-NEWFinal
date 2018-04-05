package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.immersionslabs.lcatalog.CampaignActivity;
import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.R;

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

    public ProjectPartAdapter(ProjectDetailActivity activity,
                              ArrayList<String> project_part,
                              ArrayList<String> project_partName,
                              ArrayList<String> project_partDesc,
                              ArrayList<String> project_partimages,
                              ArrayList<String> project_part_articlesIds,
                              ArrayList<String> project_part_articlesData) {

        this.project_part = project_part;
        this.project_partName = project_partName;
        this.project_partDesc = project_partDesc;
        this.project_part_articlesIds = project_part_articlesIds;
        this.project_part_articlesData = project_part_articlesData;
        this.project_partimages = project_partimages;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project_parts, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(ProjectPartAdapter.ViewHolder holder, int position) {
        final Context[] context = new Context[1];
        holder.projectpart_name.setText(project_partName.get(position));
        holder.projectpart_Desc.setText(project_partDesc.get(position));


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
        }
    }
}
