package com.firstapp.atmos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.atmos.Networking.WeatherDataService;
import com.firstapp.atmos.Networking.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class ForecastAdapter extends ArrayAdapter<WeatherModel> {

    public ForecastAdapter(@NonNull Context context, List<WeatherModel> weatherModelArrayList) {
        super(context, 0, weatherModelArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_layout, parent, false);
        }

        WeatherModel currentWeatherData = getItem(position);
        //setting the value of date
        TextView tv_date = (TextView) v.findViewById(R.id.custom_layout_date);
        long date = currentWeatherData.get_date();
        tv_date.setText(dateConversion(date));

        //setting max temp
        TextView tv_max_temp = (TextView) v.findViewById(R.id.custom_layout_max);
        long max_temp = currentWeatherData.getTemp_max();
        tv_max_temp.setText((String.valueOf(max_temp) + "°"));

        //setting the min temp
        TextView tv_min_temp = (TextView) v.findViewById(R.id.custom_layout_min);
        long min_temp = currentWeatherData.getTemp_min();
        tv_min_temp.setText((String.valueOf(min_temp) + "°"));

        TextView tv_weather_status = (TextView) v.findViewById(R.id.custom_layout_weather_desc);
        tv_weather_status.setText(currentWeatherData.getMain_weather_status());

        ImageView imageView = (ImageView) v.findViewById(R.id.custom_layout_image_view);
        long condition = currentWeatherData.get_condition();
        imageView.setImageResource(getWeatherIcon(condition));

        return v;
    }
    private int getWeatherIcon(long condition){
        if(condition >= 200 && condition <= 232){
            return R.drawable.icon_thunderstorm;
        }
        else if (condition >= 300 && condition <= 531){
            return R.drawable.icon_rain_shower;
        }
        else if (condition >= 600 && condition <= 622 ){
            return R.drawable.icon_snow;
        }
        else if(condition >= 701 && condition <= 781){
            return R.drawable.icon_mist;
        }
        else if(condition == 800){
           return R.drawable.icon_clear_sky;
        }
        else if (condition>= 801 && condition <= 804 ){
            return R.drawable.icon_scattered_clouds;
        }
        return 1;
    }

    private String dateConversion(long date){
        Date unix_date = new Date(date * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd");
        return simpleDateFormat.format(unix_date);
    }
}