package com.instadownloader.instadpdownloader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instadownloader.instadpdownloader.Activity.ImageDisplayActivity;
import com.instadownloader.instadpdownloader.R;
import com.instadownloader.instadpdownloader.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 3/9/17.
 */

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageRecyclerHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<String> imageUrls = new ArrayList<>();
    private List<String> imageUrlsThumbNail = new ArrayList<>();



    public ImageDisplayAdapter(Context context, List<String> imageUrls, List<String> imageUrlsThumbNail) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.imageUrlsThumbNail = imageUrlsThumbNail;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(final ImageRecyclerHolder holder, final int position) {

        Picasso.with(context).load(imageUrlsThumbNail.get(position)).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDisplayActivity.class);
                intent.putExtra(Constants.BUNDLE_IMAGE_URL, imageUrls.get(holder.getAdapterPosition()));
                context.startActivity(intent);

            }
        });
    }


    @Override
    public ImageRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.custom_card_layout, parent, false);
//        ImageRecyclerHolder standardListRecyclerHolder = new ImageRecyclerHolder(v);
        return new ImageRecyclerHolder(v);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
