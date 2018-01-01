package com.instadownloader.instadpdownloader.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.ads.AdListener;
import com.instadownloader.instadpdownloader.Activity.ImageDisplayActivity;
import com.instadownloader.instadpdownloader.Activity.MainActivity;
import com.instadownloader.instadpdownloader.Activity.ViewPhotosActivity;
import com.instadownloader.instadpdownloader.BuildConfig;
import com.instadownloader.instadpdownloader.Utils.Constants;
import com.instadownloader.instadpdownloader.Utils.LogWrapper;
import com.instadownloader.instadpdownloader.Utils.ObjectClass;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 29/8/17.
 */

public class LoadJsonData extends AsyncTask<Void, Void, ObjectClass> {

    private String userName;
    private boolean isSuccessful;
    private ProgressDialog mProgressDialog;
    private short action;
    private Context context;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
//                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
//                Toast.makeText(MainActivity.this, "Check your network.",
//                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    public LoadJsonData(Context context, String userName, short action) {

        this.userName = userName;
        this.context = context;
        this.action = action;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (context != null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Loading");
            mProgressDialog.setMessage("Please Wait");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        isSuccessful = false;
    }

    @Override
    protected ObjectClass doInBackground(Void... params) {

        try {
            String urlCourse = "https://instagram.com/" + userName + "/?__a=1";
            Connection.Response response = Jsoup.connect(urlCourse)
                    .method(Connection.Method.GET).timeout(0).ignoreContentType(true)
                    .execute();

            JSONObject reader = new JSONObject(response.body());
            JSONObject sys = reader.getJSONObject("user");
            String imageUrl = sys.getString("profile_pic_url");
            boolean isPrivate = sys.getBoolean("is_private");
            LogWrapper.d("Tag", imageUrl);
            if (action == Constants.ACTION_VIEW_PHOTOS) {
                ObjectClass objectClass = getImageUrlsList(sys.getString("media"));
                isSuccessful = true;
                return new ObjectClass(imageUrl, isPrivate, objectClass.getObject1(), objectClass.getObject2());
            }

            isSuccessful = true;
            return new ObjectClass(imageUrl, isPrivate);
        } catch (Exception e) {
            LogWrapper.printStackTrace(e);
            isSuccessful = false;
        }
        return null;

    }


    @Override
    protected void onPostExecute(ObjectClass objectClass) {
        super.onPostExecute(objectClass);


        try {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            if (objectClass != null) {
                Intent intent;
                switch (action) {
                    case Constants.ACTION_DOWNLOAD_DP:
                        intent = new Intent(context, ImageDisplayActivity.class);
                        intent.putExtra(Constants.BUNDLE_IMAGE_URL, (String) objectClass.getObject1());
                        context.startActivity(intent);

                        MainActivity.mInterstitialAd.setAdListener(new AdListener() {
                            public void onAdLoaded() {
                                if (MainActivity.mInterstitialAd.isLoaded()) {
                                    MainActivity.mInterstitialAd.show();
                                }
                            }
                        });

                        break;
                    case Constants.ACTION_VIEW_PHOTOS:
                        boolean isPrivate = (Boolean) objectClass.getObject2();
                        if (isPrivate) {
//                            final InstagramApp instagramApp = new InstagramApp(context, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, BuildConfig.CALLBACK_URL);
//                            instagramApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
//
//                                @Override
//                                public void onSuccess() {
//                                    instagramApp.fetchUserName(handler);
//                                }
//
//                                @Override
//                                public void onFail(String error) {
//
//                                }
//                            });
//                            instagramApp.authorize();
                        } else {
                            intent = new Intent(context, ViewPhotosActivity.class);
                            ArrayList<String> imageUrls = (ArrayList<String>) objectClass.getObject3();

                            ArrayList<String> imageUrlsThumbnail = (ArrayList<String>) objectClass.getObject4();
                            intent.putStringArrayListExtra(Constants.BUNDLE_IMAGES_URL_LIST, imageUrls);
                            intent.putStringArrayListExtra(Constants.BUNDLE_IMAGES_URL_THUMBNAIL_LIST, imageUrlsThumbnail);
                            context.startActivity(intent);
                        }
                        break;
                    case Constants.ACTION_SERVICE_VIEW_PHOTOS:

                }
            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(context, e);
//        Toast.makeText(activity.getApplicationContext(), "Failed Choosing Branch", Toast.LENGTH_LONG).show();
//        Utils.sessionExpired(activity);
        }
    }

    private ObjectClass getImageUrlsList(String json) {
        List<String> imageUrls = new ArrayList<>();

        List<String> imageUrlsThumbnail = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray nodes = reader.getJSONArray("nodes");
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);
                if (node.getBoolean("is_video") != true) {
                    imageUrls.add(node.getString("display_src"));
                    imageUrlsThumbnail.add(node.getString("thumbnail_src"));

                }

            }

            return new ObjectClass(imageUrls, imageUrlsThumbnail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}


