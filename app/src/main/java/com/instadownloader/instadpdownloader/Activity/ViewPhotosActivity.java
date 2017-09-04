package com.instadownloader.instadpdownloader.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.instadownloader.instadpdownloader.Adapter.ImageDisplayAdapter;
import com.instadownloader.instadpdownloader.R;
import com.instadownloader.instadpdownloader.Utils.Constants;

import java.util.List;

/**
 * Created by ayush on 3/9/17.
 */

public class ViewPhotosActivity extends Activity {

    private List<String> imageUrls, imageUrlsThumbnail;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        getBundleList();

    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        ImageDisplayAdapter imageDisplayAdapter = new ImageDisplayAdapter(this, imageUrls, imageUrlsThumbnail);
        mRecyclerView.setAdapter(imageDisplayAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void getBundleList() {
        Bundle bundle = getIntent().getExtras();
        imageUrls = (bundle.getStringArrayList(Constants.BUNDLE_IMAGES_URL_LIST));
        imageUrlsThumbnail = (bundle.getStringArrayList(Constants.BUNDLE_IMAGES_URL_THUMBNAIL_LIST));
        if (imageUrls == null) {
            startActivity(new Intent(ViewPhotosActivity.this, MainActivity.class));
        } else {
            init();
        }

    }
}
