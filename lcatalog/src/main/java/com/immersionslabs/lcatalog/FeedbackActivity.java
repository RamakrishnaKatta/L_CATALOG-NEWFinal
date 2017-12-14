package com.immersionslabs.lcatalog;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {
    EditText feed_name, feed_subject, feed_number;
    Button feed_Submit;

    TextView app_name, powered;

    public static final String TAG = "FeedbackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar_feedback);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        app_name = findViewById(R.id.application_name);
        powered = findViewById(R.id.immersionslabs);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

        feed_Submit = findViewById(R.id.btn_feed_submit);

//        feed_Submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this, R.style.AppTheme_Dark_Dialog);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Authenticating...");
//                progressDialog.show();
//                new android.os.Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//            }
//        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
