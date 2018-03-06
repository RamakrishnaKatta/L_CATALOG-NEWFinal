package com.immersionslabs.lcatalog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.network.ApiCommunication;

import org.json.JSONObject;

public class Fragment_ProductFeedback extends Fragment implements View.OnClickListener, ApiCommunication {

    private static final String TAG = "ProductFeedback";

    EditText feedback_article_id, feedback_vendor_id, feedback_message, feedback_article_name;
    Button feedback_submit;

    String f_article_id, f_vendor_id, f_message, f_article_name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_feedback, container, false);

        feedback_article_id = view.findViewById(R.id.feedback_article_id);
        feedback_vendor_id = view.findViewById(R.id.feedback_vendor_id);
        feedback_message = view.findViewById(R.id.feedback_article_message);
        feedback_article_name = view.findViewById(R.id.feedback_article_name);

        feedback_submit = view.findViewById(R.id.feedback_submit);
        feedback_submit.setOnClickListener(this);

        f_article_id = getArguments().getString("article_id");
        Log.e(TAG, "--" + f_article_id);
        feedback_article_id.setText(f_article_id);

        f_vendor_id = getArguments().getString("article_vendor_id");
        Log.e(TAG, "--" + f_vendor_id);
        feedback_vendor_id.setText(f_vendor_id);

        f_article_name = getArguments().getString("article_title");
        Log.e(TAG, "--" + f_article_name);
        feedback_article_name.setText(f_article_name);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.feedback_submit:

                Toast.makeText(getActivity(), "You submit form is accepted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {

    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {

    }
}
