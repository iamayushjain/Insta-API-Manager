package com.instadownloader.instadpdownloader.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.instadownloader.instadpdownloader.OnInstaDisplayDpService;
import com.instadownloader.instadpdownloader.Utils.LogWrapper;
import com.instadownloader.instadpdownloader.Utils.PreferencesConstants;
import com.instadownloader.instadpdownloader.Utils.SharedPreferencesWrapper;
import com.instadownloader.instadpdownloader.Utils.Utils;

/**
 * Created by ayush on 23/1/17..
 * //
 */
public class BroadcastBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            try {
                if (SharedPreferencesWrapper.getBoolean(context,
                        PreferencesConstants.SHARED_PREF_UTILS,
                        PreferencesConstants.SHARED_PREF_ANYWHERE_IMAGE_FIND, true)
                        && Utils.isServiceRunning(context, OnInstaDisplayDpService.class)) {
                    initiateService(context);

                }
            } catch (Exception e) {
                LogWrapper.printStackTrace(e);
            }
        }
    }

    private void initiateService(Context context) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {
                context.startService(new Intent(context, OnInstaDisplayDpService.class)
                        .addCategory(OnInstaDisplayDpService.TAG));
            }
        } else {
            context.startService(new Intent(context, OnInstaDisplayDpService.class)
                    .addCategory(OnInstaDisplayDpService.TAG));
        }
    }
}
