package com.immersionslabs.lcatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.ProjectImageSliderAdapter;
import com.immersionslabs.lcatalog.adapters.ProjectPartAdapter;
import com.immersionslabs.lcatalog.augment.ARNativeActivity;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectDetailActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "ProjectDetailActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getProjectDetails/";
    private static String PROJECT_PART_URL = null;

    private ViewPager viewpager;
    private LinearLayout slider_dots;
    ProjectImageSliderAdapter imageSliderAdapter;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    int page_position = 0;
    TextView project_name, project_description, project_sub_description;
    AppCompatImageView project_image;
    String image1, image2, image3, image4, image5;

    AppCompatImageButton project_augment, project_3dview;
    String project_id, project_images;
    RecyclerView recyclerView;
    ProjectPartAdapter adapter;
    GridLayoutManager ProjectpartManager;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;
    private ArrayList<String> project_part_3ds;

    String p_name, project_3ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar_projects);
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

        project_name = findViewById(R.id.project_title_text);
        project_description = findViewById(R.id.project_description_text);
        project_sub_description = findViewById(R.id.project_subdescription_text);
        project_image = findViewById(R.id.project_image_view);
        project_3dview = findViewById(R.id.project_3dview_icon);
        project_augment = findViewById(R.id.project_augment_icon);

        final Bundle b = getIntent().getExtras();
        assert b != null;
        p_name = (String) b.getCharSequence("projectName");
        Log.e(TAG, "project_name ---- " + p_name);
        project_name.setText(p_name);

        project_3ds = (String) b.getCharSequence("projectView_3d");
        project_id = (String) b.getCharSequence("_id");
        project_name.setText(b.getCharSequence("projectName"));
        project_description.setText(b.getCharSequence("projectDescription"));
        project_sub_description.setText(b.getCharSequence("projectSubDescription"));

        Log.e(TAG, "project_3ds  " + project_3ds);
        Log.e(TAG, "project_id ---- " + project_id);
        Log.e(TAG, "Project_name  " + project_name);
        Log.e(TAG, "project_description  " + project_description);
        Log.e(TAG, "project_sub_description  " + project_sub_description);

        recyclerView = findViewById(R.id.project_part_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        project_images = (String) b.getCharSequence("images");
        Log.e(TAG, "Project Images----" + project_images);

        try {
            JSONArray image_json = new JSONArray(project_images);
            for (int i = 0; i < image_json.length(); i++) {
                image1 = image_json.getString(0);
                image2 = image_json.getString(1);
                image3 = image_json.getString(2);
                image4 = image_json.getString(3);
                image5 = image_json.getString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "ProjectImage 1----" + image1);
        Log.e(TAG, "ProjectImage 2----" + image2);
        Log.e(TAG, "ProjectImage 3----" + image3);
        Log.e(TAG, "ProjectImage 4----" + image4);
        Log.e(TAG, "ProjectImage 5----" + image5);

        final String[] Images = {image1, image2, image3, image4, image5};

        Collections.addAll(slider_images, Images);

        viewpager = findViewById(R.id.project_view_pager);
        imageSliderAdapter = new ProjectImageSliderAdapter(ProjectDetailActivity.this, slider_images, project_id);
        viewpager.setAdapter(imageSliderAdapter);

        slider_dots = findViewById(R.id.project_slide_dots);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        project_augment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle("You are about to enter Augment Enabled Camera");
                builder.setMessage("This requires 2min of your patience, Do you wish to enter ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ProjectDetailActivity.this, ARNativeActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();
            }
        });

        project_3dview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b4 = new Bundle();
                b4.putString("projectName", project_id);
                b4.putString("project_3ds_file_name", project_3ds);
                Intent _3d_intent = new Intent(ProjectDetailActivity.this, Article3dViewActivity.class).putExtras(b4);
                startActivity(_3d_intent);
            }
        });

        PROJECT_PART_URL = REGISTER_URL + project_id;
        Log.e(TAG, "PROJECT_PART_URL------" + PROJECT_PART_URL);

        try {
            getProjectData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

        slider_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);
            slider_dots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#004D40"));
    }

    final Runnable update = new Runnable() {
        @Override
        public void run() {
            if (page_position == slider_images.size()) {
                page_position = 0;
            } else {
                page_position = page_position + 1;
            }
            viewpager.setCurrentItem(page_position, true);
        }
    };

    private void getProjectData() throws JSONException {
        ApiService.getInstance(this).getData(this, false, "PROJECT_PART_DATA", PROJECT_PART_URL, "PROJECT_PART");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("PROJECT_PART")) {
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

        ProjectpartManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(ProjectpartManager);
        adapter = new ProjectPartAdapter(this, project_part, project_partName, project_partDesc, project_partimages, project_part_articlesIds, project_part_articlesData, project_ids, project_part_3ds);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();

        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra("activity", "SplashScreen");
        startActivity(intent);
        finish();
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