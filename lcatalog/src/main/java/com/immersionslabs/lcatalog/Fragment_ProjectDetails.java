package com.immersionslabs.lcatalog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.adapters.ProjectPartAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_ProjectDetails extends Fragment implements ApiCommunication {
    private static final String TAG = "fragment_projectdetails";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getProjectDetails/";

    private static String PROJECT_PART_URL = null;

    TextView project_name, project_description, project_sub_description, project_vendor_name, project_vendor_address;

    String name, desc, subdesc, vendor_id, vendor_address, vendor_image, project_pattern, project_id;

    AppCompatImageView vendor_logo, pattern_image;

    SessionManager sessionManager;

    private String vendor_name;

    RecyclerView project_part_recycler;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;
    private ArrayList<String> project_part_3ds;

    private GridLayoutManager Project_partManager;

    ProjectPartAdapter adapter;

    public Fragment_ProjectDetails() {
        // Required empty public constructor
    }

    public static Fragment_ProjectDetails newInstance(String param1, String param2) {
        Fragment_ProjectDetails fragment = new Fragment_ProjectDetails();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_project_details, container, false);
        Bundle bundle = getArguments();

        project_ids = new ArrayList<>();
        project_part = new ArrayList<>();
        project_partName = new ArrayList<>();
        project_partDesc = new ArrayList<>();
        project_partimages = new ArrayList<>();
        project_part_articlesIds = new ArrayList<>();
        project_part_articlesData = new ArrayList<>();
        project_part_3ds = new ArrayList<>();

        sessionManager = new SessionManager(getContext());

        assert bundle != null;
        name = bundle.getString("projectName");
        desc = bundle.getString("projectDescription");
        subdesc = bundle.getString("projectSubDescription");
        vendor_id = bundle.getString("projectvendorid");
        project_pattern = bundle.getString("projectvendorpattern");
        project_id = bundle.getString("projectid");


        Log.e(TAG, "project vendor_id" + vendor_id);
        int vendor_id_int = Integer.parseInt(vendor_id) + 1;
        HashMap vendordetails = sessionManager.GetVendorDetails(Integer.toString(vendor_id_int));

        Log.e(TAG, " vendordetails" + vendordetails);
        vendor_address = (String) vendordetails.get(Integer.toString(vendor_id_int) + SessionManager.KEY_VENDOR_ADDRESS);
        vendor_image = (String) vendordetails.get(Integer.toString(vendor_id_int) + SessionManager.KEY_VENDOR_LOGO);
        Log.e(TAG, "vendorimage " + vendor_image);
        vendor_name = (String) vendordetails.get(Integer.toString(vendor_id_int) + SessionManager.KEY_VENDOR_NAME);

        project_name = view.findViewById(R.id.project_title_text);
        project_description = view.findViewById(R.id.project_description_text);
        project_sub_description = view.findViewById(R.id.project_subdescription_text);
        project_vendor_name = view.findViewById(R.id.project_vendor_text);
        project_vendor_address = view.findViewById(R.id.project_vendor_address_text);
        vendor_logo = view.findViewById(R.id.project_vendor_logo);
        pattern_image = view.findViewById(R.id.project_pattern_image);

        project_part_recycler = view.findViewById(R.id.project_part_list_recycler);
        project_part_recycler.setHasFixedSize(true);
        project_part_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        project_name.setText(name);
        project_description.setText(desc);
        project_sub_description.setText(subdesc);
        project_vendor_address.setText(vendor_address);
        project_vendor_name.setText(vendor_name);
        Log.e(TAG, " Pattern URL" + project_pattern);

        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + vendor_image)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(vendor_logo);
        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/projectpatternimg/" + project_id + project_pattern)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pattern_image);

        PROJECT_PART_URL = REGISTER_URL + project_id;
        Log.e(TAG, "PROJECT_PART_URL------" + PROJECT_PART_URL);

        try {
            getProjectData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void getProjectData() throws JSONException {
        ApiService.getInstance(getContext()).getData(this, false, "project_part_data", PROJECT_PART_URL, "project_part_det");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("project_part_det")) {
            try {
                JSONObject resp = response.getJSONObject("data");
                project_ids.add(resp.getString("_id"));

                Log.e(TAG, "responseproject: " + response);
                JSONArray parts = resp.getJSONArray("parts");
                Log.e(TAG, "partsjson: " + parts);
                getdata(parts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getdata(JSONArray parts) {
        for (int i = 0; i < parts.length(); i++) {
            JSONObject object = null;

            try {
                object = parts.getJSONObject(i);
                project_part.add(object.getString("part"));
                project_partName.add(object.getString("partName"));
                project_partDesc.add(object.getString("partDesc"));
                project_partimages.add(object.getString("partimages"));
                project_part_articlesIds.add(object.getString("articlesId"));
                project_part_articlesData.add(object.getString("articlesData"));
                project_part_3ds.add(object.getString("partview_3d"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "project_id" + project_ids);
        Log.e(TAG, "part" + project_part);
        Log.e(TAG, "partName" + project_partName);
        Log.e(TAG, "partDesc" + project_partDesc);
        Log.e(TAG, "partimages" + project_partimages);
        Log.e(TAG, "articlesId" + project_part_articlesIds);
        Log.e(TAG, "articlesData" + project_part_articlesData);
        Log.e(TAG, "part3ds" + project_part_3ds);

        Project_partManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        project_part_recycler.setLayoutManager(Project_partManager);
        adapter = new ProjectPartAdapter(getActivity(), project_part, project_partName, project_partDesc, project_partimages, project_part_articlesIds, project_part_articlesData, project_ids, project_part_3ds);
        project_part_recycler.setAdapter(adapter);
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
    }
}
