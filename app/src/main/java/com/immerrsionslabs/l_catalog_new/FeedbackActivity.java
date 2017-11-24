package com.immerrsionslabs.l_catalog_new;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {
    TextView app_name, powered;
    EditText feed_name, feed_subject;
    Button Submit;
    public static final String TAG = "FeedbackActivity";
    ConstraintLayout FeedbackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        powered = findViewById(R.id.lucidleanlabs);
        app_name = findViewById(R.id.application_name);
        FeedbackLayout = findViewById(R.id.Feedbacklayout);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");
        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        FloatingActionButton fab = findViewById(R.id.fab_feed_back);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
