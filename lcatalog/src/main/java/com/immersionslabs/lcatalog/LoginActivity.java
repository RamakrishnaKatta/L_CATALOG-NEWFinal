package com.immersionslabs.lcatalog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.immersionslabs.lcatalog.Utils.CryptionRijndeal;
import com.immersionslabs.lcatalog.Utils.CustomMessage;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.NetworkConnectivity;
import com.immersionslabs.lcatalog.Utils.PrefManager;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.Utils.UserCheckUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

import static com.immersionslabs.lcatalog.Utils.EnvConstants.user_Favourite_list;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_FORGOT_PASSWORD = 0;
    private static final String LOGIN_URL = EnvConstants.APP_BASE_URL + "/customerLogin";
    private static final String Local_url = "http://192.168.0.10:4000/customerLogin";
    SessionManager sessionmanager;
    SharedPreferences preferences;
    CryptionRijndeal rijndeal_obj;
    TextView app_name, _forgot_password, powered;
    EditText _emailText, _passwordText;
    Button _loginButton;
    ImageButton get_details;
    CoordinatorLayout LoginLayout;
    String userId, globalUserId;
    String resp, code, message;
    String userName, userEmail, userPhone, userAddress, userType;
    String email, password;
    File file_customer;
    String[] text_from_customer_file;
    ArrayList<String> temp = new ArrayList<String>();
    private PrefManager prefManager5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userType = "CUSTOMER";

        sessionmanager = new SessionManager(getApplicationContext());
        app_name = findViewById(R.id.application_name);
        powered = findViewById(R.id.immersionslabs);

        _loginButton = findViewById(R.id.btn_login);
        _forgot_password = findViewById(R.id.link_forgot_password);
        get_details = findViewById(R.id.btn_get_data);
        LoginLayout = findViewById(R.id.LoginLayout);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sessionmanager = new SessionManager(getApplicationContext());
        rijndeal_obj = new CryptionRijndeal();

        SharedPreferences settings = this.getSharedPreferences("LoginSession", Context.MODE_PRIVATE);
        String customer_text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOG/customer.txt";
        file_customer = new File(customer_text_file_location);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);
        _forgot_password.setTypeface(custom_font2);
        _forgot_password.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
