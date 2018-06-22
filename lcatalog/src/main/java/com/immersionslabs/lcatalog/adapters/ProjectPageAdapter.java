package com.immersionslabs.lcatalog.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.immersionslabs.lcatalog.Fragment_ProjectDesign;
import com.immersionslabs.lcatalog.Fragment_ProjectDetails;

public class ProjectPageAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ProjectPageAdapter";

     private String p_name,p_id,p_desc,p_sub_desc,p_images,p_3ds,p_pattern,p_vendorid;


    private int mNumOfTabs;


    public ProjectPageAdapter(FragmentManager supportFragmentManager,
                              int tabCount,
                              String name,
                              String id,
                              String description,
                              String sub_desc,
                              String images,
                              String project_3ds,
                              String project_pattern,String project_vendor_id) {
        super(supportFragmentManager);

        this.mNumOfTabs = tabCount;
        this.p_name=name;
        this.p_id =id;
        this.p_desc =description;
        this.p_sub_desc =sub_desc;
        this.p_images =images;
        this.p_3ds =project_3ds;
        this.p_pattern=project_pattern;
        this.p_vendorid=project_vendor_id;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle b_tab1 = new Bundle();
                b_tab1.putString("images",p_images);
                b_tab1.putString("_id",p_id);
                b_tab1.putString("projectView_3d",p_3ds);

                Fragment_ProjectDesign tab1 = new Fragment_ProjectDesign();
                tab1.setArguments(b_tab1);
                return tab1;

            case 1:
                Bundle b_tab2 = new Bundle();
                b_tab2.putString("projectName",p_name);
                b_tab2.putString("projectDescription",p_desc);
                b_tab2.putString("projectSubDescription",p_sub_desc);
                b_tab2.putString("projectvendorid",p_vendorid);
                Fragment_ProjectDetails tab2 = new Fragment_ProjectDetails();
                tab2.setArguments(b_tab2);
                return tab2;
            default:
                return null;
        }
    }
}
