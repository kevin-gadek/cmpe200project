package com.proj.cmpe200.sjsu.weatherapp;

import com.proj.cmpe200.sjsu.weatherapp.model.WeatherModel;

import java.util.HashMap;
import java.util.List;

public class WeatherForecastContainer {
    public final static String TAG = "WeatherForecastContainer";

    private static WeatherForecastContainer instance;
    public final static long REQUEST_WEATHER_FIVE_MINUTES = 300000;

    public final static long REQUEST_WEATH_30_MINS = REQUEST_WEATHER_FIVE_MINUTES*6;

    private HashMap<String, WeatherForecastPackage> mfHashMap;

    private WeatherForecastContainer(){
        mfHashMap = new HashMap<>();
    }

    public static synchronized WeatherForecastContainer getInstance(){
        if(instance == null){
            instance = new WeatherForecastContainer();
        }

        return instance;
    }

    public void put(String location, List<WeatherModel> mModels){
        WeatherForecastPackage weatherForecastPackage = new WeatherForecastPackage(location);
        weatherForecastPackage.models = mModels;
        mfHashMap.put(location, weatherForecastPackage);

    }

    public void replace(String location, List<WeatherModel> models){
        mfHashMap.get(location).models = models;
    }

    public List<WeatherModel> getWeatherModels(String location){
       return mfHashMap.get(location).models;
    }

    public boolean shouldRequestFetchWeather(String location){


        if(mfHashMap.get(location) == null){
            return false;
        }

        long timestamp = mfHashMap.get(location).mTimestamp;

        if(System.currentTimeMillis() - timestamp >= REQUEST_WEATH_30_MINS){
            return true;
        }

        return false;
    }

    public void remove(String location){
        mfHashMap.remove(location);
    }

    class WeatherForecastPackage{

        public String mLocation;
        public long mTimestamp;
        public List<WeatherModel> models;

        public WeatherForecastPackage(String location){
            mLocation = location;
            mTimestamp = System.currentTimeMillis();
        }
    }
}
