package com.firstapp.atmos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.atmos.Networking.WeatherDataService;
import com.firstapp.atmos.Networking.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    //constants
    private static final int REQUEST_CODE = 101;
    private static final String TAG = "MainActivity";

    //Views, layouts and other classes
    private TextView currentLoc;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews();
        checkNetworkConnection();
        swipeDownToRefresh();
    }

    //finds all the views for Main Activity
    private void findAllViews(){
        currentLoc = findViewById(R.id.id_current_location);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        swipeRefreshLayout = findViewById(R.id.id_main_activity_swipe_to_refresh);
    }

    //Method to check if the internet connection is available
    public void checkNetworkConnection(){
        CheckInternetConnection check = new CheckInternetConnection(this);
        boolean trfal = check.isNetworkAvailable(this);
        Log.d(TAG, "checkNetworkConnection: " + trfal);
        if(check.isNetworkAvailable(this)){
            getLocation();
            viewPager2.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

        }else{
            viewPager2.setVisibility(View.INVISIBLE);
            tabLayout.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().add(R.id.id_main_activity_constraint_layout, new NoConnectionFragment(this)).addToBackStack("noConn").commit();
        }
    }

    //Gets the current user location when called by checkNetworkConnection() method
    private void getLocation(){

        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(MainActivity.this);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    getSuccessfulLocation(location);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Cannot fetch current loaction", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //gets called by getLocation() method on location success
    public void getSuccessfulLocation(Location loc){

        if(loc!=null){
            //variables
            String lat = String.valueOf(loc.getLatitude());
            String lon = String.valueOf(loc.getLongitude());

            LocationModelClass location = new LocationModelClass(lat, lon);

            WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

            weatherDataService.getCurrentLocation(location.getLatitude(), location.getLongitude(), new WeatherDataService.LocationResponseListener() {
               @Override
               public void onError(String message) {
                   //Display error message
               }

               @Override
               public void onResponse(String current_loc) {
                    currentLoc.setText(current_loc);
               }
           });

            //Fragments
            Weather_Today weather_today = new Weather_Today(location);
            Weather_Upcoming_Days weather_upcoming_days = new Weather_Upcoming_Days(location);

            viewPager2.setAdapter(new PageAdapter(this, weather_today, weather_upcoming_days));

            attachTabLayout();

        }

    }

    //Method to attach "Today" and "8 days" tabs to the Main Activity layout
    private void attachTabLayout() {

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Today");
                        break;
                    case 1:
                        tab.setText("8 days");
                        break;

                }
            }
        });
        tabLayoutMediator.attach();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        if(viewPager2.getCurrentItem() == 0){
            super.onBackPressed();
            finish();
        }
        else{
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()-1);
        }

    }

    //Always check network in started state
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        checkNetworkConnection();
    }

    //Always check network in resumed state
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        checkNetworkConnection();
    }

    //Always check network when swiped down to refresh
    private void swipeDownToRefresh(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkNetworkConnection();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}