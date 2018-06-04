package com.immersionslabs.lcatalog;

import android.os.Bundle;
import android.os.Message;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityResult;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.Manager_CheckList;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.adapters.CheckListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CheckListActivity extends AppCompatActivity {

    private static final String TAG = "CheckListActivity";

    private  String CHECKLIST_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";

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
    static Set<String> set_list,set_checklist_vendorids,set_checklist_articlevendorids;
     static HashMap<String,Set> map_vendorids, map_vendorarticleids;
     static HashMap<String,HashMap>map_vendordetails;
String VENDORNAME,VENDOREMAIL,VENDORMOBILE,VENDORID;
    boolean is_email_valid_boolean =false;
    TextView total_value;
    AppCompatButton Place_enquiry;

    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private int result;
    private boolean returnval_emaivalidation;

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
        set_checklist_vendorids = new HashSet<String>();
        set_checklist_articlevendorids = new HashSet<String>();
        map_vendorarticleids = new HashMap<>();
        map_vendorids = new HashMap<>();
        map_vendordetails = new HashMap<>();
        Toolbar toolbar = findViewById(R.id.toolbar_check_list);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
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
            Log.e(TAG, "currentvalid " + Total_value_text);
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
                HashMap userDetails = sessionManager.getUserDetails();
                String _user_email = userDetails.get(SessionManager.KEY_EMAIL).toString();
                Log.e(TAG, "useremail" + _user_email);

is_email_valid_boolean =is_email_valid(_user_email);
if(is_email_valid_boolean)
{
    Toast.makeText(CheckListActivity.this,"User email is already verified,sending test mail",Toast.LENGTH_LONG).show();
    Iterator iterator = set_checklist_vendorids.iterator();
    while (iterator.hasNext()) {
        String vendor_id = iterator.next().toString();
        String vendor_id_clone = vendor_id;
        Integer integer_vendor_id=Integer.parseInt(vendor_id)+1;
        vendor_id=String.valueOf(integer_vendor_id);
        Log.e(TAG, "vendoridfromsendmail" + vendor_id);
        HashMap hashMap = map_vendordetails.get(vendor_id);
       String vendor_email= hashMap.get(vendor_id + SessionManager.KEY_VENDOR_EMAIL).toString();
        Set articleids = map_vendorarticleids.get(vendor_id_clone);
        Log.e(TAG, "articleidset" + articleids);
        if(articleids!=null)
        {
            sendEmail(articleids, vendor_email, _user_email);
        }

    }


}
else
{
    Toast.makeText(CheckListActivity.this,"your email is not a verified email,sending a verification email.......",Toast.LENGTH_LONG).show();
    VerifyEmail(_user_email);
}
            }
        });


        set_checklist_vendorids = sessionManager.GetCheckVendorId();
        Log.e(TAG, "setchecklistvendorids" + set_checklist_vendorids);

        if (null == set_checklist_vendorids) {

        } else {
            Iterator iterator1 = set_checklist_vendorids.iterator();
            while (iterator1.hasNext()) {
                String vendorid = iterator1.next().toString();
//                int vendorid_int=Integer.parseInt(vendorid)+1;
//                vendorid=Integer.toString(vendorid_int);
                set_checklist_articlevendorids = sessionManager.GetCheckArticleVendorId(vendorid);
                Log.e(TAG, "articlevendorid" + vendorid);
                if (set_checklist_articlevendorids != null) {
                    map_vendorarticleids.put(vendorid, set_checklist_articlevendorids);
                    Log.e(TAG, "vendoridfrommap" + vendorid);
                }
            }
            Log.e(TAG, "maparticlevendor" + map_vendorarticleids);

        }
        if (set_checklist_vendorids != null) {
            Iterator iterator = set_checklist_vendorids.iterator();
            while (iterator.hasNext()) {
                String vendorid = iterator.next().toString();
                int vendorid_int = Integer.parseInt(vendorid) + 1;
                vendorid = Integer.toString(vendorid_int);
                map_vendordetails.put(vendorid, sessionManager.GetVendorDetails(vendorid));

            }
            Log.e(TAG, "vendordetails" + map_vendordetails);
        }
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
    private boolean is_email_valid(String emailid) {

        final String email = emailid;
        Thread EmailValidationThread = new Thread(new Runnable() {
            public void run() {
                try {
                    CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(CheckListActivity.this
                            , "us-east-1:199fd199-d4f1-412e-9352-7918b6a69e94", Regions.US_EAST_1);
                    AmazonSimpleEmailServiceClient ses = new AmazonSimpleEmailServiceClient(credentials);

                    List lids = ses.listIdentities().getIdentities();
                    Log.e(TAG, "lids: " + lids);

                    if (lids.contains(email)) {
                        //the address is verified so
                        returnval_emaivalidation = true;
                    }

                } catch (Exception e) {

                    Log.e(TAG, "isemailverifiederror: " + e.getMessage());
                }
            }
        });

        // RUNS SEND EMAIL THREAD
        EmailValidationThread.start();
        try {
            // WAITS THREAD TO COMPLETE TO ACT ON RESULT
            EmailValidationThread.join();

              } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnval_emaivalidation;
    }
    private void VerifyEmail(final String email) {

        Thread verifyEmailThread = new Thread(new Runnable() {
            public void run() {
                try {
                    CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(CheckListActivity.this
                            , "us-east-1:199fd199-d4f1-412e-9352-7918b6a69e94", Regions.US_EAST_1);
                    final AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
                    client.setRegion(Region.getRegion(Regions.US_EAST_1));
                    VerifyEmailIdentityRequest request = new VerifyEmailIdentityRequest().withEmailAddress(email);
                    VerifyEmailIdentityResult response = client.verifyEmailIdentity(request);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "verifyemailexception: " + e.getMessage());
                }
            }
        });

        // RUNS SEND EMAIL THREAD
        verifyEmailThread.start();
        try {
            // WAITS THREAD TO COMPLETE TO ACT ON RESULT
            verifyEmailThread.join();

            if (result == SUCCESS) {
                Toast.makeText(CheckListActivity.this, "verification email sent to your mail.", Toast.LENGTH_LONG)
                        .show();

            } else if (result == ERROR) {
                Toast.makeText(CheckListActivity.this, "error", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(CheckListActivity.this, "unkown error", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void sendEmail(Set articleids, String vendor_email_text, String useremail) {

        String body_text = null;
        int Index;
        String article_name, article_desc, article_price, subject_text = null, vendor_email = null, user_email = useremail, article_id = null;
        HashMap userDetails = sessionManager.getUserDetails();
        vendor_email = vendor_email_text;
        String username = userDetails.get(SessionManager.KEY_NAME).toString();
        Iterator iterator = articleids.iterator();

        while (iterator.hasNext()) {
          try
          {
              Index  = item_ids.indexOf(article_id);
              Log.e(TAG,"ITEM_IDS"+item_ids);
              Log.e(TAG,"ARTICLE_ID"+article_id);
              article_name = item_names.get(Index);
              article_desc = item_descriptions.get(Index);
              article_price = item_prices.get(Index);
              body_text = username + "'s" + " CheckList" + "\n" + "\n" +
                      "ARTICLE NAME : " + article_name + "\n" +
                      "ARTICLE PRICE : " + article_price + "\n" +
                      "ARTICLE DESCRIPTION : " + article_desc;
              subject_text = username + "'s" + " CheckList";
              body_text += "\n" + "\n" + "\n";
          }
          catch(IndexOutOfBoundsException e)
          {
              e.printStackTrace();
              break;
          }


        }

        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(CheckListActivity.this
                , "us-east-1:199fd199-d4f1-412e-9352-7918b6a69e94", Regions.US_EAST_1);

        final AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
        client.setRegion(Region.getRegion(Regions.US_EAST_1));

        Content subject = new Content(subject_text);
        Body body = new Body(new Content(body_text));
        final com.amazonaws.services.simpleemail.model.Message message = new com.amazonaws.services.simpleemail.model.Message(subject,body);

        final String from = "enquiry@immersionslabs.com";
        String to = vendor_email;
        String cc = user_email;

        final Destination destination = new Destination()
                .withToAddresses(to.contentEquals("") ? null : Arrays.asList(to.split("\\s*,\\s*")))
                .withCcAddresses(cc.contentEquals("") ? null : Arrays.asList(cc.split("\\s*,\\s*")));

        // CREATES SEPARATE THREAD TO ATTEMPT TO SEND EMAIL
        Thread sendEmailThread = new Thread(new Runnable() {
            public void run() {
                try {
                    SendEmailRequest request = new SendEmailRequest(from, destination, message);
                    client.sendEmail(request);
                    result = SUCCESS;

                } catch (Exception e) {
                    result = ERROR;
                    Log.e(TAG, "emailerorr: " + e.getMessage());
                }
            }
        });

        // RUNS SEND EMAIL THREAD
        sendEmailThread.start();
        try {
            // WAITS THREAD TO COMPLETE TO ACT ON RESULT
            sendEmailThread.join();

            if (result == SUCCESS) {
                Toast.makeText(CheckListActivity.this, "Email Sent Successfully", Toast.LENGTH_LONG)
                        .show();
            } else if (result == ERROR) {
                Toast.makeText(CheckListActivity.this, "Email Sent Failure", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(CheckListActivity.this, "UnExpected Error Please Try again", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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