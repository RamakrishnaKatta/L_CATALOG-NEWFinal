package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.NotifyActivity;
import com.immersionslabs.lcatalog.R;

import java.io.File;
import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static final String TAG = "NotificationAdapter";

    private Activity activity;

    private ArrayList<String> notification_ids;
    private ArrayList<String> notification_messages;
    private ArrayList<String> notification_images;
    private ArrayList<String> notification_titles;

    public NotificationAdapter(NotifyActivity activity,
                               ArrayList<String> notification_ids,
                               ArrayList<String> notification_messages,
                               ArrayList<String> notification_images,
                               ArrayList<String> notification_titles) {

        this.notification_ids = notification_ids;
        this.notification_messages = notification_messages;
        this.notification_images = notification_images;
        this.notification_titles = notification_titles;


        Log.e(TAG, "ids" + notification_ids);
        Log.e(TAG, "messages" + notification_messages);
        Log.e(TAG, "images" + notification_images);
        Log.e(TAG, "titles" + notification_titles);

        this.activity = activity;
    }

    /**
     * View holder to display each RecylerView item
     */

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title, message;
        private LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notify_image);
            container = itemView.findViewById(R.id.notification_container);
            title = itemView.findViewById(R.id.notification_title);
            message = itemView.findViewById(R.id.notification_data);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_notification, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];
        viewHolder.imageView.setImageResource(R.drawable.dummy_icon);


        String get_image = notification_images.get(position);
//        String new_image = get_image.replace("\\", File.separator);


        Glide
                .with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + get_image)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);

//        new DownloadImageTask(viewHolder.imageView).execute(new_image);

        viewHolder.title.setText(notification_titles.get(position));
        viewHolder.message.setText(notification_messages.get(position));

    }

    @Override
    public int getItemCount() {
        return notification_ids.size();
    }


}
