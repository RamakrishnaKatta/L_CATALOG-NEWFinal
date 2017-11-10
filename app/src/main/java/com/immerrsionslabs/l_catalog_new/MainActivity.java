package com.immerrsionslabs.l_catalog_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<SearchResults> searchResults = GetSearchResults();

        final ListView lv = findViewById(R.id.ListView);
        lv.setAdapter(new MyCustomBaseAdapter(this, searchResults));


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                SearchResults fullObject = (SearchResults) o;


                Intent intent = new Intent(MainActivity.this, JsonView.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<SearchResults> GetSearchResults() {
        ArrayList<SearchResults> results = new ArrayList<SearchResults>();

        SearchResults sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/users");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/users");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/users/+ User ID(100001)");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/users/+%20_ID(5a02cd7623a694184096787f)");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/login");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/customerLogin");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/vendor");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/vendors");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/venodrs/+_ID (5a00005e2fd3041c38a7b9ae)");
        results.add(sr);

        sr = new SearchResults();
        sr.setName("http://192.168.0.13:4000/venodrs/+_ID%20(5a00005e2fd3041c38a7b9ae)");
        results.add(sr);

        return results;
    }
}
