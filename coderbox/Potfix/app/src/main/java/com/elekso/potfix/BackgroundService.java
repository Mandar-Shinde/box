package com.elekso.potfix;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.elekso.potfix.database.DBDataSource;
import com.elekso.potfix.model.PotfixModel;
import com.elekso.potfix.model.SavedPothole;
import com.elekso.potfix.utils.EventManager;
import com.elekso.potfix.utils.Globals;
import com.elekso.potfix.utils.Logger;
import com.squareup.otto.Bus;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    Timer timer;
    final Handler handler = new Handler();
    static  double old_lat;
    static  double old_lon;
    public static double distbtw;
    Bus bus = new Bus();

    public static  boolean isfirst=true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Init Sensor
        SensorLooper.getInstance(this).init();

        // Init Database
        DBDataSource.getInstance(this).init();

        // Log
        Logger.getInstance();


        //wl.release();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        PotfixModel mod=SensorLooper.getInstance().getUpdate();
                            float diff = Filter.getInstance().Diff( mod.getSensor(), FilterType.Z_AXIS);
                        if(old_lat>0 ||old_lon>0)
                            distbtw =distance(old_lat,old_lon,mod.getLatitude(),mod.getLongitude());
                        else
                            distbtw =0;

                            Log.d("Pot", "diff:"
                                    + String.format("%.3f", diff)
                                    + " | " + String.format("%.7f", mod.getLatitude())
                                    + " | " + String.format("%.7f", mod.getLongitude())
                                    + " | " + String.format("%.3f", mod.getaccuracy())
                                    + " | dist " + distbtw);
                            Logger.getInstance().appendLog( "," + mod.getLatitude() + "," + mod.getLongitude()+"," + String.format ("%.3f", diff)  );

                        if(isfirst)
                            distbtw=8;
                            if ((diff > 2 )  && (mod.getLongitude()>0.0 && mod.getLatitude()>0.0) )//&& distbtw > Globals.getInstance().getIgnoreDiameter()) //&& ((Math.abs(old_lat - mod.getLatitude())>0.000001)||(Math.abs(old_lon - mod.getLongitude())>0.000001) ))
                            {
                                mod.setIsBump(true);
                                DBDataSource.getInstance().savePotholeDB(mod.getLatitude(), mod.getLongitude());
                                SavedPothole.getInstance().addpothole(mod.getLatitude(), mod.getLongitude());
                                old_lat=mod.getLatitude();
                                old_lon=mod.getLongitude();
                                isfirst=false;
                            }
                        EventManager.getInstance().post(mod);

                    }
                });
            }
        }, 1000, 1000);
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        timer.purge();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
