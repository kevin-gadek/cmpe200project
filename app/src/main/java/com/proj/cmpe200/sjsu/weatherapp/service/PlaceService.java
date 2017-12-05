package com.proj.cmpe200.sjsu.weatherapp.service;

import com.proj.cmpe200.sjsu.weatherapp.model.LocalTimeModel;



public interface PlaceService {
    void getLocalTime(String location, FutureTaskListener<LocalTimeModel> listener);
}
