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
import com.immersionslabs.lcatalog.adapters.MyFavoriteAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.immersionslabs.lcatalog.Utils.EnvConstants.UserId;

public class MyfavoriteActivity extends AppCompatActivity implements ApiCommunication {
    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/users/favouriteArticles/";
    private static String FAVOURITE_URL = null;
    private static final String TAG = "MyfavoriteActivity";

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    RecyclerView recycler;
    GridLayoutManager favoritemanager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        FAVOURITE_URL = REGISTER_URL + UserId;

        recycler = findViewById(R.id.favorite_recycler);
        recycler.setHasFixedSize(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_favorite);
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

        CommongetData();
    }

    private void CommongetData() {
        Log.e(TAG, "CommongetData: " + REGISTER_URL);
        final JSONObject object = new JSONObject();

        ApiService.getInstance(this).getData(this, false, "FAVORITE", FAVOURITE_URL, "FAVORITE_LIST");
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

        Log.e(TAG, "Ids" + item_ids);
        Log.e(TAG, "Names" + item_names);
        Log.e(TAG, "Descriptions" + item_descriptions);
        Log.e(TAG, "Prices" + item_prices);
        Log.e(TAG, "Images" + item_images);
        Log.e(TAG, "Dimensions" + item_dimensions);
        Log.e(TAG, "Discounts" + item_discounts);
        Log.e(TAG, "3ds" + item_3ds);
        Log.e(TAG, "Vendors" + item_vendors);

        favoritemanager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(favoritemanager);
        MyFavoriteAdapter adapter = new MyFavoriteAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        recycler.setAdapter(adapter);
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

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("FAVORITE_LIST")) {
            Log.e(TAG, "onResponse: response" + response);

            try {
                JSONArray resp = response.getJSONArray("data");
                GetData(resp);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(MyfavoriteActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

    }
}
