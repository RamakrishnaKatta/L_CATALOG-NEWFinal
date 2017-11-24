package com.immerrsionslabs.l_catalog_new;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immerrsionslabs.l_catalog_new.Utils.NetworkConnectivity;
import com.immerrsionslabs.l_catalog_new.adapters.NotificationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class NotifyActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://lcatalog.immersionslabs.com:8080/lll/app/notify/get_all";
    private static final String TAG = "NotifyActivity";

    ArrayList<String> notification_titles;
    ArrayList<String> notification_messages;
    ArrayList<String> notification_images;
    ArrayList<String> notification_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Toolbar notify_toolbar = findViewById(R.id.notify_toolbar);
        setSupportActionBar(notify_toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Firebase is Getting Activated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        notification_titles = new ArrayList<>();
        notification_messages = new ArrayList<>();
        notification_images = new ArrayList<>();
        notification_ids = new ArrayList<>();

        GetNotificationData();

        if (NetworkConnectivity.checkInternetConnection(NotifyActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(NotifyActivity.this)) {
                    Intent intent = new Intent(NotifyActivity.this, AboutUsActivity.class);
                    startActivity(intent);
                } else {

                    InternetMessage();
                    // CustomMessage.getInstance().CustomMessage(this,"Check Your Internet connection.");
                }
            }
        });
        snackbar.show();
    }


    private void GetNotificationData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        final JSONObject baseclass = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL, baseclass, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response--" + response);

                try {
                    JSONArray resp = response.getJSONArray("resp");
                    loading.dismiss();
                    NotificationView(resp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NotifyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject request = new JSONObject(res);
                        Log.e(TAG, "request--" + request);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    private void NotificationView(JSONArray resp) {
        RecyclerView recyclerView = findViewById(R.id.Notification_recycler);
        recyclerView.setHasFixedSize(true);

        for (int i = 0; i < resp.length(); i++) {
            JSONObject obj;
            try {
                obj = resp.getJSONObject(i);

                notification_ids.add(obj.getString("id"));
                notification_messages.add(obj.getString("msg"));
                notification_images.add(obj.getString("path"));
                notification_titles.add(obj.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "ids******" + notification_ids);
        Log.e(TAG, "messages******" + notification_messages);
        Log.e(TAG, "images******" + notification_images);
        Log.e(TAG, "titles******" + notification_titles);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(this, notification_ids, notification_messages, notification_images, notification_titles);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
