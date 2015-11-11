package com.elekso.potfix.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;


public class Utility {


    public static boolean isTablet(Context appContext) {
        return (appContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context appContext) {
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}

