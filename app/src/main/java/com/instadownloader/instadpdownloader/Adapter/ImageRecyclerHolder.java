package com.instadownloader.instadpdownloader.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.instadownloader.instadpdownloader.R;

/**
 * Created by ayush on 3/9/17.
 */

public class ImageRecyclerHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public RelativeLayout relativeLayout;


    public ImageRecyclerHolder(View view) {
        super(view);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
    }
}