//                    overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivityForResult(intent, REQUEST_FORGOT_PASSWORD);
            }
        });

        prefManager5 = new PrefManager(this);
        Log.e(TAG, "" + prefManager5.LoginActivityScreenLaunch());
        if (prefManager5.LoginActivityScreenLaunch()) {
            showcaseview();
        }

        if (NetworkConnectivity.checkInternetConnection(LoginActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(LoginActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    private void showcaseview() {
        prefManager5.SetLoginActivityScreenLaunch();
        Log.e(TAG, "" + prefManager5.LoginActivityScreenLaunch());

        final Display display = getWindowManager().getDefaultDisplay();
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btn_get_data), "Click here to Autofill your recent details ")
                        .cancelable(false)
                        .tintTarget(false)
                        .textColor(R.color.white)
                        .targetRadius(30)
                        .outerCircleColor(R.color.primary_dark)
                , new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                    }
                });
    }

    private void setData() {

        if (!file_customer.exists()) {
            CustomMessage.getInstance().CustomMessage(LoginActivity.this, "No Saved Details Yet");

        } else {
            text_from_customer_file = UserCheckUtil.readFromFile("customer").split(" ### ");
            String dec_password_text = null;
            String dec_email_text = null;
            try {
                dec_email_text = rijndeal_obj.decrypt(text_from_customer_file[0].trim());
                dec_password_text = rijndeal_obj.decrypt(text_from_customer_file[1].trim());
            } catch (Exception e) {
                e.printStackTrace();
            }

            _emailText = findViewById(R.id.input_email);
            _emailText.setText(dec_email_text);

            _passwordText = findViewById(R.id.input_password);
            _passwordText.setText(dec_password_text);
        }

        _loginButton.setEnabled(true);
    }

    public void login() throws JSONException {
        Log.e(TAG, "Customer Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton = findViewById(R.id.btn_login);
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        _emailText = findViewById(R.id.input_email);
        email = _emailText.getText().toString().trim();
        Log.e(TAG, "Entered email--" + email);

        _passwordText = findViewById(R.id.input_password);
        password = _passwordText.getText().toString().trim();
        Log.e(TAG, "Entered password--" + password);

        // Implement your own authentication logic here.
        try {
            String enc_email_text = rijndeal_obj.encrypt(email);
            String enc_password_text = rijndeal_obj.encrypt(password);
            final String Credentials = enc_email_text + "  ###  " + enc_password_text;
            UserCheckUtil.writeToFile(Credentials, "customer");
            String text_file_data = UserCheckUtil.readFromFile("customer");
            Log.e(TAG, "User Details-- " + text_file_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JSONObject login_parameters = new JSONObject();
        login_parameters.put("email", email);
        login_parameters.put("password", password);
        Log.e(TAG, "Request--" + login_parameters);

        final JSONObject request = new JSONObject();
        request.put("request", login_parameters);
        Log.e(TAG, "Request--" + request);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, login_parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject requestResponse) {
                Log.e(TAG, "response--" + requestResponse);

                try {
                    resp = requestResponse.getString("success");
                    code = requestResponse.getString("status_code");
                    message = requestResponse.getString("message");
                    Log.e(TAG, "resp " + resp + " code--" + code + " message--" + message);

                    JSONArray user_details_array = requestResponse.getJSONArray("data");
                    for (int i = 0; i < user_details_array.length(); i++) {

                        JSONObject user_details = user_details_array.getJSONObject(i);
                        globalUserId = user_details.getString("_id");
                        userId = user_details.getString("id");
                        userName = user_details.getString("name");
                        userAddress = user_details.getString("address");
                        userEmail = user_details.getString("email");
                        userPhone = user_details.getString("mobile_no");

                        JSONArray fav_ids = user_details.getJSONArray("article_ids");
                        final int no_of_fav_ids = fav_ids.length() - 1;
                        for (int j = 0; j <= no_of_fav_ids; j++) {
                            temp.add(fav_ids.getString(j));
                        }

                        user_Favourite_list = temp;

                        Log.e(TAG, "favourite JSON Ids " + fav_ids);
                        Log.e(TAG, "favourite Array List Ids " + user_Favourite_list);
                        Log.e(TAG, "User Name > " + userName + "\n User Address > " + userAddress + "\n User Email > " + userEmail + "\n User Phone > " + userPhone);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Internal Error", Toast.LENGTH_LONG).show();
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject request = new JSONObject(res);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // On complete call either onLoginSuccess or onLoginFailed
                if (Objects.equals(message, "FAILURE") || Objects.equals(code, "500")) {
                    onLoginFailed();
                } else {
                    onLoginSuccess();
                }
                progressDialog.dismiss();
            }
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onLoginSuccess() {

        Button _loginButton = findViewById(R.id.btn_login);

        //CustomMessage.getInstance().CustomMessage(LoginActivity.this, "Login Success");
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);

        if (userName != null & userPhone != null & userAddress != null & userEmail != null) {

            sessionmanager.signupthings();
            sessionmanager.createUserLoginSession(globalUserId, userId, userType, userName, userEmail, userPhone, userAddress, password);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        } else {
            _loginButton = findViewById(R.id.btn_login);
//            CustomMessage.getInstance().CustomMessage(LoginActivity.this, "There is a issue with your Login, maybe a network/server issue! " +
//                    "\n Please try to login as guest for this time");
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }
    }

    public void onLoginFailed() {
        Button _loginButton = findViewById(R.id.btn_login);
       // CustomMessage.getInstance().CustomMessage(LoginActivity.this, "Login failed Please Sign Up or Try Logging Again");
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
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
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    public boolean validate() {

        EditText _emailText = findViewById(R.id.input_email);
        EditText _passwordText = findViewById(R.id.input_password);
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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
