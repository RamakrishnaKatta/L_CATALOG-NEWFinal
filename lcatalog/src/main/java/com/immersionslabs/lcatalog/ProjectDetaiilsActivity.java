package com.immersionslabs.lcatalog;

import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.immersionslabs.lcatalog.adapters.ProjectPageAdapter;

import java.util.ArrayList;

public class ProjectDetaiilsActivity extends AppCompatActivity {

    private static final String TAG = "ProjectDetaiilsActivity";

    String images;

    String position, name, id, description, sub_desc,vendor_id;

    String project_3ds, project_pattern;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;
    private ArrayList<String> project_part_3ds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar_project);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        project_ids = new ArrayList<>();
        project_part = new ArrayList<>();
        project_partName = new ArrayList<>();
        project_partDesc = new ArrayList<>();
        project_partimages = new ArrayList<>();
        project_part_articlesIds = new ArrayList<>();
        project_part_articlesData = new ArrayList<>();
        project_part_3ds = new ArrayList<>();

        final Bundle b = getIntent().getExtras();
        assert b != null;
        name = (String) b.getCharSequence("projectName");
        description = (String) b.getCharSequence("projectDescription");
        sub_desc = (String) b.getCharSequence("projectSubDescription");

        position = (String) b.getCharSequence("project_position");
        id = (String) b.getCharSequence("_id");
        project_3ds = (String) b.getCharSequence("projectView_3d");
        project_pattern =(String)b.getCharSequence("patternImg");
         vendor_id=b.getString("vendorid");
        images = (String) b.getCharSequence("images");


        Log.e(TAG, "Project Name----" + name);
        Log.e(TAG, "Project Description----" + description);
        Log.e(TAG, "Project position----" + position);
        Log.e(TAG, "Project Id----" + id);
        Log.e(TAG, "Project Pattern----" + project_pattern);
        Log.e(TAG, "Project Images----" + images);
        Log.e(TAG, "vendor id----" + vendor_id);


        TabLayout tabLayout = findViewById(R.id.project_detaiils_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("DESIGN"));
        tabLayout.addTab(tabLayout.newTab().setText("DETAILS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.project_detaiils_pager);
        final ProjectPageAdapter adapter = new ProjectPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), name, id, description,sub_desc,images,project_3ds,project_pattern,vendor_id);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

}