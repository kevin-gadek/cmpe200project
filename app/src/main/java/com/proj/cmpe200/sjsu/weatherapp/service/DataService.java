package com.proj.cmpe200.sjsu.weatherapp.service;

import com.proj.cmpe200.sjsu.weatherapp.model.WeatherModel;

import java.util.ArrayList;

public interface DataService {
    //WeatherModel getWeatherByLatLng(String location);
    void getForecastByLatLng(String location, FutureTaskListener<ArrayList<WeatherModel>> listener);
    void getWeatherByLatLng(String location, FutureTaskListener<WeatherModel> listener);
    void getWeatherThreeHoursLatLng(String location, FutureTaskListener<ArrayList<WeatherModel>> listener);
}
