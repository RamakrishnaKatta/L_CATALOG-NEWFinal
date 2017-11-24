package com.immerrsionslabs.l_catalog_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.immerrsionslabs.l_catalog_new.Utils.ImageUtils;
import com.immerrsionslabs.l_catalog_new.Utils.NetworkConnectivity;
import com.immerrsionslabs.l_catalog_new.adapters.FullScreenImageAdapter;

public class FullScreenImageViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image_view);

        ViewPager viewPager = findViewById(R.id.pager);

        ImageUtils imageUtils = new ImageUtils(getApplicationContext());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullScreenImageViewActivity.this, imageUtils.getFilePaths());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        if (NetworkConnectivity.checkInternetConnection(FullScreenImageViewActivity.this)) {

        } else {
            InternetMessage();
        }

    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(FullScreenImageViewActivity.this)) {

                } else {

                    InternetMessage();
                    // CustomMessage.getInstance().CustomMessage(this,"Check Your Internet connection.");
                }
            }
        });
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
