package com.immersionslabs.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.immersionslabs.lcatalog.Utils.CustomMessage;
import com.immersionslabs.lcatalog.Utils.NetworkConnectivity;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.Utils.UserCheckUtil;
import com.immersionslabs.lcatalog.Utils.CryptionRijndeal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Objects;

import static com.immersionslabs.lcatalog.Utils.EnvConstants.APP_BASE_URL;
import static com.immersionslabs.lcatalog.Utils.EnvConstants.GlobalUserId;

public class UserAccountActivity extends AppCompatActivity {

    private static final String TAG = "UserAccountActivity";

    private static final int REQUEST_UPDATE = 0;

    private EditText name, email, address, mobile;
    private KeyListener listener;
    private Button edit_user, update_user;
    private String user_name, user_address, user_phone, user_email, resp, message, code, user_id, user_password;

    private String LOGIN_URL = APP_BASE_URL + "/users";

    SessionManager sessionmanager;
    CryptionRijndeal rijndeal_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        sessionmanager = new SessionManager(getApplicationContext());
        HashMap hashmap = new HashMap();

        hashmap = sessionmanager.getUserDetails();

        Toolbar toolbar = findViewById(R.id.toolbar_user_account);
        setSupportActionBar(toolbar);

        TextView app_name = findViewById(R.id.application_name);
        TextView powered = findViewById(R.id.immersionslabs);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");
        app_name.setTypeface(custom_font1);
        powered.setTypeface(custom_font2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user_name = (String) hashmap.get(SessionManager.KEY_NAME);
        user_address = (String) hashmap.get(SessionManager.KEY_ADDRESS);
        user_email = (String) hashmap.get(SessionManager.KEY_EMAIL);
        user_phone = (String) hashmap.get(SessionManager.KEY_MOBILE_NO);
        user_id = (String) hashmap.get(SessionManager.KEY_USER_ID);
        user_password = (String) hashmap.get(SessionManager.KEY_PASSWORD);
        rijndeal_obj = new CryptionRijndeal();

        name = findViewById(R.id.user_input_name);
        disableEditText(name);
        name.setText(user_name);

        address = findViewById(R.id.user_input_address);
        disableEditText(address);
        address.setText(user_address);

        email = findViewById(R.id.user_input_email);
        disableEditText(email);
        email.setText(user_email);

        mobile = findViewById(R.id.user_input_mobile);
        disableEditText(mobile);
        mobile.setText(user_phone);

        edit_user = findViewById(R.id.btn_edit_account);
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                enableEditText(name);
                enableEditText(email);
                enableEditText(address);
                enableEditText(mobile);

                edit_user.setVisibility(View.GONE);
                update_user = findViewById(R.id.btn_update_account);
                update_user.setVisibility(View.VISIBLE);
                update_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update();
                    }
                });
            }
        });

        if (NetworkConnectivity.checkInternetConnection(UserAccountActivity.this)) {

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
                if (NetworkConnectivity.checkInternetConnection(UserAccountActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    public void update() {

        if (!validate()) {
            UpdateFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(UserAccountActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();

        user_name = name.getText().toString();
        user_email = email.getText().toString();
        user_phone = mobile.getText().toString();
        user_address = address.getText().toString();

        try {
            final JSONObject user_update_parameters = new JSONObject();
            user_update_parameters.put("name", user_name);
            user_update_parameters.put("email", user_email);
            user_update_parameters.put("mobile_no", user_phone);
            user_update_parameters.put("adress", user_address);

            Log.e(TAG, "Request--" + user_update_parameters);

            final JSONObject request = new JSONObject();
            request.put("request", user_update_parameters);
            Log.e(TAG, "Request--" + request);
            LOGIN_URL += "/" + GlobalUserId;
            Log.e(TAG, "Global user Id--" + GlobalUserId);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, LOGIN_URL, user_update_parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject requestResponse) {
                    Log.e(TAG, "response--" + requestResponse);

                    try {
                        resp = requestResponse.getString("success");
                        code = requestResponse.getString("status_code");
                        message = requestResponse.getString("message");
                        Log.e(TAG, "resp " + resp + " code--" + code + " message--" + message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(UserTypeActivity.this, "Internal Error", Toast.LENGTH_LONG).show();
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (Objects.equals(message, "FAILURE")) {
                            UpdateFailed();
                        } else {
                            updateSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void updateSuccess() {

        CustomMessage.getInstance().CustomMessage(UserAccountActivity.this, "Update Success");

        Toast.makeText(getBaseContext(), "Update Success", Toast.LENGTH_LONG).show();

        sessionmanager.updatedetails(user_name, user_email, user_phone, user_address);
        try {
            String enc_email_text = rijndeal_obj.encrypt(user_email);
            String enc_password_text = rijndeal_obj.encrypt(user_password);

            final String Credentials = enc_email_text + "  ###  " + enc_password_text;
            UserCheckUtil.writeToFile(Credentials, "customer");

            String text_file_data = UserCheckUtil.readFromFile("customer");
            Log.e(TAG, "User Details-- " + text_file_data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void UpdateFailed() {
        CustomMessage.getInstance().CustomMessage(UserAccountActivity.this, "Update failed");

        // Toast.makeText(getBaseContext(), "Update failed", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setCursorVisible(true);
        editText.setKeyListener(listener);
    }

    public boolean validate() {
        boolean valid = true;

        name = findViewById(R.id.user_input_name);
        address = findViewById(R.id.user_input_address);
        email = findViewById(R.id.user_input_email);
        mobile = findViewById(R.id.user_input_mobile);

        String user_name = name.getText().toString().trim();
        String user_address = address.getText().toString().trim();
        String user_email = email.getText().toString().trim();
        String user_mobile = mobile.getText().toString().trim();

        if (user_name.isEmpty() || user_name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (user_address.isEmpty()) {
            address.setError("Enter Valid Address");
            valid = false;
        } else {
            address.setError(null);
        }

        if (user_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (user_mobile.isEmpty() || user_mobile.length() != 10) {
            mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobile.setError(null);
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
