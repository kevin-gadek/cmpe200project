package com.proj.cmpe200.sjsu.weatherapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseWeatherActivity extends AppCompatActivity{
    protected abstract void onLoadUI();
    protected abstract void onFetchPeriodically();
    protected abstract void onLoadData();

    protected abstract void fetchThreeHours(String location);
    protected abstract void fetchTodayWeather(String location);
    protected abstract void fetchForecastWeather(String location);

    protected void fetchData(String location){
        fetchTodayWeather(location);
        fetchForecastWeather(location);
        fetchThreeHours(location);
    }

    protected void load(){
        onLoadUI();
        onLoadData();
        onFetchPeriodically();
    }

    public void showFragment(int containerResId, Fragment fragment){
        showFragment(containerResId, fragment, false, false);
    }

    public void showFragment(int containerResId, Fragment fragment, boolean addToBackStack){
        showFragment(containerResId, fragment, addToBackStack, false);
    }

    public void showFragment(int containerResId, Fragment fragment, boolean addToBackStack, boolean isAnimated){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerResId, fragment, fragment.getClass().getSimpleName());
        if(addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }
}