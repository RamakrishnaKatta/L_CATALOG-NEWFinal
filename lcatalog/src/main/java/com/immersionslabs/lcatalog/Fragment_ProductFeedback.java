package com.immersionslabs.lcatalog;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.network.ApiCommunication;

import org.json.JSONObject;

public class Fragment_ProductFeedback extends Fragment implements View.OnClickListener, ApiCommunication {

    private static final String TAG = "ProductFeedback";

    EditText feedback_article_id, feedback_vendor_id, feedback_message, feedback_article_name;
    TextInputLayout feedback_article_id_area, feedback_vendor_id_area, feedback_message_area, feedback_article_name_area;
    Button feedback_submit,ratings_submit;
    RatingBar ratingBar;
    String f_article_id, f_vendor_id, f_message, f_article_name;
    RelativeLayout feedback_layout,ratings_layout;
    ImageView feedback_imageview,ratings_imageview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_feedback, container, false);

        feedback_article_id = view.findViewById(R.id.feedback_article_id);
        feedback_vendor_id = view.findViewById(R.id.feedback_vendor_id);
        feedback_message = view.findViewById(R.id.feedback_article_message);
        feedback_article_name = view.findViewById(R.id.feedback_article_name);
        feedback_article_id_area = view.findViewById(R.id.feedback_article_id_area);
        feedback_vendor_id_area = view.findViewById(R.id.feedback_vendor_id_area);
        feedback_message_area = view.findViewById(R.id.feedback_article_message_area);
        feedback_article_name_area = view.findViewById(R.id.feedback_article_name_area);
        feedback_layout = view.findViewById(R.id.feedback_relative_layout);
        feedback_imageview = view.findViewById(R.id.feedback_imageview);
        feedback_submit = view.findViewById(R.id.feedback_submit);

        ratingBar=view.findViewById(R.id.ratingsbar);
        ratings_layout=view.findViewById(R.id.ratings_relative_layout);
        ratings_submit=view.findViewById(R.id.ratings_submit);
        ratings_imageview=view.findViewById(R.id.ratings_imageview);

        feedback_article_id_area.setVisibility(View.GONE);
        feedback_vendor_id_area.setVisibility(View.GONE);
        feedback_message_area.setVisibility(View.GONE);
        feedback_article_name_area.setVisibility(View.GONE);
        feedback_submit.setVisibility(View.GONE);


        ratingBar.setVisibility(View.GONE);
        ratings_submit.setVisibility(View.GONE);


        feedback_layout.setTag("down");
        ratings_layout.setTag("down");

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

        feedback_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedback_layout.getTag().toString().equals("down")) {
                    feedback_article_id_area.setVisibility(View.VISIBLE);
                    feedback_vendor_id_area.setVisibility(View.VISIBLE);
                    feedback_message_area.setVisibility(View.VISIBLE);
                    feedback_article_name_area.setVisibility(View.VISIBLE);
                    feedback_submit.setVisibility(View.VISIBLE);
                    feedback_imageview.setImageResource(R.mipmap.ic_faq_up);
                    feedback_layout.setTag("up");
                } else {
                    feedback_imageview.setImageResource(R.mipmap.ic_faq_down);
                    feedback_article_id_area.setVisibility(View.GONE);
                    feedback_vendor_id_area.setVisibility(View.GONE);
                    feedback_message_area.setVisibility(View.GONE);
                    feedback_article_name_area.setVisibility(View.GONE);
                    feedback_submit.setVisibility(View.GONE);
                    feedback_layout.setTag("down");
                }

            }
        });
        ratings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratings_layout.getTag().toString().equals("down"))
                {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratings_submit.setVisibility(View.VISIBLE);
                    ratings_imageview.setImageResource(R.mipmap.ic_faq_up);
                    ratings_layout.setTag("up");

                }
                else {
                    ratings_imageview.setImageResource(R.mipmap.ic_faq_down);
                    ratingBar.setVisibility(View.GONE);
                    ratings_submit.setVisibility(View.GONE);
                    ratings_layout.setTag("down");
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.feedback_submit)
        {
            Toast.makeText(getActivity(), "Your submit form is accepted", Toast.LENGTH_LONG).show();
        }
        else if(v.getId()==R.id.ratings_submit)
        {
            Toast.makeText(getActivity(), "Your submit form is accepted", Toast.LENGTH_LONG).show();
        }

   //     switch (v.getId()) {
//            case R.id.feedback_submit:
//
//                Toast.makeText(getActivity(), "Your submit form is accepted", Toast.LENGTH_LONG).show();
//
//
//            case R.id.ratings_submit:
//
//                Toast.makeText(getActivity(), "Your Rating is submitted.Thank you. ", Toast.LENGTH_LONG).show();
//
//        }

    }


    @Override
    public void onResponseCallback(JSONObject response, String flag) {

    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {

    }


}
