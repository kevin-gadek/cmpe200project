package com.proj.cmpe200.sjsu.weatherapp.service;



public interface NetworkService {
    void cancel(String tag);
    void cancelAll();
    void getString(String url, String tag, FutureTaskListener<String> listener);
}

