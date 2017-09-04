package com.instadownloader.instadpdownloader.Notifications;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


/**
 * Created by Ayush on 26/8/2017.
 */

public class GCMTokenRefreshListenerService extends InstanceIDListenerService {

    //If the token is changed registering the device again
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
