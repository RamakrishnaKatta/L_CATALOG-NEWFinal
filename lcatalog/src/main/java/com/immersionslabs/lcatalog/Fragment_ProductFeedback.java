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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Fragment_ProductFeedback extends Fragment implements View.OnClickListener, ApiCommunication {

    private static final String TAG = "ProductFeedback";

    EditText feedback_article_id, feedback_vendor_id, feedback_message, feedback_article_name;
    TextInputLayout feedback_article_id_area, feedback_vendor_id_area, feedback_message_area, feedback_article_name_area;
    Button feedback_submit, ratings_submit;
    RatingBar ratingBar;
    String f_article_id, f_vendor_id, f_message, f_article_name, user_id;
    RelativeLayout feedback_layout, ratings_layout;
    ImageView feedback_imageview, ratings_imageview;
    SessionManager sessionManager;
    String resp, code, message;
    Float rating;

    public static final String RATING_URL = EnvConstants.APP_BASE_URL + "articleRating";
    public static final String ARTICLE_FEEDBACK_URL = "http://ladmin.immersionslabs.com/articleFeedback";
    public static final String GET_RATING_URL = EnvConstants.APP_BASE_URL + "getUserRating";
    private String global_user_id;
    private boolean _rating;

    private String f_vendor_id_mongo;

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

        ratingBar = view.findViewById(R.id.ratingsbar);
        ratings_layout = view.findViewById(R.id.ratings_relative_layout);
        ratings_submit = view.findViewById(R.id.ratings_submit);
        ratings_imageview = view.findViewById(R.id.ratings_imageview);

        sessionManager = new SessionManager(getContext());

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
        feedback_vendor_id.setText(f_vendor_id_mongo);

        try {
            f_vendor_id_mongo = getvendorid();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "--" + f_vendor_id_mongo);

        f_article_name = getArguments().getString("article_title");
        Log.e(TAG, "--" + f_article_name);
        feedback_article_name.setText(f_article_name);

        HashMap userdetails = new HashMap();
        userdetails = sessionManager.getUserDetails();
        user_id = String.valueOf(userdetails.get(SessionManager.KEY_USER_ID));
        global_user_id = String.valueOf(userdetails.get(SessionManager.KEY_GLOBAL_USER_ID));


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
                if (ratings_layout.getTag().toString().equals("down")) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratings_submit.setVisibility(View.VISIBLE);
                    ratings_imageview.setImageResource(R.mipmap.ic_faq_up);
                    ratings_layout.setTag("up");

                } else {
                    ratings_imageview.setImageResource(R.mipmap.ic_faq_down);
                    ratingBar.setVisibility(View.GONE);
                    ratings_submit.setVisibility(View.GONE);
                    ratings_layout.setTag("down");
                }
            }
        });
        ratings_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ratingapicall();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        try {
            getratingapicall();
            if(_rating)
            {
                ratingBar.setRating(rating);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getvendorid() throws JSONException
    {
        String UNIQUE_VENDORID_CALL="http://ladmin.immersionslabs.com//vendors/specific/"+f_vendor_id;
        ApiService.getInstance(getContext()).getData(this,false,"get_mongo_vendorid",UNIQUE_VENDORID_CALL,"GET_MONGO_VENDORID");
        return f_vendor_id_mongo;

    }
    public void getratingapicall() throws JSONException
    {
        String UNIQUE_GETRATING_CALL=GET_RATING_URL+"?article_id="+f_article_id+"&user_id="+user_id;
       // ApiService.getInstance(getContext()).getData(this,false,"get_rating",UNIQUE_GETRATING_CALL,"GET_ARTICLE_RATING");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, UNIQUE_GETRATING_CALL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    message = response.getString("message");
                    if (message.equals("Your rating")) {
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            rating = (float) object.get("rate");
                        }
                        _rating = true;
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

    }


    private void ratingapicall() throws JSONException {
        float rating = ratingBar.getRating();
        JSONObject rating_parameters = new JSONObject();
        rating_parameters.put("article_id", f_article_id);
        rating_parameters.put("user_id", user_id);
        rating_parameters.put("rating", rating);
        ApiService.getInstance(getContext()).postData(this, RATING_URL, rating_parameters, "RATING", "ARTICLE_RATING");

    }

    private void feedbackapicall() throws JSONException {
        String message = feedback_message.getText().toString();
        JSONObject feedback_parameters = new JSONObject();
        feedback_parameters.put("article_id", f_article_id);
        feedback_parameters.put("user_id", global_user_id);
        feedback_parameters.put("vendor_id", f_vendor_id_mongo);
        feedback_parameters.put("feedbacks", message);
        Log.e(TAG, "feedbackapicall: Request" + feedback_parameters);

        ApiService.getInstance(getContext()).postData(this, ARTICLE_FEEDBACK_URL, feedback_parameters, "FEEDBACK_ARTICLE", "ARTICLE_FEEDBACK");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.feedback_submit) {
            try {
                feedbackapicall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(getActivity(), "Your submit form is accepted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag)  {
        if (flag.equals("ARTICLE_RATING")) {
            try {

                message = response.getString("message");
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Response--" + resp + " Status Code--" + code + " Message--" + message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (flag.equals("ARTICLE_FEEDBACK")) {
            try {
                code=response.getString("status_code");
                message = response.getString("message");
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Response--" + resp + " Status Code--" + code + " Message--" + message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(flag.equals("GET_MONGO_VENDORID"))
        {try{
            JSONArray jsonArray=response.getJSONArray("data");
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            f_vendor_id_mongo=jsonObject.getString("_id");
        }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        }



    @Override
    public void onErrorCallback(VolleyError error, String flag) {

    }


}
