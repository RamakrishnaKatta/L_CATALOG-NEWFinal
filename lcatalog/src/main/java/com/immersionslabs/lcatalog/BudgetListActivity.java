package com.immersionslabs.lcatalog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.BudgetManager;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.adapters.MyFavoriteAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BudgetListActivity extends AppCompatActivity {

    private static final String TAG = "BudgetListActivity";

    EditText Total_budget, Current_value, Remaining_value;
    Button Alter_Budget;
    SessionManager sessionManager;
    private KeyListener listener;
    HashMap<String, Integer> getdetails;
    BudgetManager budgetManager;
    RecyclerView recycler;
    GridLayoutManager BUDGET_MANAGER;
    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    Integer current_value, total_budget_value, remaining_value;

    String str_current_value, str_total_budget_value, str_remaining_value;
    String BUDGET_LIST_URL = EnvConstants.APP_BASE_URL + "/vendorArticles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);

        budgetManager = new BudgetManager();

        recycler = findViewById(R.id.budget_recycler);
        recycler.setHasFixedSize(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        item_ids = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_3ds = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar_budget_list);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Total_budget = findViewById(R.id.input_your_budget);
        disableEditText(Total_budget);
        Current_value = findViewById(R.id.input_your_current_value);
        disableEditText(Current_value);
        Remaining_value = findViewById(R.id.input_your_remaining_value);
        disableEditText(Remaining_value);
        Alter_Budget = findViewById(R.id.btn_alter_budget);

        Alter_Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BudgetListActivity.this, BudgetBarActivity.class);
                startActivity(intent);
            }
        });

        CommongetData();
    }

    private void CommongetData() {

        Log.e(TAG, "Current User Type: " + EnvConstants.user_type);

        final JSONObject object = new JSONObject();

        if (EnvConstants.user_type.equals("CUSTOMER")) {
            Set setlist = new HashSet();
            setlist = sessionManager.GET_BUDGET_ARTICLES();
            if (setlist.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No articles found", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "Customer Articles Array : \n " + setlist);
                for (Object aSetlist : setlist) {

                    String BUDGET_ARTICLES_URL = BUDGET_LIST_URL + "/" + aSetlist.toString();
                    Log.e(TAG, "Budget Articles URL" + BUDGET_ARTICLES_URL);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BUDGET_ARTICLES_URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            GetData(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(jsonObjectRequest);
                }
            }
        } else if (EnvConstants.user_type.equals("GUEST")) {
            ArrayList arrayList = new ArrayList();
            arrayList = budgetManager.GET_BUDGET_ARTICLE_IDS();
            Log.e(TAG, "Guest Articles Array : \n " + arrayList);
            for (Object anArrayList : arrayList) {
                String BUDGET_ARTICLES_URL = BUDGET_LIST_URL + "/" + anArrayList.toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BUDGET_ARTICLES_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GetData(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(jsonObjectRequest);
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

        BUDGET_MANAGER = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(BUDGET_MANAGER);
        MyFavoriteAdapter adapter = new MyFavoriteAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        recycler.setAdapter(adapter);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setClickable(false);
        listener = editText.getKeyListener();
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EnvConstants.user_type.equals("CUSTOMER")) {
            sessionManager = new SessionManager(getApplicationContext());
            getdetails = new HashMap<>();
            getdetails = sessionManager.getBudgetDetails();

            current_value = getdetails.get(SessionManager.KEY_CURRENT_VALUE);
            total_budget_value = getdetails.get(SessionManager.KEY_TOTAL_BUDGET_VALUE);
            remaining_value = total_budget_value - current_value;

            str_total_budget_value = Integer.toString(total_budget_value);
            str_current_value = Integer.toString(current_value);
            str_remaining_value = Integer.toString(remaining_value);
            Total_budget.setText(str_total_budget_value);
            Current_value.setText(str_current_value);

            Remaining_value.setText(str_remaining_value);

        } else {
            String Guest_Total_budget, Guest_Current_value, Guest_Remaining_budget;
            Guest_Total_budget = Integer.toString(budgetManager.getTotal_Budget());
            Guest_Current_value = Integer.toString(budgetManager.getCurrent_Value());
            Guest_Remaining_budget = Integer.toString(budgetManager.getRemaining_Budget());
            Total_budget.setText(Guest_Total_budget);
            Current_value.setText(Guest_Current_value);
            Remaining_value.setText(Guest_Remaining_budget);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
//
//    @Override
//    public void onResponseCallback(JSONObject response, String flag) {
//
//        if (flag.equals("budget_list")) {
//            try {
//                JSONObject RESP = response.getJSONObject("data");
//                GetData(RESP);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onErrorCallback(VolleyError error, String flag) {
//        Toast.makeText(MyfavoriteActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
