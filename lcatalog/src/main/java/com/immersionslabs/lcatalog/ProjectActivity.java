package com.immersionslabs.lcatalog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.CampaignAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "ProjectActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getprojects";

    RecyclerView recyclerView;
    LinearLayoutManager Campaign_Manager;
    CampaignAdapter adapter;
    GridLayoutManager ProjectpartManager;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_name;
    private ArrayList<String> project_description;
    private ArrayList<String> project_subDescription;
    private ArrayList<String> project_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        recyclerView = findViewById(R.id.project_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_project);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        project_ids = new ArrayList<>();
        project_name = new ArrayList<>();
        project_description = new ArrayList<>();
        project_subDescription = new ArrayList<>();
        project_images = new ArrayList<>();

        CommonGetData();
    }

    private void CommonGetData() {
        Log.e(TAG, "CommonGetData: Project URL" + REGISTER_URL);
        final JSONObject object = new JSONObject();
        ApiService.getInstance(this).getData(this, false, "CAMPAIGN", REGISTER_URL, "PROJECT_CAMPAIGN");
    }

    private void GetData(JSONArray resp) {
        for (int i = 0; i < resp.length(); i++) {
            JSONObject object = null;

            try {
                object = resp.getJSONObject(i);
                project_ids.add(object.getString("_id"));
                project_name.add(object.getString("projectName"));
                project_description.add(object.getString("projectDescription"));
                project_images.add(object.getString("images"));
                project_subDescription.add(object.getString("projectSubDescription"));

//                for (int j = 0; j < object.length(); j++) {
//                    JSONArray parts = object.getJSONArray("parts");
//                    Log.e(TAG, " parts" + parts);
//
//                    JSONObject object1 = parts.getJSONObject(j);
//
//                    project_part.add(object1.getString("part"));
//                    project_partName.add(object1.getString("partName"));
//                    project_partDesc.add(object1.getString("partDesc"));
//                    project_partimages.add(object1.getString("partimages"));
//                    project_part_articlesIds.add(object1.getString("articlesId"));
//                    project_part_articlesData.add(object1.getString("articlesData"));
//
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, " ids" + project_ids);
        Log.e(TAG, " Name" + project_name);
        Log.e(TAG, " Description" + project_description);
        Log.e(TAG, " Sub Description" + project_subDescription);
        Log.e(TAG, "Images" + project_images);

        Campaign_Manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(Campaign_Manager);
        CampaignAdapter adapter = new CampaignAdapter(this, project_ids,
                project_name, project_description,
                project_subDescription, project_images);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("PROJECT_CAMPAIGN")) {
            Log.e(TAG, " response" + response);
            try {
                JSONArray resp = response.getJSONArray("data");
                GetData(resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(ProjectActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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
