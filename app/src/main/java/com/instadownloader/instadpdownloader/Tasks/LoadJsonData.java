package com.instadownloader.instadpdownloader.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.instadownloader.instadpdownloader.Activity.ImageDisplayActivity;
import com.instadownloader.instadpdownloader.BuildConfig;
import com.instadownloader.instadpdownloader.Utils.Constants;
import com.instadownloader.instadpdownloader.Utils.LogWrapper;
import com.instadownloader.instadpdownloader.Utils.ObjectClass;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Created by ayush on 29/8/17.
 */

public class LoadJsonData extends AsyncTask<Void, Void, ObjectClass> {

    private String userName;
    private boolean isSuccessful;
    private ProgressDialog mProgressDialog;
    private short action;
    private Activity activity;
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
    public LoadJsonData(Activity activity, String userName, short action) {

        this.userName = userName;
        this.activity = activity;
        this.action = action;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Rukk bhai");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
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
            LogWrapper.d(activity.getApplicationContext(), "Tag", imageUrl);
            isSuccessful = true;
            return new ObjectClass(imageUrl, isPrivate);
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            isSuccessful = false;
        }
        return null;

    }

    @Override
    protected void onPostExecute(ObjectClass objectClass) {
        super.onPostExecute(objectClass);


        try {
            mProgressDialog.dismiss();
            if (objectClass != null) {
                Intent intent;
                switch (action) {
                    case Constants.ACTION_DOWNLOAD_DP:
                        intent = new Intent(activity, ImageDisplayActivity.class);
                        intent.putExtra(Constants.BUNDLE_IMAGE_URL, (String) objectClass.getObject1());
                        activity.startActivity(intent);

                        break;
                    case Constants.ACTION_VIEW_PHOTOS:
                        boolean isPrivate= (Boolean) objectClass.getObject2();
                        if(isPrivate)
                        {
                            final InstagramApp instagramApp=new InstagramApp(activity, BuildConfig.CLIENT_ID,BuildConfig.CLIENT_SECRET,BuildConfig.CALLBACK_URL);
                            instagramApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

                                @Override
                                public void onSuccess() {
                                      instagramApp.fetchUserName(handler);
                                }

                                @Override
                                public void onFail(String error) {

                                }
                            });
                        instagramApp.authorize();
                        }
                        else {
                            intent = new Intent(activity, ImageDisplayActivity.class);
                            intent.putExtra("imageUrl", (Boolean) objectClass.getObject2());
                            activity.startActivity(intent);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
//        Toast.makeText(activity.getApplicationContext(), "Failed Choosing Branch", Toast.LENGTH_LONG).show();
//        Utils.sessionExpired(activity);
        }
    }

}


