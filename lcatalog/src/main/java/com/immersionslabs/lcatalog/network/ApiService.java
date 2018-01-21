package com.immersionslabs.lcatalog.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Fragment_Overview;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApiService {
    private static ApiService instance;
    private static Context mCtx;
    private static Context context;
    private RequestQueue requestQueue;

    private static int intClearCache = 0;

    private static ProgressDialog progressDialog;

    private Map headers;


    private ApiService(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            if (intClearCache == 1) {
                requestQueue.getCache().clear();
            }
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public static synchronized ApiService getInstance(Context context) {

        if (instance == null)
            instance = new ApiService(context);
        return instance;

    }

    public static synchronized ApiService getInstance(Context context, int intClearCache) {
        ApiService.context = context;
        ApiService.intClearCache = intClearCache;
        if (instance == null)
            instance = new ApiService(context);
        return instance;

    }

    public static synchronized ApiService getInstance(Context context, int intClearCache, ProgressDialog dialog) {
        ApiService.context = context;
        ApiService.intClearCache = intClearCache;
        ApiService.progressDialog = dialog;
        if (instance == null)
            instance = new ApiService(context);
        return instance;

    }


    public void getData(final ApiCommunication listener, boolean iscached, final String SCREEN_NAME, final String url, final String flag) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponseCallback(response, flag);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    try {
                        Toast.makeText(mCtx, "Internal Error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listener.onErrorCallback(error, flag);
                getRequestQueue().getCache().remove(url);
            }
        })
        ;
        request.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(request);
        Log.e(SCREEN_NAME + "URLHIT", request.getUrl() + " ");

    }

    public void postData(final ApiCommunication listener, final String url, JSONObject params, final String SCREEN_NAME, final String flag) {
        final String csrf_token, sessionId;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.e(SCREEN_NAME, response + "");
                        listener.onResponseCallback(response, flag);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            ////Log.e(SCREEN_NAME + "-" + flag, error.networkResponse.data.toString() + "");
                            Toast.makeText(mCtx, "internalerror", Toast.LENGTH_SHORT).show();
                        }
                        // TODO Auto-generated method stub
                        listener.onErrorCallback(error, flag);
                        getRequestQueue().getCache().remove(url);
                    }
                });
        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME + "URL  HIT", jsObjRequest.getUrl() + " ");
    }


    public void putData(final ApiCommunication listener, final String url, JSONObject params, final String SCREEN_NAME, final String flag) {
        final String sessionId, csrf_token;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.e(SCREEN_NAME, response + "");
                        listener.onResponseCallback(response, flag);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            ////Log.e(SCREEN_NAME + "-" + flag, error.networkResponse.data.toString() + "");
                            Toast.makeText(mCtx, "Out Dataa", Toast.LENGTH_SHORT).show();
                        }
                        // TODO Auto-generated method stub
                        listener.onErrorCallback(error, flag);
                        getRequestQueue().getCache().remove(url);
                    }
                }) ;
        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME, jsObjRequest.getUrl() + " ");
    }


    public void deleteData(final ApiCommunication listener, boolean isCached, final String SCREEN_NAME, final String url, final String flag) {


        ////System.out.println("sessionid___" + sessionId);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ////System.out.println("___" + response + "___");
                        listener.onResponseCallback(response, flag);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        if (error.networkResponse != null) {
                            ////Log.e(SCREEN_NAME + "-" + flag, error.networkResponse.data.toString() + "");
                            ////System.out.println("apiService error :" + error.networkResponse.data.toString() + "__" + flag);
                            Toast.makeText(mCtx, "Delete Data", Toast.LENGTH_SHORT).show();
                        }
                        listener.onErrorCallback(error, flag);
                        getRequestQueue().getCache().remove(url);
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME, jsObjRequest.getUrl() + " ");
    }

   }
