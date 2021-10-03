package com.firstapp.atmos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firstapp.atmos.Networking.WeatherDataService;
import com.firstapp.atmos.Networking.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class Weather_Upcoming_Days extends Fragment {

    private ListView list;
    private ImageView imageView;
    private LocationModelClass location;

    public Weather_Upcoming_Days(LocationModelClass location) {
        // Required empty public constructor
        this.location = location;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_weather__upcoming__days, container, false);

        list = (ListView) rootView.findViewById(R.id.list_view_seven_days);
        imageView = (ImageView) rootView.findViewById(R.id.custom_layout_image_view);

        fetchList(this.location);

        return rootView;
    }

    private void fetchList(LocationModelClass location){
                        WeatherDataService weatherDataService = new WeatherDataService(getActivity());
                        weatherDataService.getWeatherList(location.getLatitude(), location.getLongitude(), new WeatherDataService.ForecastResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getActivity(), "Cannot get weather", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherModel> weatherModelData) {
                                ForecastAdapter adapter = new ForecastAdapter(getActivity(), weatherModelData);
                                list.setAdapter(adapter);
                            }
                        });
                    }
}

