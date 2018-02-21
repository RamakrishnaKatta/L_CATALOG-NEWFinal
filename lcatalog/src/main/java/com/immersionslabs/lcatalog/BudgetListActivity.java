package com.immersionslabs.lcatalog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.BudgetManager;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.adapters.BudgetListAdapter;

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
    SessionManager sessionmanager;
    HashMap<String, Long> getdetails;
    BudgetManager budgetManager;
    KeyListener listener;

    private static String BUDGET_URL = null;

    String user_id, USER_LOG_TYPE;
    RecyclerView recycler;
    GridLayoutManager budgetlistmanager;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    Long current_value, total_budget_value, remaining_value;
    Set<String> setlist;

    String str_current_value, str_total_budget_value, str_remaining_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);

        budgetManager = new BudgetManager();
        USER_LOG_TYPE = EnvConstants.user_type;

        sessionmanager = new SessionManager(getApplicationContext());
        HashMap hashmap = new HashMap();

        hashmap = sessionmanager.getUserDetails();
        user_id = (String) hashmap.get(SessionManager.KEY_USER_ID);

        BUDGET_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";

        recycler = findViewById(R.id.budget_recycler);
        recycler.setHasFixedSize(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        setlist = new HashSet<String>();

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

        item_ids = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_3ds = new ArrayList<>();

        Alter_Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BudgetListActivity.this);
                builder.setTitle("Enter Your Budget");

                final EditText Total_budget_val = new EditText(BudgetListActivity.this);
                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    Total_budget_val.setHint(sessionmanager.GET_TOTAL_VALUE().toString());
                } else {
                    Total_budget_val.setHint(budgetManager.getTotal_Budget().toString());
                }

                Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(Total_budget_val);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    String budget_value;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (EnvConstants.user_type.equals("CUSTOMER")) {
                            budget_value = Total_budget_val.getText().toString();
                            if (budget_value.isEmpty()) {
                                Toast.makeText(BudgetListActivity.this, "Enter a value first", Toast.LENGTH_LONG).show();
                            } else {
                                sessionmanager.SET_TOTAL_VALUE(Long.parseLong(budget_value));
                                onResume();
                            }
                        } else {
                            budget_value = Total_budget_val.getText().toString();
                            if (budget_value.isEmpty()) {
                                Toast.makeText(BudgetListActivity.this, "Enter a value first", Toast.LENGTH_LONG).show();
                            } else {
                                budgetManager.setTotal_Budget(Long.parseLong(budget_value));
                                onResume();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }


                });
                builder.show();
            }
        });
    }

    private void CommongetData() {
        Log.e(TAG, "CommonGetData: " + BUDGET_URL);
        if (USER_LOG_TYPE.equals("CUSTOMER")) {

            setlist = sessionmanager.ReturnID();
            if (null == setlist || setlist.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                recycler.setVisibility(View.GONE);
            } else {
                Iterator iterator = setlist.iterator();
                while (iterator.hasNext()) {
                    String temp_budgtet_url = BUDGET_URL + iterator.next().toString();
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
            ArrayList<String> strings = budgetManager.GET_BUDGET_ARTICLE_IDS();
            if (null == strings || strings.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                recycler.setVisibility(View.GONE);
            } else {
                Iterator iterator = strings.iterator();
                while (iterator.hasNext()) {
                    String temp_budgtet_url = BUDGET_URL + iterator.next().toString();
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

        budgetlistmanager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(budgetlistmanager);
        BudgetListAdapter adapter = new BudgetListAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        recycler.setAdapter(adapter);
    }

    @Override
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

        CommongetData();

        if (EnvConstants.user_type.equals("CUSTOMER")) {
            sessionmanager = new SessionManager(getApplicationContext());
            getdetails = new HashMap<>();
            getdetails = sessionmanager.getBudgetDetails();

            current_value = getdetails.get(SessionManager.KEY_CURRENT_VALUE);
            total_budget_value = getdetails.get(SessionManager.KEY_TOTAL_BUDGET_VALUE);
            remaining_value = total_budget_value - current_value;

            str_total_budget_value = Long.toString(total_budget_value);
            str_current_value = Long.toString(current_value);
            str_remaining_value = Long.toString(remaining_value);
            Total_budget.setText(str_total_budget_value);
            Current_value.setText(str_current_value);

            Remaining_value.setText(str_remaining_value);

        } else {
            String Guest_Total_budget, Guest_Current_value, Guest_Remaining_budget;
            Guest_Total_budget = Long.toString(budgetManager.getTotal_Budget());
            Guest_Current_value = Long.toString(budgetManager.getCurrent_Value());
            Guest_Remaining_budget = Long.toString(budgetManager.getRemaining_Budget());
            Total_budget.setText(Guest_Total_budget);
            Current_value.setText(Guest_Current_value);
            Remaining_value.setText(Guest_Remaining_budget);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
