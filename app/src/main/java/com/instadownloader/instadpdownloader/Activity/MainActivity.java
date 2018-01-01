package com.instadownloader.instadpdownloader.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.instadownloader.instadpdownloader.Utils.AppUtils;
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
    private SwitchCompat switchLoadProfileFromAnyWhere;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 526;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    //initialize all variable here
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
        loadInterstitialAds();
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
        switchLoadProfileFromAnyWhere = (SwitchCompat) findViewById(R.id.switchButton);
        getStoredSwitchStatus();
        switchLoadProfileFromAnyWhere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogWrapper.d("Service", "Button changed");
                setValueSwitch(isChecked);
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);

    }

    @Override
    protected void onStart() {
        super.onStart();
        startGCMService();

        if (!Utils.isNetworkAvailable(getApplicationContext())) {
            noInternet();
        }
        getClipBoardData();
    }

    private void getClipBoardData() {
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        if ((clipboard.hasPrimaryClip()) &&
                ((clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)))) {
            //since the clipboard contains plain text.
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String pasteData = item.getText().toString();
            if (AppUtils.isInstaData(pasteData)) {
                userNameEditText.setText(pasteData);
            }
        }

    }

    private void startGCMService() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }

    //to change button only once is service is completely started or ended
    private void getStoredSwitchStatus() {
        if (SharedPreferencesWrapper.getBoolean(getApplicationContext(),
                PreferencesConstants.SHARED_PREF_UTILS,
                PreferencesConstants.SHARED_PREF_ANYWHERE_IMAGE_FIND, true)) {
            initiateService();
        } else {
            destroyService();
        }
    }

    private void setValueSwitch(boolean isChecked) {
        if (isChecked) {
            initiateService();
        } else {
            destroyService();
        }
    }

    private void startServiceInsta() {
        LogWrapper.d("Service", "Start Service Here");
        startService(new Intent(MainActivity.this, Service.class).addCategory(Service.TAG));
        SharedPreferencesWrapper.putData(getApplicationContext(),
                PreferencesConstants.SHARED_PREF_UTILS,
                PreferencesConstants.SHARED_PREF_ANYWHERE_IMAGE_FIND, true);
        switchLoadProfileFromAnyWhere.setChecked(true);
    }

    private void initiateService() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent2.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent2, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startServiceInsta();
            }
        } else {
            startServiceInsta();
        }
    }

    private void destroyService() {
        LogWrapper.d(getApplicationContext(), "Service", "Stop Service Here");
        SharedPreferencesWrapper.putData(getApplicationContext(),
                PreferencesConstants.SHARED_PREF_UTILS,
                PreferencesConstants.SHARED_PREF_ANYWHERE_IMAGE_FIND, false);
        switchLoadProfileFromAnyWhere.setChecked(false);
        stopService(new Intent(MainActivity.this, Service.class).addCategory(Service.TAG));
    }

    private void loadProfile() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            String userName = AppUtils.filterUserName(userNameEditText.getText().toString());
            if (userName == null) {
                invalidUserName();
                return;
            }
            LoadJsonData loadJsonData = new LoadJsonData(MainActivity.this,
                    userName, Constants.ACTION_VIEW_PHOTOS);
            loadJsonData.execute();
        } else {
            noInternet();
        }
    }

    private void downloadDp() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            String userName = AppUtils.filterUserName(userNameEditText.getText().toString());
            if (userName == null) {
                invalidUserName();
                return;
            }
            LoadJsonData loadJsonData = new LoadJsonData(MainActivity.this,
                    userName, Constants.ACTION_DOWNLOAD_DP);
            loadJsonData.execute();
        } else {
            noInternet();
        }
    }

    private void noInternet() {
        Snackbar.make(findViewById(R.id.rootRelativeLayout), getString(R.string.no_internet_message),
                Snackbar.LENGTH_LONG).show();
    }

    private void invalidUserName() {
        Snackbar.make(findViewById(R.id.rootRelativeLayout), getString(R.string.invalid_user_name),
                Snackbar.LENGTH_LONG).show();
    }


    private void loadBannerAds() {
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.test_id_1))
                .build();
        mAdView.loadAd(adRequest1);
    }


    private void loadInterstitialAds() {

        final SharedPreferencesWrapper sharedPreferencesWrapper = new SharedPreferencesWrapper(getApplicationContext());
        String adSavedOccurrence = sharedPreferencesWrapper.getString(
                PreferencesConstants.SHARED_PREF_ADS_COUNT,
                PreferencesConstants.SHARED_PREF_ADS_COUNT_LAUNCHER_INTERSTITIAL, "0");
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


                sharedPreferencesWrapper.putSingleData(PreferencesConstants.SHARED_PREF_ADS_COUNT,
                        PreferencesConstants.SHARED_PREF_ADS_COUNT_LAUNCHER_INTERSTITIAL,
                        currentAdsCount + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogWrapper.out(getApplicationContext(), databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(getApplicationContext())) {
                    startServiceInsta();
                } else {
                    switchLoadProfileFromAnyWhere.setChecked(false);
                }
            }
        }
    }
}