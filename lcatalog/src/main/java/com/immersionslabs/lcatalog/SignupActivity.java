package com.immersionslabs.lcatalog;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.immersionslabs.lcatalog.Utils.CustomMessage;
import com.immersionslabs.lcatalog.Utils.NetworkConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_SIGNUP = 0;

    public static final String KEY_USERNAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_ADDRESS = "adress";
    public static final String KEY_TYPE = "type";
    public static final String KEY_VENDORID = "vendor_id";

    private static final String REGISTER_URL = "http://35.154.150.204:4000/users";

    TextView app_name, powered;
    private EditText _nameText, _addressText, _emailText, _mobileText, _passwordText, _reEnterPasswordText;
    private Button _signupButton;

    CoordinatorLayout SignupLayout;

    String resp, code, message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        powered = findViewById(R.id.immersionslabs);
        app_name = findViewById(R.id.application_name);

        SignupLayout = findViewById(R.id.SignupLayout);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");
        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        FloatingActionButton fab = findViewById(R.id.fab_signup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, UserTypeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (NetworkConnectivity.checkInternetConnection(SignupActivity.this)) {
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
                if (NetworkConnectivity.checkInternetConnection(SignupActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    private void signup() throws JSONException {
        Log.e(TAG, "Sign Up");

        Log.e(TAG, "KEY_USERNAME--" + KEY_USERNAME);
        Log.e(TAG, "KEY_PASSWORD--" + KEY_PASSWORD);
        Log.e(TAG, "KEY_EMAIL--" + KEY_EMAIL);
        Log.e(TAG, "KEY_MOBILE_NO--" + KEY_MOBILE_NO);
        Log.e(TAG, "KEY_ADDRESS--" + KEY_ADDRESS);
        Log.e(TAG, "KEY_TYPE--" + KEY_TYPE);
        Log.e(TAG, "KEY_VENDORID--" + KEY_VENDORID);


        _nameText = findViewById(R.id.input_name);
        final String name = _nameText.getText().toString().trim();
        Log.e(TAG, "name--" + name);

        _addressText = findViewById(R.id.input_address);
        final String address = _addressText.getText().toString().trim();
        Log.e(TAG, "adress--" + address);

        _emailText = findViewById(R.id.input_email);
        final String email = _emailText.getText().toString().trim();
        Log.e(TAG, "email--" + email);

        _mobileText = findViewById(R.id.input_mobile);
        final String mobile = _mobileText.getText().toString().trim();
        Log.e(TAG, "mobile_no--" + mobile);

        _passwordText = findViewById(R.id.input_password);
        final String password = _passwordText.getText().toString().trim();
        Log.e(TAG, "password--" + password);

        _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);

        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // SIGN_UP LOGIC !!
        JSONObject signup_parameters = new JSONObject();
        signup_parameters.put(KEY_USERNAME, name);
        signup_parameters.put(KEY_ADDRESS, address);
        signup_parameters.put(KEY_EMAIL, email);
        signup_parameters.put(KEY_MOBILE_NO, mobile);
        signup_parameters.put(KEY_PASSWORD, password);
        signup_parameters.put(KEY_TYPE, "CUSTOMER");
        signup_parameters.put(KEY_VENDORID, 100000); // This Value should be changed when a user is registered under specific customer

        Log.e(TAG, "adress--" + address);
        Log.e(TAG, "email--" + email);
        Log.e(TAG, "mobile--" + mobile);
        Log.e(TAG, "password--" + password);
        Log.e(TAG, "name--" + name);

        Log.e(TAG, "request--" + signup_parameters);

//
        JSONObject request = new JSONObject();
        request.put("request", signup_parameters);
//        Log.e(TAG, "Request--" + request);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, signup_parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject requestResponse) {
                Log.e(TAG, "response--" + requestResponse);
                try {
                    resp = requestResponse.getString("success");
                    code = requestResponse.getString("status_code");
                    message = requestResponse.getString("message");
                    Log.e(TAG, "response--" + resp + " code--" + code + " message--" + message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put(KEY_USERNAME, name);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_ADDRESS, address);
                params.put(KEY_MOBILE_NO, mobile);
                params.put(KEY_TYPE, "CUSTOMER");
                params.put(KEY_VENDORID, "100000");  // This Value should be changed when a user is registered under specific customer

                Log.e(TAG, "HashMap--" + String.valueOf(params));
                Log.e(TAG, "HashMap--" + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (Objects.equals(message, "FAILURE") || Objects.equals(code, "500")) {
                            onSignupFailed();
                        } else {
                            onSignupSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onSignupSuccess() {
        CustomMessage.getInstance().CustomMessage(SignupActivity.this, "SignUp Success, Welcome");

        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        CustomMessage.getInstance().CustomMessage(SignupActivity.this, "SignUp failed, Please Try Again");

        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, UserTypeActivity.class));
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        _nameText = findViewById(R.id.input_name);
        _addressText = findViewById(R.id.input_address);
        _emailText = findViewById(R.id.input_email);
        _mobileText = findViewById(R.id.input_mobile);
        _passwordText = findViewById(R.id.input_password);
        _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);

        String name = _nameText.getText().toString().trim();
        String address = _addressText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String mobile = _mobileText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String reEnterPassword = _reEnterPasswordText.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
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
