package com.instadownloader.instadpdownloader;

/**
 * Created by dell-ca on 18/12/17.
 */

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.instadownloader.instadpdownloader.Activity.MainActivity;
import com.instadownloader.instadpdownloader.Tasks.LoadJsonData;
import com.instadownloader.instadpdownloader.Utils.Constants;
import com.instadownloader.instadpdownloader.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnInstaDisplayDpService extends Service {

    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params, paramsForLogo;
    private ImageView popUp;
    private RelativeLayout meaningLayout, parentLayout;
    private long startTime;
    private CountDownTimer timer, subTimer, pulsingTimer;
    private String meaning;
    private String word;
    private String jsonData;
    private boolean isLocalMeaning = false;
    public static final String TAG = "ClipBoardService";
    private FirebaseAnalytics mFirebaseAnalytics;
    private Boolean responseReceived;
    private LinearLayout.LayoutParams paramsChild, paramsPopup;
    private Animation in, in2, out, popUpIn, popUpOut, popUpOutNoLoad;
    private ScaleAnimation scale1;
    private RelativeLayout blip;
    private String calledWord;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        calledWord = null;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        meaningLayout = new RelativeLayout(this);
        parentLayout = new RelativeLayout(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.overlay_layout, meaningLayout);

        blip = new RelativeLayout(this);
//        layoutInflater.inflate(R.layout.activity_stickypopup_circleblip, blip);

        blip.setVisibility(View.GONE);

        scale1 = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale1.setDuration(1000);


        //Animation set-up

        paramsPopup = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsPopup.gravity = Gravity.BOTTOM | Gravity.LEFT;


        blip.setClickable(true);
        blip.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if ((Math.abs(initialTouchX - event.getRawX()) < 5) && (Math.abs(initialTouchY - event.getRawY()) < 5)) {
                            if (subTimer != null) {
                                subTimer.cancel();
                            }
                            if (timer != null) {
                                timer.cancel();
                            }
                            try {
                                blip.setVisibility(View.GONE);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
//                        windowManager.updateViewLayout(popUp, paramsForLogo);
                        return true;
                }
                return false;
            }
        });


        //Params for the meaning strip
        params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM;

        paramsChild = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsChild.gravity = Gravity.BOTTOM;


        meaningLayout.setClickable(true);
        meaningLayout.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if ((Math.abs(initialTouchX - event.getRawX()) < 5) && (Math.abs(initialTouchY - event.getRawY()) < 5)) {
                            timer.cancel();
                            if (subTimer != null) {
                                subTimer.cancel();
                            }
                            blip.clearAnimation();
                            meaningLayout.clearAnimation();

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            Bundle bundle = new Bundle();

                            try {
                                if (mFirebaseAnalytics != null)
                                    mFirebaseAnalytics.logEvent("HelloMeaningDetailedWordLookup", null);
                            } catch (Exception e) {

                            }
                            startActivity(i);
                            meaningLayout.setVisibility(View.GONE);
                            blip.setVisibility(View.GONE);

                            parentLayout.setVisibility(View.GONE);
//                            parentLayout.removeAllViews();
//                            windowManager.removeView(parentLayout);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
//                        windowManager.updateViewLayout(meaningLayout, params);
                        return true;
                }
                return false;
            }
        });
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(changeListener);
        meaningLayout.setVisibility(View.GONE);
        popUp.setVisibility(View.GONE);
        parentLayout.addView(meaningLayout, paramsChild);
//        parentLayout.addView(popUp, paramsPopup);
        parentLayout.addView(blip, paramsPopup);
        windowManager.addView(parentLayout, params);
    }

    private ClipboardManager.OnPrimaryClipChangedListener changeListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            if ((clipboardManager.hasPrimaryClip()) &&  ((clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))) ){
                //since the clipboard contains plain text.
                ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);

                // Gets the clipboard as text.
                String pasteData = item.getText().toString();
                if(isInstaData(pasteData)){
                    if (Utils.isNetworkAvailable(getApplicationContext())) {
                        
                    } else {

                    }
                }

        }

    }};

    @Override
    public void onDestroy() {


        if (clipboardManager != null) {
            clipboardManager.removePrimaryClipChangedListener(changeListener);
        }
        try {
            windowManager.removeView(parentLayout);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    private boolean isInstaData(String url){
        Pattern r = Pattern.compile(Constants.REGEX_FOR_FILTER_USERNAME_JAVA);

        Matcher m = r.matcher(url);
        return m.find();
    }

}

