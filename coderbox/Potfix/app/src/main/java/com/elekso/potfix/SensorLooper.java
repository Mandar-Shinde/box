package com.elekso.potfix;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.elekso.potfix.model.PotfixModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;


//
// LOCATION AND SENSOR LISTNER
//
public class SensorLooper implements SensorEventListener,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static int UPDATE_INTERVAL = 1000;
    private static int FATEST_INTERVAL = 500;
    private static int DISPLACEMENT = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private Location loc;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Sensor accelerometerSensor;
    private static SensorManager sensorManager;
    private static Context context;


    public static boolean isGPSEnabled;
    public static boolean isNetworkEnabled = true;
    public static Location location;
    public static double latitude = 0;
    public static double longitude = 0;
    private static LocationManager locationManager;
    private static Criteria criteria;
    private static String slocationprovider;
    public static float[] eventdata = {0, 0, 0, 0};

    private static SensorLooper sensorInstance = new SensorLooper();

    private SensorLooper() {

    }

    public static SensorLooper getInstance(Context c) {
        sensorInstance.context = c;
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            getCurrentLocation();
        }


        return sensorInstance;
    }

    public static SensorLooper getInstance() {

        return sensorInstance;
    }


    public void init() {


        if(checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();

            if(mGoogleApiClient != null) {
                mGoogleApiClient.connect();

            }

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //GooglePlayServicesUtil.getErrorDialog(resultCode,context.getApplicationContext() , PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    public static void getCurrentLocation() {


        //----------------------------------------------------------
        // Get the location manager
        //----------------------------------------------------------
//        try {
//
//            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//            //getting GPS status
//            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            //getting network status
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//
//            } else {
//                if (isNetworkEnabled) {
//                    if (locationManager != null) {
//                        //if (checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                        {
//                            // TODO: Consider calling
//                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for Activity#requestPermissions for more details.
//                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                            return;
//                        }
//
//                    }
//                }
//
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        if (locationManager != null) {
//                          //  if (checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                           {
//                                {
//                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        }catch(Exception e){
//
//        }
    }

    public PotfixModel getUpdate()
    {
        PotfixModel pm=new PotfixModel();
        getCurrentLocation();
        if(location != null) {
            pm.setLatitude(location.getLatitude());
            pm.setLongitude(location.getLongitude());
            pm.setaccuracy(location.getAccuracy());
        }
        pm.setSensor(eventdata[0],eventdata[1],eventdata[2]);
        return pm;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        eventdata[0] = event.values[0];
        eventdata[1] = event.values[1];
        eventdata[2] = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onLocationChanged(Location lc) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null ) {
           location=lc;
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}