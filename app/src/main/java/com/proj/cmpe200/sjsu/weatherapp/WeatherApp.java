package com.proj.cmpe200.sjsu.weatherapp;

import android.app.Application;
import android.location.Address;

import com.proj.cmpe200.sjsu.weatherapp.service.LocationService;
import com.proj.cmpe200.sjsu.weatherapp.service.VolleyNetworkService;
import com.proj.cmpe200.sjsu.weatherapp.util.Configurations;
import com.proj.cmpe200.sjsu.weatherapp.util.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class WeatherApp extends Application {

    public static final String TAG = "WeatherApp";
    private static Application mApplication;
    private static List<String> mLatLngList;
    private static List<String> mCityList;
    private static LocationService mLocationService;

    private PreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mLatLngList = new ArrayList<>();
        mCityList = new ArrayList<>();
        VolleyNetworkService.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        int unit = preferenceManager.getInt(PreferenceManager.KeyValue.UNIT_KEY);
        Configurations.setUnit(unit);
        mLocationService = new LocationService();

    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public static synchronized List<String> getCityList(){
        return mCityList;
    }

    public synchronized static String findCity(String location){
        int index = mLatLngList.indexOf(location);
        return mCityList.get(index);
    }

    public static synchronized Application getInstance() {
        return mApplication;
    }

    public static synchronized List<String> getLatLngList(){
        return mLatLngList;
    }

    public void cancelAll(){
       VolleyNetworkService.getInstance().cancelAll();
    }

    public void saveUnitChange(boolean b){
        preferenceManager.save(PreferenceManager.KeyValue.UNIT_KEY, b ? 1:0);
    }

    public void saveWeatherData(){

    }

    public void saveForecastData(){

    }

    public synchronized static Address getAddressHere(){
        return mLocationService.getAddress(mLocationService.getLastKnownLocation());
    }
}

