package com.proj.cmpe200.sjsu.weatherapp.service;



public interface FutureTaskListener<V> {
    void onCompletion(V result);
    void onError(String errorMessage);
    void onProgress(float progress);
}