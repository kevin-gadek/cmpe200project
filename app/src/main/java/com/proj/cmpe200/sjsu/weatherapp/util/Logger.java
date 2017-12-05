package com.proj.cmpe200.sjsu.weatherapp.util;

import android.util.Log;

import com.proj.cmpe200.sjsu.weatherapp.service.GooglePlaceService;
import com.proj.cmpe200.sjsu.weatherapp.service.intent.FetchForecastIntentService;
import com.proj.cmpe200.sjsu.weatherapp.WeatherApp;
import com.proj.cmpe200.sjsu.weatherapp.WeatherForecastContainer;
import com.proj.cmpe200.sjsu.weatherapp.activities.CityListViewActivity;
import com.proj.cmpe200.sjsu.weatherapp.MainActivity;
import com.proj.cmpe200.sjsu.weatherapp.activities.WeatherFragment;
import com.proj.cmpe200.sjsu.weatherapp.service.OpenWeatherDataService;
import com.proj.cmpe200.sjsu.weatherapp.service.VolleyNetworkService;

import java.util.Arrays;
import java.util.List;


public final class Logger {
    private Logger() {}

    private static final List<String> CLASS_SIMPLE_NAMES = Arrays.asList(
            MainActivity.TAG,
            CityListViewActivity.TAG,
            WeatherFragment.TAG,
            WeatherApp.TAG,
            WeatherForecastContainer.TAG,
            FetchForecastIntentService.TAG,
            OpenWeatherDataService.TAG,
            VolleyNetworkService.TAG,
            GooglePlaceService.TAG
    );

    public static synchronized void d(String tag, String msg) {
        if (CLASS_SIMPLE_NAMES.contains(tag)) {
            Log.d(tag, msg);
        }
    }

    public static synchronized void e(String tag, String msg) {
        if (CLASS_SIMPLE_NAMES.contains(tag)) {
            Log.e(tag, msg);
        }
    }
}