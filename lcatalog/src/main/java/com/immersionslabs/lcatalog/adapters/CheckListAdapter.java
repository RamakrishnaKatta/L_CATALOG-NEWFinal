package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.CheckListActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.ChecklistManager;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    private static final String TAG = "CheckListAdapter";
    private Activity activity;
    private SessionManager sessionManager;
    private ChecklistManager checklistManager;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_images;
    private ArrayList<String> item_discounts;
    private String total_val_text;
    String item_id=null;

    public CheckListAdapter(CheckListActivity activity,
                            ArrayList<String> item_ids,
                            ArrayList<String> item_names,
                            ArrayList<String> item_images,
                            ArrayList<String> item_prices,
                            ArrayList<String> item_discounts) {

        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_images = item_images;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_checklist, parent, false);
        sessionManager = new SessionManager(activity);
        checklistManager = new ChecklistManager();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.ViewHolder holder, final int position) {
        final Context[] context = new Context[1];
        String im1 = null;
        String get_image = item_images.get(position);
        try {
            JSONArray images_json = new JSONArray(get_image);
            for (int i = 0; i < images_json.length(); i++) {
                im1 = images_json.getString(0);
                Log.e(TAG, "onBindViewHolder: image1" + im1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.item_image);
        Integer x = Integer.parseInt(item_prices.get(position));
        Integer y = Integer.parseInt(item_discounts.get(position));
        final Integer z = (x * (100 - y)) / 100;
        final String itemNewPrice = Integer.toString(z);
        final Long price = Long.parseLong(itemNewPrice);

try
{
     item_id = item_ids.get(position);
}
catch(IndexOutOfBoundsException e)
{
    e.printStackTrace();
}
        holder.item_name.setText(item_names.get(position));
        holder.item_price_new.setText(itemNewPrice);

        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    sessionManager.CHECKLIST_REMOVE_ARTICLE(item_id, price);
                    total_val_text=sessionManager.CHECKLIST_GET_CURRENT_VALUE().toString();
                    Toast.makeText(activity, "Article is Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    checklistManager.CHECKLIST_REMOVE_ARTICLE(item_id, price);
                    total_val_text=checklistManager.CHECKLIST_GET_CURRENT().toString();
                    Toast.makeText(activity, "Article is Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                try
                {
                    item_ids.remove(position);
                    notifyItemRemoved(position);
                    TextView text_total_value=activity.findViewById(R.id.text_total_value);
                    text_total_value.setText(total_val_text);
                }
                catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_price, item_discount, item_price_new, item_delete;
        AppCompatImageView item_image;
        RelativeLayout check_list_container;

        public ViewHolder(View itemView) {
            super(itemView);
            check_list_container = itemView.findViewById(R.id.check_container);
            item_image = itemView.findViewById(R.id.check_item_image);
            item_name = itemView.findViewById(R.id.check_item_name);
            item_price_new = itemView.findViewById(R.id.check_item_price);
            item_delete = itemView.findViewById(R.id.check_item_delete_button);
        }
    }
}
