package com.immersionslabs.lcatalog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.immersionslabs.lcatalog.Utils.BudgetManager;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;

import static com.immersionslabs.lcatalog.R.*;

public class BudgetBarActivity extends AppCompatActivity {

    EditText Total_budget_val;
    Button edit_button, submit_button;
    private KeyListener listener;
    String budget_value;

    SessionManager sessionManager;
    BudgetManager budgetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_budget_bar);

        Toolbar toolbar = findViewById(id.toolbar_budget_bar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sessionManager = new SessionManager(getApplicationContext());
        budgetManager = new BudgetManager();

        Total_budget_val = findViewById(id.input_budget);
        disableEditText(Total_budget_val);
        String text_total;
        if (EnvConstants.user_type.equals("CUSTOMER")) {
            text_total = Integer.toString(sessionManager.GET_TOTAL_VALUE());
        } else {
            text_total = Integer.toString(budgetManager.getTotal_Budget());
        }

        Total_budget_val.setText(text_total);
        submit_button = findViewById(id.btn_budget_submit);
        edit_button = findViewById(id.btn_budget_edit);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_button.setVisibility(View.GONE);
                submit_button.setVisibility(View.VISIBLE);
                enableEditText(Total_budget_val);
                Total_budget_val.setTextColor(ContextCompat.getColor(getApplicationContext(), color.red));
                Toast.makeText(getApplicationContext(), "ALTER THE BUDGET", Toast.LENGTH_LONG).show();
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    budget_value = Total_budget_val.getText().toString();
                    sessionManager.SET_TOTAL_VALUE(Integer.parseInt(budget_value));
                    Toast.makeText(getApplicationContext(), "BUDGET CHANGED SUCCESSFULLY", Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(getApplicationContext(),BudgetListActivity.class);
//                    startActivity(intent);

                } else {
                    budget_value = Total_budget_val.getText().toString();
                    budgetManager.setTotal_Budget(Integer.parseInt(budget_value));
                    Toast.makeText(getApplicationContext(), "BUDGET CHANGED SUCCESSFULLY", Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(getApplicationContext(),BudgetListActivity.class);
//                    startActivity(intent);

                }
            }
        });
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
