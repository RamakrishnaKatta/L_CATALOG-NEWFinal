package com.immersionslabs.lcatalog;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.SessionManager;

import java.util.HashMap;

import javax.security.auth.login.LoginException;


public class Fragment_ProjectDetails extends Fragment {
String TAG="fragment_projectdetails";
    TextView project_name, project_description, project_sub_description,project_vendor_name,project_vendor_address;
String name,desc,subdesc,vendor_id,vendor_address,vendor_image,project_pattern,project_id;
    AppCompatImageView vendor_logo,pattern_image;
SessionManager sessionManager;
    private OnFragmentInteractionListener mListener;
    private String vendor_name;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment__project_details, container, false);
        Bundle bundle=getArguments();

        sessionManager=new SessionManager(getContext());
        name=bundle.getString("projectName");
        desc=bundle.getString("projectDescription");
        subdesc=bundle.getString("projectSubDescription");
        vendor_id=bundle.getString("projectvendorid");
        project_pattern=bundle.getString("projectvendorpattern");
        project_id=bundle.getString("projectid");
        Log.e(TAG, "onCreateView: vendor_id"+vendor_id);
        int vendor_id_int=Integer.parseInt(vendor_id)+1;
       HashMap vendordet= sessionManager.GetVendorDetails(Integer.toString(vendor_id_int));
        Log.e(TAG, "onCreateView: vendordet"+vendordet);
        vendor_address= (String) vendordet.get(Integer.toString(vendor_id_int)+SessionManager.KEY_VENDOR_ADDRESS);
        vendor_image=(String)vendordet.get(Integer.toString(vendor_id_int)+SessionManager.KEY_VENDOR_LOGO);
        Log.e(TAG, "onCreateView: vendorimage"+vendor_image);
        vendor_name=(String)vendordet.get(Integer.toString(vendor_id_int)+SessionManager.KEY_VENDOR_NAME);
        project_name=view.findViewById(R.id.project_title_text);
        project_description=view.findViewById(R.id.project_description_text);
        project_sub_description=view.findViewById(R.id.project_subdescription_text);
        project_vendor_name=view.findViewById(R.id.project_vendor_text);
        project_vendor_address=view.findViewById(R.id.project_vendor_address_text);
        vendor_logo=view.findViewById(R.id.project_vendor_logo);
        pattern_image=view.findViewById(R.id.project_pattern_image);
        project_name.setText(name);
        project_description.setText(desc);
        project_sub_description.setText(subdesc);
        project_vendor_address.setText(vendor_address);
        project_vendor_name.setText(vendor_name);
        Log.e(TAG, "onCreateView: vendorurl"+EnvConstants.APP_BASE_URL + "/upload/pattern/" + project_pattern);

        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + vendor_image)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(vendor_logo);
        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/projectpatternimg/" + project_id+project_pattern)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pattern_image);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
