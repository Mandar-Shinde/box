package com.elekso.potfix.fragment;

import android.os.Bundle;

import com.elekso.potfix.R;
import com.elekso.potfix.database.DBDataSource;
import com.elekso.potfix.model.PotfixModel;
import com.elekso.potfix.utils.EventManager;
import com.elekso.potfix.utils.Globals;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<PotfixModel> potholeList;
    private Bus eventBus;
    private boolean isZoomLevelSet;
    int pos=50;
    boolean isfirst=true;


    @Subscribe
    public void getMessage(PotfixModel mod)
    {
      //  Toast.makeText(getContext(), "got data gor map", Toast.LENGTH_SHORT).show();
        LatLng latlon = new LatLng(mod.getLatitude(),mod.getLongitude());

        mMap=this.getMap();
        if(mMap!=null && mod.isBump()==true) {
            if(mod.getaccuracy()>90)
                mMap.addMarker(new MarkerOptions().position(latlon).title("90% pothole").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_red)));
            if(mod.getaccuracy()>50 && mod.getaccuracy()<=90 )
                mMap.addMarker(new MarkerOptions().position(latlon).title("50% pothole").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_green)));
            if(mod.getaccuracy()>5 &&  mod.getaccuracy()<=50 )
                mMap.addMarker(new MarkerOptions().position(latlon).title("20% pothole").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_blue)));
            if(mod.getaccuracy()<6)
                mMap.addMarker(new MarkerOptions().position(latlon).title("20% pothole").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_yellow)));


            // mMap.moveCamera(CameraUpdateFactory.newLatLng(latlon));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 18));
        }
        else
        {
            if(Globals.getInstance().getFlexiblemap()) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 18));
            }
        }
//        pos++;
//        if(pos>5) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 18));
//            pos=0;
//        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//       mMap=getMap();

//        Object obj = getArguments().get(PotfixModel.class.getSimpleName());
//        if(obj != null) {
//            potholeList = (List<PotfixModel>) obj;
//        }
//
//        if(potholeList == null) {
//            potholeList = new ArrayList<PotfixModel>();
//        }
//
        eventBus = EventManager.getInstance();


         getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(MapsFragment.this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(MapsFragment.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

//        // configuring google map
//        googleMap.setTrafficEnabled(true);
//        googleMap.setBuildingsEnabled(true);
        googleMap.setMyLocationEnabled(true);


      //  googleMap.addMarker(new MarkerOptions().position(new LatLng(18, 72)).title("Marker"));
//
//        // configuring UI controls and gesture
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
//        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);


        // Load old marker
        // Init Database
        if(isfirst==true) {
            for (PotfixModel p : DBDataSource.getInstance().updatePothole()) {
                getMessage(p);
            }
            ;
            isfirst=false;
        }

//
//        this.mMap = googleMap;
//
//        List<MarkerOptions> optionsList = buildMarkerOptions();
//        bindMarkersToMap(optionsList);
//       // bindClusterManager();

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private List<MarkerOptions> buildMarkerOptions() {
        List<MarkerOptions> optionsList = new ArrayList<MarkerOptions>();
        for(PotfixModel gForce : potholeList){
        //    MarkerOptions options = MapUtil.buildMarkerOption(gForce);
//            if(options != null) {
//                optionsList.add(options);
//            }
        }
        return optionsList;
    }

    private void bindMarkersToMap(List<MarkerOptions> optionsList) {
        if(optionsList == null){
            return;
        }

        for (MarkerOptions options : optionsList) {
            mMap.addMarker(options);
        }
    }

//    private ClusterManager<MapUtil.MyClusterItem> bindClusterManager() {
//        ClusterManager<MapUtil.MyClusterItem> clusterManager = new ClusterManager<MapUtil.MyClusterItem>(getContext(), googleMap);
//        for(GForce gForce : gForceList){
//            clusterManager.addItem(new MapUtil.MyClusterItem(gForce.getLatitude(), gForce.getLongitude()));
//        }
//        return clusterManager;
//    }


    private boolean isDrawMarkerOnMap(PotfixModel gForce) {
        if(potholeList.contains(gForce)){
            // marker is already drawn
            return false;
        }

        potholeList.add(gForce);
        return true;
    }



}
