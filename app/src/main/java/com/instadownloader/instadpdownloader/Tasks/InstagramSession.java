package com.instadownloader.instadpdownloader.Tasks;

import android.content.Context;

import com.instadownloader.instadpdownloader.Utils.PreferencesConstants;
import com.instadownloader.instadpdownloader.Utils.SharedPreferencesWrapper;

/**
 * Created by ayush on 17/1/17.
 */

public class InstagramSession {

    private SharedPreferencesWrapper sharedPreferencesWrapper;

    public InstagramSession(Context context) {
        sharedPreferencesWrapper = new SharedPreferencesWrapper(context);
    }

    public void storeAccessToken(String accessToken, String id, String username, String name) {
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ID, id);
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_USERNAME, name);
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ACCESS_TOKEN, accessToken);
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_USERNAME, username);
        sharedPreferencesWrapper.commit();
    }

    public void storeAccessToken(String accessToken) {
        sharedPreferencesWrapper.putSingleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ACCESS_TOKEN, accessToken);
    }

    /**
     * Reset access token and user name
     */
    public void resetAccessToken() {
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ID, null);
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_USERNAME, null);
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ACCESS_TOKEN, null);
        sharedPreferencesWrapper.putBundleData(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_USERNAME, null);
        sharedPreferencesWrapper.commit();
    }

    /**
     * Get user name
     *
     * @return User name
     */
    public String getUsername() {
        return sharedPreferencesWrapper.getString(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_USERNAME, null);
    }

    /**
     * @return
     */
    public String getId() {
        return sharedPreferencesWrapper.getString(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ID, null);
    }

    /**
     * @return
     */
    public String getName() {
        return sharedPreferencesWrapper.getString(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_NAME, null);
    }

    /**
     * Get access token
     *
     * @return Access token
     */
    public String getAccessToken() {
        return sharedPreferencesWrapper.getString(PreferencesConstants.SHARED_PREF_API_LOGIN, PreferencesConstants.SHARED_PREF_API_LOGIN_ACCESS_TOKEN, null);
    }

}