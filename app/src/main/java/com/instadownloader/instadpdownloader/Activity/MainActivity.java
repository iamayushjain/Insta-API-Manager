package com.instadownloader.instadpdownloader.Activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instadownloader.instadpdownloader.Notifications.GCMRegistrationIntentService;
import com.instadownloader.instadpdownloader.R;
import com.instadownloader.instadpdownloader.Tasks.LoadJsonData;
import com.instadownloader.instadpdownloader.Utils.Constants;
import com.instadownloader.instadpdownloader.Utils.FirebaseConstants;
import com.instadownloader.instadpdownloader.Utils.LogWrapper;
import com.instadownloader.instadpdownloader.Utils.PreferencesConstants;
import com.instadownloader.instadpdownloader.Utils.SharedPreferencesWrapper;
import com.instadownloader.instadpdownloader.Utils.Utils;

public class MainActivity extends Activity {

    private EditText userNameEditText;
    private Button download, viewProfile;
    public static InterstitialAd mInterstitialAd;
    private TextView textViewToolbar;
    private AdView mAdView;
    private int currentAdsCount, adsCountLimit;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;


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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);

    }


    private void loadProfile() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            LoadJsonData loadJsonData = new LoadJsonData(MainActivity.this, userNameEditText.getText().toString(), Constants.ACTION_VIEW_PHOTOS);
            loadJsonData.execute();
        } else {
            noInternet();
        }
    }

    private void downloadDp() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            LoadJsonData loadJsonData = new LoadJsonData(MainActivity.this, userNameEditText.getText().toString(), Constants.ACTION_DOWNLOAD_DP);
            loadJsonData.execute();
        } else {
            noInternet();
        }
    }

    private void noInternet() {
        Snackbar.make(findViewById(R.id.rootRelativeLayout), getString(R.string.no_internet_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.toString();
//                if(a.startsWith(Constants.INSTA_PROFILE_START))
//                Toast.makeText(getBaseContext(),"Copy:\n"+a, Toast.LENGTH_LONG).show();
            }
        });
        if (!Utils.isNetworkAvailable(getApplicationContext())) {
            noInternet();
        }
    }

    private void loadBannerAds() {
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.test_id_1))
                .build();
        mAdView.loadAd(adRequest1);
    }


    private void loadInterstitialAds() {

        final SharedPreferencesWrapper sharedPreferencesWrapper = new SharedPreferencesWrapper(getApplicationContext());
        String adSavedOccurrence = sharedPreferencesWrapper.getString(PreferencesConstants.SHARED_PREF_ADS_COUNT, PreferencesConstants.SHARED_PREF_ADS_COUNT_LAUNCHER_INTERSTITIAL, "0");
        currentAdsCount = Integer.parseInt(adSavedOccurrence);
        currentAdsCount++;


        databaseReferenceGsim.child(FirebaseConstants.ADS_COUNT_MAIN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                adsCountLimit = Integer.parseInt(snapshot.getValue().toString());

                if (currentAdsCount >= adsCountLimit) {
                    currentAdsCount = 0;
                    mInterstitialAd = new InterstitialAd(MainActivity.this);
                    mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_1));
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice(getResources().getString(R.string.test_id_1))
                            .build();
                    mInterstitialAd.loadAd(adRequest);

                }


                sharedPreferencesWrapper.putSingleData(PreferencesConstants.SHARED_PREF_ADS_COUNT, PreferencesConstants.SHARED_PREF_ADS_COUNT_LAUNCHER_INTERSTITIAL, currentAdsCount + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogWrapper.out(getApplicationContext(), databaseError.getMessage());
            }
        });
    }
}




