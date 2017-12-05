package com.proj.cmpe200.sjsu.weatherapp.service.intent;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.proj.cmpe200.sjsu.weatherapp.WeatherForecastContainer;
import com.proj.cmpe200.sjsu.weatherapp.model.WeatherModel;
import com.proj.cmpe200.sjsu.weatherapp.service.DataService;
import com.proj.cmpe200.sjsu.weatherapp.service.FutureTaskListener;
import com.proj.cmpe200.sjsu.weatherapp.service.OpenWeatherDataService;
import com.proj.cmpe200.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;


public class FetchForecastIntentService extends IntentService {
    public final static String TAG = "FetchForecastIntentService";
    public final static String WHO = "com.proj.cmpe200.sjsu.weatherapp.who";
    public final static String FETCH_WEATHER = "com.proj.cmpe200.sjsu.weatherapp.fetch_weather";

    public FetchForecastIntentService(){
        this(TAG);
    }
    public FetchForecastIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Logger.d(TAG, "onHandleIntent");
        final String location = intent.getStringExtra(FETCH_WEATHER);
        final Bundle bundle = intent.getExtras();

        if(location != null){

            DataService service = new OpenWeatherDataService();
            service.getForecastByLatLng(location, new FutureTaskListener<ArrayList<WeatherModel>>() {
                @Override
                public void onCompletion(final ArrayList<WeatherModel> results) {
                    if(results != null){

                        WeatherForecastContainer.getInstance().put(location, results);
                        try {
                            Messenger messenger = (Messenger) bundle.get(WHO);
                            Message msg = Message.obtain();
                            bundle.putString(FETCH_WEATHER, location);
                            msg.setData(bundle);
                            if (messenger != null) {
                                messenger.send(msg);
                            }
                        } catch (RemoteException e) {
                            Logger.e(TAG, "Error");
                        }

                    }
                }

                @Override
                public void onError(String errorMessage) {

                }

                @Override
                public void onProgress(float progress) {

                }
            });

        }

    }
}
