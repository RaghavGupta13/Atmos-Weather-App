package com.firstapp.atmos.Networking;

import android.content.Context;
import android.net.sip.SipSession;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firstapp.atmos.Conversions;
import com.firstapp.atmos.Weather_Today;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherDataService {

    //common constants for both the APIs
    private static final String API_KEY = "c3e995acb2eb52cf6d77cd5f6244471b";
    private static final String UNITS_METRIC = "&units=metric";
    private static final String UNITS_IMPERIAL = "&units=imperial";
    private static final String API_KEY_STRING = "&appid="+ API_KEY;
    private final Context context;

    //declaring constants for first API
    private static final String QUERY_FOR_LAT = "https://api.openweathermap.org/data/2.5/weather?lat=";
    private static final String QUERY_FOR_LON = "&lon=";

    private String current_location = "";

    //declaring the required constants for second api
    private static final String QUERY_FOR_LAT2 = "https://api.openweathermap.org/data/2.5/onecall?lat=";
    private static final String QUERY_FOR_LON2 = "&lon=";
    private static final String API_EXCLUSIONS = "&exclude=hourly,minutely,alerts,current";

    public WeatherDataService(Context context){
        this.context = context;
    }

    //Interface for network callback for first API
    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(WeatherModel weatherModelData);
    }

    //Get weather according to current location through first API
    public void getCurrentLocationWeather(String latitude, String longitude, final VolleyResponseListener volleyResponseListener){
        String url1 = QUERY_FOR_LAT + latitude + QUERY_FOR_LON + longitude + API_KEY_STRING + UNITS_METRIC;
        //String url2 = QUERY_FOR_LAT + latitude + QUERY_FOR_LON + longitude + API_KEY_STRING + UNITS_IMPERIAL;
        Log.i("Api", url1);


        final JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray weatherStatus = response.getJSONArray("weather");
                    JSONObject getDescription = weatherStatus.getJSONObject(0);
                    long condition_id = getDescription.getLong("id");
                    String icon_id = getDescription.getString("icon");
                    JSONObject temperatures = response.getJSONObject("main");
                    long json_date_time = response.getLong("dt");

                    WeatherModel weatherModel = new WeatherModel(
                            getDescription.getString("main"),
                            temperatures.getLong("temp"),
                            temperatures.getLong("feels_like"),
                            Conversions.dateConversion(json_date_time),
                            Conversions.timeConversionToHour(json_date_time),
                            getDescription.getLong("id"));


                    volleyResponseListener.onResponse(weatherModel);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request1);

    }


    //Interface for network callback for second API
    public interface ForecastResponseListener{
        void onError(String message);

        void onResponse(List<WeatherModel> weatherModelData);
    }

    //Get weather list according to current location through second API
    public void getWeatherList(String latitude, String longitude, final ForecastResponseListener forecastResponseListener){
        String url2 = QUERY_FOR_LAT2 + latitude + QUERY_FOR_LON2 + longitude + API_EXCLUSIONS + API_KEY_STRING + UNITS_METRIC;

        final List<WeatherModel> report = new ArrayList<>();

        final JsonObjectRequest request2  = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray getWeatherArray = response.getJSONArray("daily");

                    for(int i=0; i< getWeatherArray.length(); i++){
                        JSONObject firstDay = getWeatherArray.getJSONObject(i);
                        long date = firstDay.getLong("dt");

                        JSONObject temperature = firstDay.getJSONObject("temp");
                        JSONArray weather_details = firstDay.getJSONArray("weather");
                        JSONObject weather = weather_details.getJSONObject(0);

                        WeatherModel weatherModel = new WeatherModel(weather.getString("main"),
                                temperature.getLong("min"),
                                temperature.getLong("max"),
                                weather.getInt("id"),
                                firstDay.getLong("dt"));

                        report.add(weatherModel);
                    }
                    forecastResponseListener.onResponse(report);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request2);
    }

    //Interface for network callback using first API to get current city and country
    public interface LocationResponseListener{
        void onError(String message);

        void onResponse(String current_loc);
    }

    //Method to get current city and country using first API
    public String getCurrentLocation(String latitude, String longitude, final LocationResponseListener locationResponseListener){
        String url3 = QUERY_FOR_LAT + latitude + QUERY_FOR_LON + longitude + API_KEY_STRING + UNITS_METRIC;

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject country = response.getJSONObject("sys");
                    String cityName = response.getString("name");

                    WeatherModel weatherModel = new WeatherModel(country.getString("country"),
                            cityName);

                  current_location = weatherModel.getCity_name() + "," + weatherModel.getCountry_name();

                  locationResponseListener.onResponse(current_location);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request3);
        return current_location;
    }


}
