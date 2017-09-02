package com.instadownloader.instadpdownloader.Activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.instadownloader.instadpdownloader.Notifications.GCMRegistrationIntentService;
import com.instadownloader.instadpdownloader.R;
import com.instadownloader.instadpdownloader.Tasks.LoadJsonData;
import com.instadownloader.instadpdownloader.Utils.Constants;

public class MainActivity extends Activity {

    private EditText userNameEditText;
    private Button download, viewProfile;
    private InterstitialAd mInterstitialAd;
    private TextView textViewToolbar;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        userNameEditText = (EditText) findViewById(R.id.inputUsername);

        download = (Button) findViewById(R.id.downloadDpButton);
        viewProfile = (Button) findViewById(R.id.viewProfileButton);
        mAdView = (AdView) findViewById(R.id.adView);

        userNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    downloadDp();
                return true;
            }
        });
        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar);
        textViewToolbar = (TextView) toolbar.findViewById(R.id.toolbarText);
        textViewToolbar.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.BILLABONG_TTF));
        loadBannerAds();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDp();
            }
        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProfile();
            }
        });
//        mInterstitialAd = new InterstitialAd(MainActivity.this);
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad));
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(getResources().getString(R.string.testid))
//                .build();
//        mInterstitialAd.loadAd(adRequest);
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                while (mInterstitialAd.isLoading()) {
//                }
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//
//            }
//        });

    }


    private void loadProfile() {
        LoadJsonData loadJsonData = new LoadJsonData(MainActivity.this, userNameEditText.getText().toString(), Constants.ACTION_VIEW_PHOTOS);
        loadJsonData.execute();

    }

    private void downloadDp() {
        LoadJsonData loadJsonData = new LoadJsonData(MainActivity.this, userNameEditText.getText().toString(), Constants.ACTION_DOWNLOAD_DP);
        loadJsonData.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.toString();
//                if(a.startsWith(Constants.INSTA_PROFILE_START))
//                Toast.makeText(getBaseContext(),"Copy:\n"+a, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadBannerAds() {
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.test_id_1))
                .build();
        mAdView.loadAd(adRequest1);
    }


}
