package com.immersionslabs.lcatalog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.ChecklistManager;
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
    TextView totalvalue;
    ChecklistManager checklistManager;
    SessionManager sessionManager;

    private static String CHECKLIST_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";
    String USER_ID, USER_LOG_TYPE;
    RecyclerView checklist_recycler;
    LinearLayoutManager check_list_manager;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_images;
    private ArrayList<String> item_discounts;

    Set<String> set_list;

    Button check_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        totalvalue = findViewById(R.id.text_total_value);
        checklistManager = new ChecklistManager();
        sessionManager = new SessionManager(getApplicationContext());


        USER_LOG_TYPE = EnvConstants.user_type;

        if (EnvConstants.user_type.equals("CUSTOMER")) {
            String Total_value_text = sessionManager.CHECKLIST_GET_CURRENT_VALUE().toString();
            Log.e(TAG, "currentvalid" + Total_value_text);
            totalvalue.setText(Total_value_text);
        } else {
            String Total_value_text = checklistManager.CHECKLIST_GET_CURRENT().toString();
            totalvalue.setText(Total_value_text);
        }


        checklist_recycler = findViewById(R.id.checklist_recycler);
        checklist_recycler.setHasFixedSize(true);
        checklist_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        set_list = new HashSet<String>();


        Toolbar toolbar = findViewById(R.id.toolbar_check_list);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        item_ids = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        commongetData();

        super.onResume();
    }

    private void commongetData() {
        Log.e(TAG, "CommonGetData: " + CHECKLIST_URL);
        if (USER_LOG_TYPE.equals("CUSTOMER")) {
            set_list = sessionManager.ReturnCheckListID();
            if (null == set_list || set_list.isEmpty()) {
                checklist_recycler.setVisibility(View.GONE);
            } else {
                Iterator iterator = set_list.iterator();
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
        } else if (USER_LOG_TYPE.equals("GUEST")) {
            ArrayList<String> strings = checklistManager.CHECKLIST_GET_ARTICLE_IDS();
            if (null == strings || strings.isEmpty()) {
                checklist_recycler.setVisibility(View.GONE);
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
            item_prices.add(obj.getString("price"));
            item_images.add(obj.getString("img"));
            item_discounts.add(obj.getString("discount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Ids" + item_ids);
        Log.e(TAG, "Names" + item_names);
        Log.e(TAG, "Prices" + item_prices);
        Log.e(TAG, "Images" + item_images);
        Log.e(TAG, "Discounts" + item_discounts);


        check_list_manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        checklist_recycler.setLayoutManager(check_list_manager);
        CheckListAdapter adapter = new CheckListAdapter(this, item_ids, item_names, item_images, item_prices, item_discounts);
        checklist_recycler.setAdapter(adapter);
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