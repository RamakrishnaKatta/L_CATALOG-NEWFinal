package com.immersionslabs.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
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

import com.android.volley.AuthFailureError;
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
import com.immersionslabs.lcatalog.Utils.CustomMessage;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.NetworkConnectivity;
import com.immersionslabs.lcatalog.Utils.PrefManager;
import com.immersionslabs.lcatalog.Utils.UserCheckUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private PrefManager prefManager5;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_FORGOT_PASSWORD = 0;

    private static final String LOGIN_URL = EnvConstants.APP_BASE_URL + "/customerLogin";
    private static final String Local_url = "http://192.168.0.10:4000/customerLogin";

    TextView app_name, _forgot_password, powered;
    EditText _emailText, _passwordText;
    Button _loginButton;
    ImageButton get_details;
    CoordinatorLayout LoginLayout;

    String resp, code, message;
    String userName, userEmail, userPhone, userAddress;
    String email, password;

    File file_customer;
    String[] text_from_customer_file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app_name = findViewById(R.id.application_name);
        powered = findViewById(R.id.immersionslabs);

        _loginButton = findViewById(R.id.btn_login);
        _forgot_password = findViewById(R.id.link_forgot_password);
        get_details = findViewById(R.id.btn_get_data);
        LoginLayout = findViewById(R.id.LoginLayout);

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
        final Snackbar snackbar = Snackbar.make(view, "Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
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

            _emailText = findViewById(R.id.input_email);
            _emailText.setText(text_from_customer_file[0].trim());

            _passwordText = findViewById(R.id.input_password);
            _passwordText.setText(text_from_customer_file[1].trim());
        }

        _loginButton.setEnabled(true);
    }

    public void login() throws JSONException {
        Log.e(TAG, "Customer Login");

        Log.e(TAG, "KEY_EMAIL--" + "email");
        Log.e(TAG, "KEY_PASSWORD--" + "password");

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

        final String Credentials = email + "  ###  " + password;
        UserCheckUtil.writeToFile(Credentials, "customer");
        String text_file_date = UserCheckUtil.readFromFile("customer");
        Log.e(TAG, "User Details-- " + text_file_date);

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
                        userName = user_details.getString("name");
                        userAddress = user_details.getString("adress");
                        userEmail = user_details.getString("email");
                        userPhone = user_details.getString("mobile_no");
                        Log.e(TAG, "User Name > " + userName + "\n User Address > " + userAddress + "\n User Email > " + userEmail + "\n User Phone > " + userPhone);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
        }) {
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                Log.e(TAG, "Hash map...." + String.valueOf(params));
                Log.e(TAG, "Hash map .. " + params);
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

        CustomMessage.getInstance().CustomMessage(LoginActivity.this, "Login Success");

        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Bundle user_data = new Bundle();
        user_data.putString("name", userName);
        user_data.putString("phone", userPhone);
        user_data.putString("address", userAddress);
        user_data.putString("email", userEmail);
        Log.e(TAG, "User -- " + user_data);

        if (userName != null & userPhone != null & userAddress != null & userEmail != null) {
            Intent intent = new Intent(this, MainActivity.class).putExtras(user_data);
            startActivity(intent);
            finish();
        } else {
            _loginButton = findViewById(R.id.btn_login);
            CustomMessage.getInstance().CustomMessage(LoginActivity.this, "There is a issue with your Login, maybe a network/server issue! " +
                    "\n Please try to login as guest for this time");

            _loginButton.setEnabled(true);
        }
    }

    public void onLoginFailed() {
        Button _loginButton = findViewById(R.id.btn_login);
        CustomMessage.getInstance().CustomMessage(LoginActivity.this, "Login failed Please Sign Up or Try Logging Again");

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
