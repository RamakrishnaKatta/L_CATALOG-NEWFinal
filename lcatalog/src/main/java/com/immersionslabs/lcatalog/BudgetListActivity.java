package com.immersionslabs.lcatalog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.immersionslabs.lcatalog.Utils.BudgetManager;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Set;

public class BudgetListActivity extends AppCompatActivity {

    EditText Total_budget, Current_value, Remaining_value;
    Button Alter_Budget;
    SessionManager sessionManager;
    private KeyListener listener;
    HashMap<String, Integer> getdetails;
    BudgetManager budgetManager;

    Integer current_value, total_budget_value, remaining_value;
    TextView id_display;

    String str_current_value, str_total_budget_value, str_remaining_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);
budgetManager=new BudgetManager();
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
        if(EnvConstants.user_type.equals("CUSTOMER"))
        {
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

        }
        else
        {
            String Guest_Total_budget,Guest_Current_value,Guest_Remaining_budget;
            Guest_Total_budget=Integer.toString(budgetManager.getTotal_Budget());
            Guest_Current_value=Integer.toString(budgetManager.getCurrent_Value());
           Guest_Remaining_budget=Integer.toString(budgetManager.getRemaining_Budget());
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
