package com.instadownloader.instadpdownloader.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instadownloader.instadpdownloader.R;
import com.instadownloader.instadpdownloader.Tasks.DownloadingPhoto;
import com.instadownloader.instadpdownloader.Utils.Constants;
import com.instadownloader.instadpdownloader.Utils.LogWrapper;
import com.instadownloader.instadpdownloader.Utils.PreferencesConstants;
import com.instadownloader.instadpdownloader.Utils.SharedPreferencesWrapper;
import com.instadownloader.instadpdownloader.Utils.ZoomImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by ayush on 29/8/17.
 */

public class ImageDisplayActivity extends Activity {

    private String imageUrl;
    private ImageView imageView;
    private TextView textViewToolbar;
    private Button downloadButton;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int permissionCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        getBundle();
        init();
        setImageView();
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.imageView);
        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar);
        textViewToolbar = (TextView) toolbar.findViewById(R.id.toolbarText);
        textViewToolbar.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.BILLABONG_TTF));
        downloadButton = (Button) findViewById(R.id.downloadButton);
        downloadButton.setEnabled(false);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForPermission();
            }
        });

    }

    private void downloadPhoto() {
        DownloadingPhoto downloadingPhoto = new DownloadingPhoto(getApplicationContext(), "abcd.jpg", imageUrl);
        downloadingPhoto.execute();

    }

    private void setImageView() {
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageView);
        LogWrapper.d(getApplicationContext(), "ATG", imageUrl);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        final ZoomImageView zoomImageView = new ZoomImageView(720, 720);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return zoomImageView.zoom(v, event);
            }
        });
        downloadButton.setEnabled(true);
    }


    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        String bundleString = bundle.getString(Constants.BUNDLE_IMAGE_URL);
        if (bundleString != null)
            imageUrl = bundleString.replace("s150x150/", "");
        else
            imageUrl = "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadPhoto();
                } else if (permissionCount == 0) {
                    Snackbar.make(findViewById(R.id.rootRelativeLayout), getString(R.string.permission), Snackbar.LENGTH_LONG)
                            .setAction(R.string.permission, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkForPermission();
                                }
                            }).show();
                    permissionCount++;

                } else {
                    Snackbar.make(findViewById(R.id.rootRelativeLayout), getString(R.string.no_permission), Snackbar.LENGTH_LONG).show();
                }
        }
    }

    private void checkForPermission() {

        int permissionReadExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED
                || permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            downloadPhoto();
        }
    }

//    private void onToggleButtonAction(){
//
//        if(SharedPreferencesWrapper.getString( PreferencesConstants.GCM_TOKEN, )){
//            stopService(new Intent(ImageDisplayActivity.this, ClipBoardService.class).addCategory(ClipBoardService.TAG));
//            Preferences.put(getApplicationContext(), Preferences.KEY_IS_MEANING_SEARCH_ANYWHERE_ENABLE, false);
//            mHelloMeaningSwitch.setImageResource(R.drawable.toggle_off);
//            mHelloMeaningSwitch.setTag("off");
//            searchStatus.setText(getString(R.string.sticky_enable));
//            hellomeaning_toggle_text.setText(getString(R.string.sticky_enable));
//            status.setText(getString(R.string.findmeaning_disable_msg));
//            Toast.makeText(getApplicationContext(), getString(R.string.findmeaning_disable), Toast.LENGTH_SHORT).show();
//    }
//				else{
//
//        if(Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(getApplicationContext())) {
//                Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent2.setData(Uri.parse("package:"+getPackageName()));
//                startActivityForResult(intent2, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
//            }else{
//                startService(new Intent(StickyActivity.this, ClipBoardService.class).addCategory(ClipBoardService.TAG));
//                Preferences.put(getApplicationContext(), Preferences.KEY_IS_MEANING_SEARCH_ANYWHERE_ENABLE, true);
//                searchStatus.setText(getString(R.string.sticky_disable));
//                hellomeaning_toggle_text.setText(getString(R.string.sticky_disable));
//                mHelloMeaningSwitch.setImageResource(R.drawable.toggle_on);
//                mHelloMeaningSwitch.setTag("on");
//                status.setText(getString(R.string.findmeaning_enable_msg));
//                Toast.makeText(getApplicationContext(), getString(R.string.findmeaning_enable), Toast.LENGTH_SHORT).show();
//            }
//        }else{
//            startService(new Intent(StickyActivity.this, ClipBoardService.class).addCategory(ClipBoardService.TAG));
//            Preferences.put(getApplicationContext(), Preferences.KEY_IS_MEANING_SEARCH_ANYWHERE_ENABLE, true);
//            mHelloMeaningSwitch.setImageResource(R.drawable.toggle_on);
//            mHelloMeaningSwitch.setTag("on");
//            searchStatus.setText(getString(R.string.sticky_disable));
//            hellomeaning_toggle_text.setText(getString(R.string.sticky_disable));
//            status.setText(getString(R.string.findmeaning_enable_msg));
//            Toast.makeText(getApplicationContext(), getString(R.string.findmeaning_enable), Toast.LENGTH_SHORT).show();
//        }
//    }
//}
//});
//        }
//@Override
//protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
//        if (Settings.canDrawOverlays(this)) {
//        startService(new Intent(StickyActivity.this, ClipBoardService.class).addCategory(ClipBoardService.TAG));
//        Preferences.put(getApplicationContext(), Preferences.KEY_IS_MEANING_SEARCH_ANYWHERE_ENABLE, true);
//        mHelloMeaningSwitch.setImageResource(R.drawable.toggle_on);
//        mHelloMeaningSwitch.setTag("on");
//        searchStatus.setText(getString(R.string.sticky_disable));
//        hellomeaning_toggle_text.setText(getString(R.string.sticky_disable));
//        status.setText(getString(R.string.findmeaning_enable_msg));
//        Toast.makeText(getApplicationContext(), getString(R.string.findmeaning_enable), Toast.LENGTH_SHORT).show();
//        }
//        }
}
