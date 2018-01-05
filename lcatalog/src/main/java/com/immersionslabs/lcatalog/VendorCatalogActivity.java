package com.immersionslabs.lcatalog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.VendorCatalogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class VendorCatalogActivity extends AppCompatActivity {

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/userArticles/";
    private static final String TAG = "VendorCatalogActivity";
    private static String VENDOR_URL = null;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    RecyclerView vendor_recycler;
    GridLayoutManager VendorCatalogManager;
    String vendor_id;
    Integer vendor_id_no;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_catalog);

        vendor_recycler = findViewById(R.id.vendor_catalog_recycler);
        vendor_recycler.setHasFixedSize(true);
        vendor_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_vendor_catalog);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        item_ids = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_3ds = new ArrayList<>();

        final Bundle vendor_data = getIntent().getExtras();
        vendor_id = vendor_data.getString("vendor_id").trim();
        vendor_id_no = Integer.parseInt(vendor_id);
        vendor_id_no = vendor_id_no + 1;
        vendor_id = String.valueOf(vendor_id_no);
        Log.e(TAG, "Vendor Id" + vendor_id);
        VENDOR_URL = REGISTER_URL + vendor_id;

        Log.e(TAG, "VENDOR_URL" + VENDOR_URL);

        try {
            CommonGetData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CommonGetData() throws JSONException {

        Log.e(TAG, "CommonGetData: " + REGISTER_URL);
        final JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VENDOR_URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response" + response);
                Log.e(TAG, "-----------------------------------------------------------------------------------------------------------------------");

                try {
                    JSONArray resp = response.getJSONArray("data");
                    GetData(resp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VendorCatalogActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
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

    private void GetData(JSONArray resp) {

        for (int i = 0; i < resp.length(); i++) {
            JSONObject obj = null;

            try {
                obj = resp.getJSONObject(i);

                item_ids.add(obj.getString("_id"));
                item_names.add(obj.getString("name"));
                item_descriptions.add(obj.getString("description"));
                item_prices.add(obj.getString("price"));
                item_images.add(obj.getString("img"));
                item_discounts.add(obj.getString("discount"));
                item_3ds.add(obj.getString("view_3d"));
                item_dimensions.add(obj.getString("dimensions"));
                item_vendors.add(obj.getString("vendor_id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "GetData: ids" + item_ids);
        Log.e(TAG, "GetData: name" + item_names);
        Log.e(TAG, "GetData: description" + item_descriptions);
        Log.e(TAG, "GetData: price" + item_prices);
        Log.e(TAG, "GetData: img" + item_images);
        Log.e(TAG, "GetData: dimensions" + item_dimensions);
        Log.e(TAG, "GetData: discount" + item_discounts);
        Log.e(TAG, "GetData: view_3d" + item_3ds);
        Log.e(TAG, "GetData: vendor_id" + item_vendors);

        VendorCatalogManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vendor_recycler.setLayoutManager(VendorCatalogManager);
        VendorCatalogAdapter adapter = new VendorCatalogAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        vendor_recycler.setAdapter(adapter);
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
