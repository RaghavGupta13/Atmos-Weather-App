package com.firstapp.atmos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.atmos.Networking.WeatherDataService;
import com.firstapp.atmos.Networking.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.content.ContentValues.TAG;


public class Weather_Today extends Fragment {

    //views and classes
    private TextView tv_date_time, tv_current_temp, tv_feels_like_temp, tv_weather_desc;
    private ConstraintLayout weather_today_layout_visibility, loading_page_visiblity;
    private ImageView weather_icon_view;
    private WeatherDataService weatherDataService;
    private LocationModelClass location;

    //constants
    private static final String TAG = "Weather_Today";
    private static final int REQUEST_CODE = 101;

    public Weather_Today(LocationModelClass location) {
        this.location = location;
    }

    public Weather_Today() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather__today, container, false);

        tv_current_temp = (TextView) rootView.findViewById(R.id.tv_current_temperature);
        tv_date_time = (TextView) rootView.findViewById(R.id.tv_date_time);
        tv_feels_like_temp = (TextView) rootView.findViewById(R.id.tv_feels_like_temperature);
        tv_weather_desc = (TextView) rootView.findViewById(R.id.tv_weather_description);
        weather_icon_view = (ImageView) rootView.findViewById(R.id.iv_weather_icon);
        weather_today_layout_visibility = (ConstraintLayout) rootView.findViewById(R.id.weather_today_cons_layout);
        loading_page_visiblity = rootView.findViewById(R.id.id_layout_loading_page);

        weatherDataService = new WeatherDataService(getActivity());

        weather_today_layout_visibility.setVisibility(View.INVISIBLE);
        loading_page_visiblity.setVisibility(View.VISIBLE);

        fetchWeather(this.location);

        return rootView;
    }

    //Method which uses weather data service to fetch weather of current location
    public void fetchWeather(LocationModelClass location) {
        
    if (location != null){

        weatherDataService.getCurrentLocationWeather(location.getLatitude(), location.getLongitude(), new WeatherDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), "Cannot get current weather", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WeatherModel weatherModelData) {
                loading_page_visiblity.setVisibility(View.INVISIBLE);
                weather_today_layout_visibility.setVisibility(View.VISIBLE);
                tv_current_temp.setText(String.valueOf(weatherModelData.getTemp_current()));
                tv_feels_like_temp.setText(String.valueOf(weatherModelData.getTemp_feels_like()) + " °C");
                tv_date_time.setText(String.valueOf(weatherModelData.getDate_time()));
                tv_weather_desc.setText(weatherModelData.getMain_weather_status());

                // t gives the time of the day and accordingly different images are set
                int t = weatherModelData.get_time();

                if(t>=6 && t<=15){

                    weather_today_layout_visibility.setBackgroundResource(R.drawable.morning);

                }else if(t>=16 && t<=19){

                    weather_today_layout_visibility.setBackgroundResource(R.drawable.evening);

                }else{
                    weather_today_layout_visibility.setBackgroundResource(R.drawable.night);
                }

                if(weatherModelData.get_condition() >= 200 && weatherModelData.get_condition() <= 232 ){
                    weather_icon_view.setImageResource(R.drawable.icon_thunderstorm);
                }

                else if(weatherModelData.get_condition() >= 300 && weatherModelData.get_condition() <= 531 ){
                    weather_icon_view.setImageResource(R.drawable.icon_rain_shower);
                }

                else if(weatherModelData.get_condition() >= 600 && weatherModelData.get_condition() <= 622 ){
                    weather_icon_view.setImageResource(R.drawable.icon_snow);
                }

                else if(weatherModelData.get_condition() >= 701 && weatherModelData.get_condition() <= 781 ){
                    weather_icon_view.setImageResource(R.drawable.icon_mist);
                }

                else if(weatherModelData.get_condition() == 800 ){
                    if(t >= 6 && t <=19){
                        weather_icon_view.setImageResource(R.drawable.icon_clear_sky);
                    }else{
                        weather_icon_view.setImageResource(R.drawable.clear_night_sky);
                    }

                }

                else if(weatherModelData.get_condition() >= 801 && weatherModelData.get_condition() <= 804 ){
                    weather_icon_view.setImageResource(R.drawable.icon_scattered_clouds);
                }


            }
        });
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Please allow permission to fetch location", Toast.LENGTH_SHORT).show();
            }

        }
    }


}

