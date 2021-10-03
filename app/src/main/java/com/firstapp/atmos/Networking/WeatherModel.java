package com.firstapp.atmos.Networking;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherModel {
    private String main_weather_status;
    private String icon;

    private long temp_current;
    private long temp_feels_like;
    private long temp_min;
    private long temp_max;
    private int time;
    private long condition;
    private String date_time;
    private long date;
    private String country_name;
    private String city_name;


    public WeatherModel(String main_weather_status, long temp_min, long temp_max, long condition, long date) {
        this.main_weather_status = main_weather_status;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.condition = condition;
        this.date = date;
    }

    public WeatherModel(String main_weather_status, long temp_current, long temp_feels_like, String date_time, int time, long condition) {
        this.main_weather_status = main_weather_status;
        this.icon = icon;
        this.temp_current = temp_current;
        this.temp_feels_like = temp_feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.date_time = date_time;
        this.time = time;
        this.condition = condition;
    }

    public WeatherModel(String country_name, String city_name){
        this.city_name = city_name;
        this.country_name = country_name;
    }

    @Override
    public String toString() {
        return "Code: " + condition;

    }

    public String getMain_weather_status() {
        return main_weather_status;
    }

    public void setMain_weather_status(String main_weather_status) {
        this.main_weather_status = main_weather_status;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTemp_current() {
        return temp_current;
    }

    public void setTemp_current(long temp_current) {
        this.temp_current = temp_current;
    }

    public long getTemp_feels_like() {
        return temp_feels_like;
    }

    public void setTemp_feels_like(long temp_feels_like) {
        this.temp_feels_like = temp_feels_like;
    }

    public long getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(long temp_min) {
        this.temp_min = temp_min;
    }

    public long getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(long temp_max) {
        this.temp_max = temp_max;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int get_time() {
        return time;
    }

    public void set_time(int time) {
        this.time = time;
    }

    public long get_condition() {
        return condition;
    }

    public void set_condition(long condition) {
        this.condition = condition;
    }

    public long get_date() {
        return date;
    }

    public void set_date(long date) {
        this.date = date;
    }
}
