package com.immersionslabs.lcatalog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.Manager_CheckList;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.adapters.CheckListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CheckListActivity extends AppCompatActivity {

    private static final String TAG = "CheckListActivity";

    private static String CHECKLIST_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";

    String USER_LOG_TYPE;

    SessionManager sessionManager;
    RecyclerView recycler_checklist;
    Manager_CheckList manager_checkList;
    LinearLayoutManager linearlayoutmanager;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    Set<String> set_list;

    TextView total_value;
    AppCompatButton Place_enquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        manager_checkList = new Manager_CheckList();
        USER_LOG_TYPE = EnvConstants.user_type;

        sessionManager = new SessionManager(getApplicationContext());

        recycler_checklist = findViewById(R.id.checklist_recycler);
        recycler_checklist.setHasFixedSize(true);
        recycler_checklist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        set_list = new HashSet<String>();

        Toolbar toolbar = findViewById(R.id.toolbar_check_list);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Place_enquiry = findViewById(R.id.place_enquiry);
        total_value = findViewById(R.id.text_total_value);

        if (EnvConstants.user_type.equals("CUSTOMER")) {
            String Total_value_text = sessionManager.CHECKLIST_GET_CURRENT_VALUE().toString();
            Log.e(TAG, "currentvalid" + Total_value_text);
            total_value.setText(Total_value_text);
        } else {
            String Total_value_text = manager_checkList.CHECKLIST_GET_CURRENT().toString();
            total_value.setText(Total_value_text);
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

        Place_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckListActivity.this, "Hey Wonderful, Will Get back you soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void commongetData() {
        Log.e(TAG, "CommonGetData: " + CHECKLIST_URL);

        if (USER_LOG_TYPE.equals("CUSTOMER")) {
            set_list = sessionManager.ReturnCheckListID();
            if (null == set_list || set_list.isEmpty()) {
                recycler_checklist.setVisibility(View.GONE);
            } else {
                Iterator iterator = set_list.iterator();
                while (iterator.hasNext()) {
                    String temp_checklist_url = CHECKLIST_URL + iterator.next().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, temp_checklist_url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RESP = null;
                            try {
                                RESP = response.getJSONObject("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            assert RESP != null;
                            GetData(RESP);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);
                }
            }
        } else if (USER_LOG_TYPE.equals("GUEST")) {
            ArrayList<String> strings = manager_checkList.CHECKLIST_GET_ARTICLE_IDS();
            if (null == strings || strings.isEmpty()) {
                recycler_checklist.setVisibility(View.GONE);
            } else {
                Iterator iterator = strings.iterator();
                while (iterator.hasNext()) {
                    String temp_budgtet_url = CHECKLIST_URL + iterator.next().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, temp_budgtet_url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RESP = null;
                            try {
                                RESP = response.getJSONObject("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            GetData(RESP);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);
                }
            }
        }
    }

    private void GetData(JSONObject obj) {

        try {
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

        Log.e(TAG, "Ids" + item_ids);
        Log.e(TAG, "Names" + item_names);
        Log.e(TAG, "Descriptions" + item_descriptions);
        Log.e(TAG, "Prices" + item_prices);
        Log.e(TAG, "Images" + item_images);
        Log.e(TAG, "Dimensions" + item_dimensions);
        Log.e(TAG, "Discounts" + item_discounts);
        Log.e(TAG, "3ds" + item_3ds);
        Log.e(TAG, "Vendors" + item_vendors);

        linearlayoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_checklist.setLayoutManager(linearlayoutmanager);
        CheckListAdapter adapter = new CheckListAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        recycler_checklist.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();

        item_ids.clear();
        item_names.clear();
        item_descriptions.clear();
        item_prices.clear();
        item_images.clear();
        item_discounts.clear();
        item_3ds.clear();
        item_dimensions.clear();
        item_vendors.clear();

        commongetData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}