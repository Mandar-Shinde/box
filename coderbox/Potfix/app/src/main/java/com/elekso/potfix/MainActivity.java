package com.elekso.potfix;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.elekso.potfix.fragment.LicenseFragment;
import com.elekso.potfix.fragment.MapsFragment;
import com.elekso.potfix.fragment.ProfileFragment;
import com.elekso.potfix.fragment.ShareFragment;
import com.elekso.potfix.utils.Config;
import com.elekso.potfix.utils.Globals;
import com.elekso.potfix.utils.LoginActivity;
import com.google.android.gms.maps.GoogleMap;

import java.text.DateFormat;
import java.util.Date;

//import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    FloatingActionButton fab;
    DrawerLayout drawer;
    short currentFragment=2;

    TextView tusername;
    TextView tuseremail;
    public static String globaldata_test="hit";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PowerManager pm = (PowerManager)getApplicationContext().getSystemService( getApplicationContext().POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, " POTFIX holding wake lock");
        wl.acquire();
        Globals.getInstance().setFlexiblemap(true);



//
//
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                try {
//                    LALpotfixservicePortBinding service = new LALpotfixservicePortBinding();
//                    try {
//                        globaldata_test=service.CheckWS("mandar");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }).start();

        String login="";



      //  Config.getInstance(getBaseContext(),getCacheDir()).setProfile("df","asd");
        login=Config.getInstance(getBaseContext(),getCacheDir()).getProfileName();
        if(login ==null || login.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        //remove
      //  Stetho.initializeWithDefaults(this);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        // Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();




        tusername =(TextView) drawer.findViewById(R.id.tvusername);
//        tuseremail =(TextView) findViewById(R.id.tvuseremail);
//
//        if(login!=null)
//            tusername.setText("jhjhjhjh");
        //   if(Config.getInstance().getProfileEmail()!=null)
        //tuseremail.setText(Config.getInstance().getProfileEmail());

        // FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setRippleColor(Color.parseColor("#78D6F3"));
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#039FDC")));
        fab.setImageResource(R.drawable.ic_gps_fixed_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentFragment) {
                    case 1: //profile
                        Snackbar.make(view, "Updating Information", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Fragment frg = new ProfileFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_containerone, frg);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.commit();
                        break;
                    case 2: //map

                        if(Globals.getInstance().getFlexiblemap()) {
                            Snackbar.make(view, "Free to Scroll", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Globals.getInstance().setFlexiblemap(false);
                            fab.setRippleColor(Color.parseColor("#FFE082"));
                            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB300")));
                            fab.setImageResource(R.drawable.ic_gps_off_white_24dp);
                        }
                        else
                        {
                            Snackbar.make(view, "Follow Potfix", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Globals.getInstance().setFlexiblemap(true);
                            fab.setRippleColor(Color.parseColor("#78D6F3"));
                            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#039FDC")));
                            fab.setImageResource(R.drawable.ic_gps_fixed_white_24dp);
                        }
                        break;
                    case 3: //share
                        //Snackbar.make(view, "Some sharing action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        String[] TO = {"aziz@potfix.com"};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Potfix Communication");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message..." );

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                        }
                        catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4: //legal
                        Snackbar.make(view, "Software License", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        drawer.openDrawer(Gravity.LEFT);
                        break;
                }

            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startService(new Intent(getBaseContext(), BackgroundService.class));

        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        MapsFragment mFRaFragment = new MapsFragment();
        mTransaction.add(R.id.frame_containerone, mFRaFragment);
        mTransaction.commit();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }

//        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//            Toast.makeText(this, "Network is Enabled in your devide", Toast.LENGTH_SHORT).show();
//        }else{
//            showNetDisabledAlertToUser();
//        }
        createNotification();
    }

    private void createNotification() {
        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getResources().getString(R.string.custom_notification));

        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.icon4_1);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.textView, text);

        /* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView =
                    new RemoteViews(getPackageName(), R.layout.notification_expanded);
            notification.bigContentView = expandedView;
        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }


    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Internet & GPS is required by Potfix. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showNetDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Internet will improve accuracy. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable Internet",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_DATA_ROAMING_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment newFragment=null;

        if (id == R.id.nav_profile) {
            currentFragment=1;
            newFragment = new ProfileFragment();
            fab.setRippleColor(Color.parseColor("#99ff66"));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00CC33")));
            fab.setImageResource(R.drawable.ic_autorenew_white_36dp);

        } else if (id == R.id.nav_map) {
            currentFragment=2;
            newFragment = new MapsFragment();
            fab.setRippleColor(Color.parseColor("#78D6F3"));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#039FDC")));
            fab.setImageResource(R.drawable.ic_gps_fixed_white_24dp);

//        } else if (id == R.id.nav_news) {
//            newFragment = new NewsFragment();
//            fab.setImageResource(android.R.drawable.ic_dialog_info);
//        } else if (id == R.id.nav_setting) {
//            newFragment = new SettingFragment();
//            fab.setImageResource(R.drawable.ic_settings_white_24dp);
        } else if (id == R.id.nav_share) {
            currentFragment=3;
            newFragment = new ShareFragment();
            fab.setRippleColor(Color.parseColor("#FF7F50"));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2400")));
            fab.setImageResource(android.R.drawable.ic_dialog_email);
        } else if (id == R.id.nav_license) {
            currentFragment=4;
            newFragment = new LicenseFragment();
            fab.setRippleColor(Color.parseColor("#FFFFFF"));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            fab.setImageResource(R.drawable.ic_done_white_24dp);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_containerone, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
      //  transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }
}
