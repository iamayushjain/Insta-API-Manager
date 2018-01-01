package com.instadownloader.instadpdownloader.Activity;
public class Service{
/**
 * Created by dell-ca on 16/12/17.
// */
//package com.CultureAlley.stickyPopup;
//
//        import android.app.Service;
//        import android.content.ClipData;
//        import android.content.ClipboardManager;
//        import android.content.Intent;
//        import android.graphics.PixelFormat;
//        import android.os.Build;
//        import android.os.Bundle;
//        import android.os.CountDownTimer;
//        import android.os.Handler;
//        import android.os.IBinder;
//        import android.provider.Settings;
//        import android.support.annotation.Nullable;
//        import android.util.DisplayMetrics;
//        import android.util.Log;
//        import android.view.Gravity;
//        import android.view.LayoutInflater;
//        import android.view.MotionEvent;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.view.WindowManager;
//        import android.view.animation.Animation;
//        import android.view.animation.AnimationUtils;
//        import android.view.animation.ScaleAnimation;
//        import android.view.animation.TranslateAnimation;
//        import android.widget.ImageView;
//        import android.widget.LinearLayout;
//        import android.widget.RelativeLayout;
//        import android.widget.TextView;
//
//        import com.CultureAlley.common.CAUtility;
//        import com.CultureAlley.common.preferences.Preferences;
//        import com.CultureAlley.common.server.CAServerInterface;
//        import com.CultureAlley.common.server.CAServerParameter;
//        import com.CultureAlley.database.DatabaseInterface;
//        import com.CultureAlley.japanese.english.R;
//        import com.CultureAlley.search.IndexDefinitions;
//        import com.CultureAlley.search.SearchOnlineWordsCompleteDetails;
//        import com.CultureAlley.settings.defaults.Defaults;
//        import com.google.firebase.analytics.FirebaseAnalytics;
//
//        import org.json.JSONException;
//        import org.json.JSONObject;
//
//        import java.io.IOException;
//        import java.io.UnsupportedEncodingException;
//        import java.net.URLDecoder;
//        import java.util.ArrayList;
//        import java.util.Locale;
//        import java.util.concurrent.ExecutionException;
//
///**
// * Created by siddhant on 1/6/16.
// */
//public class ClipBoardService extends Service {
//
//    private ClipboardManager clipboardManager;
//    private ClipData clipData;
//    private WindowManager windowManager;
//    private WindowManager.LayoutParams params, paramsForLogo;
//    private ImageView popUp;
//    private RelativeLayout meaningLayout, parentLayout;
//    private long startTime;
//    private CountDownTimer timer, subTimer, pulsingTimer;
//    private DatabaseInterface databaseInterface;
//    private String meaning;
//    private String word;
//    private String jsonData;
//    private boolean isLocalMeaning = false;
    public static final String TAG = "ClipBoardService";
//    private FirebaseAnalytics mFirebaseAnalytics;
//    private Boolean responseReceived;
//    private LinearLayout.LayoutParams paramsChild, paramsPopup;
//    private Animation in, in2, out, popUpIn, popUpOut, popUpOutNoLoad;
//    private ScaleAnimation scale1;
//    private RelativeLayout blip;
//    private String calledWord;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
//
//        calledWord = null;
//        databaseInterface = new DatabaseInterface(getApplicationContext());
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        meaningLayout = new RelativeLayout(this);
//        parentLayout = new RelativeLayout(this);
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        layoutInflater.inflate(R.layout.activity_stickypopup_footer, meaningLayout);
//
//        blip = new RelativeLayout(this);
//        layoutInflater.inflate(R.layout.activity_stickypopup_circleblip, blip);
//
//        blip.setVisibility(View.GONE);
//
//        scale1 = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF,
//                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scale1.setDuration(1000);
//
//
//
//        //Animation set-up
//        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in);
//        in.setDuration(250);
//        in.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("Inanimation","Started");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d("Inanimation","stopped");
//                meaningLayout.clearAnimation();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//
//        in2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in);
//        in2.setDuration(250);
//        in2.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("Inanimation","Started");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d("Inanimation","stopped");
//                meaningLayout.clearAnimation();
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_out);
//        out.setDuration(250);
//        out.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("OutAnimation", "Started");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d("OutAnimation","Ended");
//                meaningLayout.findViewById(R.id.listenIcon).setVisibility(View.INVISIBLE);
//                meaningLayout.setVisibility(View.GONE);
//                blip.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//
//        popUpIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in);
//        popUpIn.setDuration(250);
//        popUpIn.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("PopUpInAnimation", "started");
//                blip.findViewById(R.id.pulse_circle).setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//
//                Log.d("PopUpInAnimation", "stopped");
//
//                blip.clearAnimation();
//                try{
//                    pulsingTimer.cancel();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                pulsingTimer = new CountDownTimer(5000, 1000) {
//                    @Override
//                    public void onTick(long l) {
//                        (blip.findViewById(R.id.pulse_circle)).startAnimation(scale1);
//                    }
//
//                    @Override
//                    public void onFinish() {
//
//                    }
//                };
//                pulsingTimer.start();
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        scale1.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        popUpOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_out);
//        popUpOut.setDuration(250);
//        popUpOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("PopUpOutAnimation","started");
//                try {
//                    timer.cancel();
//                } catch(Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d("PopUpOutAnimation","stopped");
//                blip.clearAnimation();
//                blip.setVisibility(View.GONE);
//
//
//
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//
//        popUpOutNoLoad = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_out);
//        popUpOutNoLoad.setDuration(250);
//        popUpOutNoLoad.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("popUpOutNoLoad", "started");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d("popUpOutNoLoad", "finished");
//                blip.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        popUp = new ImageView(this);
//        popUp.setImageResource(R.drawable.ic_launcher);
////        popUp.setMaxWidth(75);
//
//
//        //Params for the logo shown for the load time
//        paramsPopup = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsPopup.gravity = Gravity.BOTTOM | Gravity.LEFT;
//
//
//
//
//        blip.setClickable(true);
//        blip.setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = params.x;
//                        initialY = params.y;
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        if( (Math.abs(initialTouchX - event.getRawX())<5) && (Math.abs(initialTouchY - event.getRawY())<5) ) {
//                            if(subTimer != null){
//                                subTimer.cancel();
//                            }
//                            if(timer != null) {
//                                timer.cancel();
//                            }
//                            try {
//                                blip.setVisibility(View.GONE);
//                            } catch(Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        params.x = initialX
//                                + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY
//                                + (int) (event.getRawY() - initialTouchY);
////                        windowManager.updateViewLayout(popUp, paramsForLogo);
//                        return true;
//                }
//                return false;
//            }
//        });
//
//
//
//
//        //Params for the meaning strip
//        params= new WindowManager.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.BOTTOM;
//
//        paramsChild = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsChild.gravity = Gravity.BOTTOM;
//
//
//
//        meaningLayout.setClickable(true);
//        meaningLayout.setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = params.x;
//                        initialY = params.y;
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        if( (Math.abs(initialTouchX - event.getRawX())<5) && (Math.abs(initialTouchY - event.getRawY())<5) ) {
//                            timer.cancel();
//                            if(subTimer != null) {
//                                subTimer.cancel();
//                            }
//                            blip.clearAnimation();
//                            meaningLayout.clearAnimation();
//
//                            Intent i = new Intent(getApplicationContext(), SearchOnlineWordsCompleteDetails.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                    | Intent.FLAG_ACTIVITY_NO_HISTORY
//                                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            Bundle bundle = new Bundle();
//
//                            bundle.putInt("type", IndexDefinitions.TYPE_WORD);
//                            bundle.putString("word", word);
//                            bundle.putString("meaning",meaning);
//                            bundle.putString("data", jsonData);
//                            bundle.putBoolean("isLocalMeaning", isLocalMeaning);
//                            i.putExtras(bundle);
//                            try{
//                                if(mFirebaseAnalytics!=null)
//                                    mFirebaseAnalytics.logEvent("HelloMeaningDetailedWordLookup",null);
//                            }catch(Exception e){
//
//                            }
//                            startActivity(i);
//                            meaningLayout.setVisibility(View.GONE);
//                            blip.setVisibility(View.GONE);
//
//                            parentLayout.setVisibility(View.GONE);
////                            parentLayout.removeAllViews();
////                            windowManager.removeView(parentLayout);
//                        }
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        params.x = initialX
//                                + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY
//                                + (int) (event.getRawY() - initialTouchY);
////                        windowManager.updateViewLayout(meaningLayout, params);
//                        return true;
//                }
//                return false;
//            }
//        });
//        try{
//            if(Build.VERSION.SDK_INT >= 23) {
//                if (!Settings.canDrawOverlays(getApplicationContext())) {
//                    Preferences.put(getApplicationContext(), Preferences.KEY_IS_MEANING_SEARCH_ANYWHERE_ENABLE, false);
//                    stopSelf();
//                    return;
//                }
//            }
//        }catch (Throwable e){
//            if(CAUtility.isDebugModeOn){
//                e.printStackTrace();
//            }
//        }
//        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        clipboardManager.addPrimaryClipChangedListener(changeListener);
//        meaningLayout.setVisibility(View.GONE);
//        popUp.setVisibility(View.GONE);
//        parentLayout.addView(meaningLayout, paramsChild);
////        parentLayout.addView(popUp, paramsPopup);
//        parentLayout.addView(blip, paramsPopup);
//        windowManager.addView(parentLayout, params);
//
//    }
//
//    private ClipboardManager.OnPrimaryClipChangedListener changeListener =  new ClipboardManager.OnPrimaryClipChangedListener() {
//        @Override
//        public void onPrimaryClipChanged() {
//            try {
//                try {
//                    if (timer != null) {
//                        timer.cancel();
//                        meaningLayout.setVisibility(View.GONE);
//                        blip.setVisibility(View.GONE);
//                    }
//                    if (subTimer != null) {
//                        subTimer.cancel();
//                        meaningLayout.setVisibility(View.GONE);
//                        blip.setVisibility(View.GONE);
//                    }
//                } catch(Exception ex) {
//                    ex.printStackTrace();
//                }
//                clipData = clipboardManager.getPrimaryClip();
//                if (calledWord!=null && calledWord.equals((String) clipData.getItemAt(0).getText())){
//                    calledWord = null;
//                    return;
//                }
//                calledWord = (String) clipData.getItemAt(0).getText();
//
//
//                word = (String) clipData.getItemAt(0).getText();
//                Log.d("clip changed", String.valueOf(clipData));
//
//                word = word.toLowerCase(Locale.US).replaceAll("[\n\r\n\t?'\"!:;,.]", "");
//                word = word.replaceAll("\\p{P}", "");
//                int index = word.indexOf(" ");
//                if (index > 0) {
//                    word = word.substring(0, index);
//                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        meaning = databaseInterface.getDictionaryMeaningFromTable(word, Defaults.getInstance(getApplicationContext()).fromLanguage);
//                        if (meaning == null || "".equals(meaning)) {
//                            try {
//                                boolean result = isAlpha(word);
//
//                                if (!result) {
//                                    DatabaseInterface dbInterface = new DatabaseInterface(
//                                            getApplicationContext());
//                                    meaning = dbInterface
//                                            .getDictionaryMeaningFromTable(
//                                                    word,
//                                                    Defaults.getInstance(getApplicationContext()).toLanguage,
//                                                    Defaults.getInstance(getApplicationContext()).fromLanguage);
//                                }
//
//                                Log.i("ReverseDictionary", " result = " + result + " word = "
//                                        + word + " meaning = " + meaning);
//
//                                if (meaning == null || meaning.isEmpty()) {
//                                    isLocalMeaning = false;
//                                    responseReceived = false;
//
//
//                                    //put up the popup to show loading
//                                    new Handler(getMainLooper()).post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//
//                                                parentLayout.setVisibility(View.VISIBLE);
//                                                blip.setVisibility(View.VISIBLE);
//                                                blip.startAnimation(popUpIn);
//
//                                                timer = new CountDownTimer(5000, 1000) {
//                                                    boolean notDoneBefore = false;
//                                                    boolean responseReceivedInTime = false;
//                                                    @Override
//                                                    public void onTick(long l) {
//                                                        try {
//
//
//                                                            if (responseReceived && !notDoneBefore) {
//                                                                notDoneBefore = true;
//                                                                responseReceivedInTime = true;
//                                                                pulsingTimer.cancel();
//
//                                                                (blip.findViewById(R.id.pulse_circle)).clearAnimation();
//                                                                blip.clearAnimation();
//                                                                blip.findViewById(R.id.pulse_circle).setVisibility(View.INVISIBLE);
////                                                                    blip.setVisibility(View.GONE);
//
//                                                                try {
//                                                                    word = URLDecoder.decode(word, "UTF-8");
//                                                                    meaning = URLDecoder.decode(meaning, "UTF-8");
//                                                                } catch (UnsupportedEncodingException e) {
//                                                                    e.printStackTrace();
//                                                                }catch (Exception e){
//                                                                    if(CAUtility.isDebugModeOn){
//                                                                        e.printStackTrace();
//                                                                    }
//                                                                }
//
//                                                                ((TextView) meaningLayout.findViewById(R.id.wordTv)).setText(word);
//                                                                ((TextView) meaningLayout.findViewById(R.id.meaningTv)).setText(meaning);
//                                                                ((TextView) meaningLayout.findViewById(R.id.equals_to_sign)).setText("=");
//                                                                ((ImageView) meaningLayout.findViewById(R.id.listenIcon)).setVisibility(View.INVISIBLE);
//
//                                                                try {
//                                                                    if (mFirebaseAnalytics != null)
//                                                                        mFirebaseAnalytics.logEvent("HelloMeaningLookUp", null);
//                                                                } catch (Exception e) {
//
//                                                                }
//                                                                Log.i("ClipBoardService", "2");
//                                                                meaningLayout.setVisibility(View.VISIBLE);
//                                                                parentLayout.setVisibility(View.VISIBLE);
//                                                                meaningLayout.startAnimation(in);
//
//
//                                                                Log.d("SubTimer","Executing");
//                                                                subTimer = new CountDownTimer(5000, 1000) {
//                                                                    @Override
//                                                                    public void onTick(long l) {
//
//                                                                    }
//                                                                    @Override
//                                                                    public void onFinish() {
//                                                                        try {
//                                                                            Log.d("subtimerexpired","true");
//                                                                            meaningLayout.startAnimation(out);
//                                                                            blip.startAnimation(out);
//                                                                        } catch(Exception ex) {
//                                                                            ex.printStackTrace();
//                                                                        }
//                                                                    }
//                                                                };
//                                                                subTimer.start();
//
//                                                            }
//                                                        } catch (Exception ex) {
//                                                            ex.printStackTrace();
//                                                        }
//
//                                                    }
//
//                                                    @Override
//                                                    public void onFinish() {
//                                                        try {
//                                                            Log.d("timer","finished");
//                                                            if((blip.getVisibility() == View.VISIBLE) && !responseReceivedInTime) {
//                                                                Log.d("outanimation","Called");
//                                                                blip.startAnimation(popUpOutNoLoad);
//                                                            }
//                                                        } catch (Exception ex) {
//                                                            ex.printStackTrace();
//                                                        }
//                                                    }
//                                                };
//                                                timer.start();
//                                            } catch (Exception ex) {
//                                                ex.printStackTrace();
//                                            }
//                                        }
//                                    });
//
//
//                                    if (CAUtility
//                                            .isConnectedToInternet(getApplicationContext())) {
//                                        ArrayList<CAServerParameter> params = new ArrayList<CAServerParameter>();
//                                        params.add(new CAServerParameter("word", word));
//                                        params.add(new CAServerParameter("dictionary", "true"));
//                                        params.add(new CAServerParameter("search_category", "DICTIONARY"));
//                                        params.add(new CAServerParameter(
//                                                "language",
//                                                Defaults.getInstance(getApplicationContext()).fromLanguage));
//                                        if (!result) {
//                                            params.add(new CAServerParameter("isReverse",
//                                                    "true"));
//                                        }
//
//                                        String actionName = CAServerInterface.PHP_ACTION_GET_DICTIONARY_MEANING;
//                                        String response = CAServerInterface.callPHPActionSync(
//                                                getApplicationContext(),
//                                                actionName, params);
//
//                                        //response received from the server
//                                        responseReceived = true;
//
//
//                                        JSONObject json = new JSONObject(response);
//
//                                        if (json.has("success")
//                                                && json.get("success") instanceof JSONObject) {
//                                            meaning = json.getJSONObject("success").getString(
//                                                    "meaning");
//                                            try {
//                                                word = URLDecoder.decode(word, "UTF-8");
//                                                meaning = URLDecoder.decode(meaning, "UTF-8");
//                                            } catch (UnsupportedEncodingException e) {
//                                                e.printStackTrace();
//                                            } catch (Exception e){
//                                                if(CAUtility.isDebugModeOn){
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            if (json.getJSONObject("success").has("data")
//                                                    && !("null".equals(json.getJSONObject(
//                                                    "success").getString("data")))
//                                                    && json.getJSONObject("success")
//                                                    .getJSONObject("data") instanceof JSONObject) {
//                                                jsonData = json.getJSONObject("success")
//                                                        .getJSONObject("data").toString();
//                                            }
//                                        }
//                                    } else {
//                                        CAUtility.sendWordRequestToServer(getApplicationContext(), word, "DICTIONARY", false);
//                                    }
//
//                                } else {
//                                    CAUtility.sendWordRequestToServer(getApplicationContext(), word, "DICTIONARY", false);
//                                    isLocalMeaning = true;
//                                }
//                            } catch (JSONException e) {
//                                if (CAUtility.isDebugModeOn) {
//                                    CAUtility.printStackTrace(e);
//                                }
//
//                            } catch (IOException e) {
//                                if (CAUtility.isDebugModeOn) {
//                                    CAUtility.printStackTrace(e);
//                                }
//
//                            }
//                        } else {
//                            isLocalMeaning = true;
//                        }
//                        new Handler(getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    word = URLDecoder.decode(word, "UTF-8");
//                                    meaning = URLDecoder.decode(meaning, "UTF-8");
//                                    ((TextView) meaningLayout.findViewById(R.id.wordTv)).setText(word);
//                                    ((TextView) meaningLayout.findViewById(R.id.meaningTv)).setText(meaning);
//                                    ((TextView) meaningLayout.findViewById(R.id.equals_to_sign)).setText("=");
//
//                                    try {
//                                        if (mFirebaseAnalytics != null)
//                                            mFirebaseAnalytics.logEvent("HelloMeaningLookUp", null);
//                                    } catch (Exception e) {
//
//                                    }
//                                    if (isLocalMeaning) {
//
//                                        Log.i("ClipBoardService", "1");
//                                        meaningLayout.setVisibility(View.VISIBLE);
//                                        meaningLayout.startAnimation(in);
//                                        blip.findViewById(R.id.pulse_circle).setVisibility(View.GONE);
//                                        meaningLayout.findViewById(R.id.listenIcon).setVisibility(View.VISIBLE);
//                                        parentLayout.setVisibility(View.VISIBLE);
//
//                                        timer = new CountDownTimer(5000, 1000) {
//                                            @Override
//                                            public void onTick(long l) {
//
//                                            }
//
//                                            @Override
//                                            public void onFinish() {
//                                                Log.d("timerexpired", "true");
//                                                meaningLayout.startAnimation(out);
//
//                                            }
//                                        };
//                                        timer.start();
//                                    }
//                                } catch (Throwable e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//                    }
//                }).start();
//            }catch(Throwable ex){
//                ex.printStackTrace();
//            }
//        }
//
//    };
//
//    @Override
//    public void onDestroy() {
//
//        if(clipboardManager != null) {
//            clipboardManager.removePrimaryClipChangedListener(changeListener);
//        }
//        try {
//            windowManager.removeView(parentLayout);
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
//        super.onDestroy();
//    }
//    public boolean isAlpha(String s) {
//        String pattern = "^[a-zA-Z0-9 ]*$";
//        if (s != null && s.matches(pattern)) {
//            return true;
//        }
//        return false;
//    }
}
