package com.proj.cmpe200.sjsu.weatherapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.proj.cmpe200.sjsu.weatherapp.MainActivity;
import com.proj.cmpe200.sjsu.weatherapp.R;
import com.proj.cmpe200.sjsu.weatherapp.TodayWeatherContainer;
import com.proj.cmpe200.sjsu.weatherapp.WeatherApp;
import com.proj.cmpe200.sjsu.weatherapp.model.WeatherModel;
import com.proj.cmpe200.sjsu.weatherapp.service.intent.FetchForecastIntentService;
import com.proj.cmpe200.sjsu.weatherapp.service.intent.FetchThreeHoursIntentService;
import com.proj.cmpe200.sjsu.weatherapp.service.intent.FetchTodayWeatherIntentService;
import com.proj.cmpe200.sjsu.weatherapp.service.intent.RemoveWeatherIntentService;
import com.proj.cmpe200.sjsu.weatherapp.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.proj.cmpe200.sjsu.weatherapp.util.Constants.ListViewMessages.ADD_CITY;
import static com.proj.cmpe200.sjsu.weatherapp.util.Constants.ListViewMessages.REMOVE_CITY;
import static com.proj.cmpe200.sjsu.weatherapp.util.Constants.ListViewMessages.UPDATE_TIME;
import static com.proj.cmpe200.sjsu.weatherapp.util.Constants.ListViewMessages.UPDATE_WEATHER;

public class CityListViewActivity extends BaseWeatherActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    public final static String TAG = CityListViewActivity.class.getSimpleName();
    private CityViewAdapter mAdapter;
    private final List<WeatherModel> mModels = new ArrayList<>();
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private boolean pause = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list_view);
        Logger.d(TAG, "onCreate");
        getLocationPermission();
        onLoadUI();
        onLoadData();
        onFetchPeriodically();
    }

    @Override
    public void onResume(){
        super.onResume();
        Logger.d(TAG, "onResume");

    }

    @Override
    public void onPause(){
        super.onPause();
        Logger.d(TAG, "onPause");
    }

    protected void onLoadUI(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cities");

        FloatingActionButton mFabAddCity = (FloatingActionButton) findViewById(R.id.fab_add_city);
        FloatingActionButton mFabWeatherMap = (FloatingActionButton) findViewById(R.id.fab_weather_map);
        mAdapter = new CityViewAdapter(getApplicationContext(), R.layout.item_city_view, mModels);
        ListView listView = (ListView) findViewById(R.id.list_city_view);
        listView.setAdapter(mAdapter);
        mFabAddCity.setOnClickListener(this);
        mFabWeatherMap.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }

    @Override
    protected void onFetchPeriodically() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        // update time periodically
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = UPDATE_TIME;
                mHandler.sendMessage(msg);
            }
        }, 0, 1, TimeUnit.SECONDS);
        // fetchData weather periodically
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (mModels){
                    for (int i = 0; i < mModels.size(); i++){
                        Message msg = mHandler.obtainMessage();
                        msg.what = UPDATE_WEATHER;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }, 0, 3, TimeUnit.HOURS);
    }

    @Override
    protected void onLoadData() {
        load(WeatherApp.getLatLngList());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    private void load(final List<String> locations){
        Logger.d(TAG, String.valueOf(locations.size()));
        if(!locations.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(String location: locations){

                        WeatherModel weatherModel = TodayWeatherContainer.getInstance().getWeatherModel(location);
                        if(weatherModel != null){
                            Message msg = mHandler.obtainMessage();
                            msg.obj = weatherModel;
                            msg.what = ADD_CITY;
                            mHandler.sendMessage(msg);
                        }

                    }
                }
            }).start();
        }
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    protected void onWeatherMapLoad(){
        Intent i = new Intent(CityListViewActivity.this, WeatherMapActivity.class);
        startActivity(i);
    }

    private void onCitySearch(){
        try{
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException ignored){
        }
    }

    @Override
    protected void fetchTodayWeather(String location){
        Intent intent = new Intent(this, FetchTodayWeatherIntentService.class);
        intent.putExtra(FetchTodayWeatherIntentService.FETCH_WEATHER, location);
        intent.putExtra(FetchTodayWeatherIntentService.WHO, new Messenger(mHandler));
        startService(intent);
    }

    @Override
    protected void fetchForecastWeather(String location){
        Intent intent = new Intent(this, FetchForecastIntentService.class);
        intent.putExtra(FetchForecastIntentService.FETCH_WEATHER, location);
        startService(intent);
    }

    @Override
    protected void fetchThreeHours(String location) {
        Intent intent = new Intent(this, FetchThreeHoursIntentService.class);
        intent.putExtra(FetchThreeHoursIntentService.FETCH_THREE_HOURS, location);
        startService(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Logger.d(TAG, place.toString());
                final String city = place.getAddress().toString().split(",")[0];
                final double lat = place.getLatLng().latitude;
                final double lng = place.getLatLng().longitude;

                String location = "lat="+ String.format("%.2f",lat)+"&lon="+String.format("%.2f",lng);
                if(!WeatherApp.getLatLngList().contains(location)){
                    WeatherApp.getLatLngList().add(location);
                    WeatherApp.getCityList().add(city);
                    fetchData(location);
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Logger.d(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {}
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_city:
                onCitySearch();
                break;
            case R.id.fab_weather_map:
                onWeatherMapLoad();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
        Logger.d(TAG, "onItemClick " + position);

        if(pause){
            return;
        }
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("position", position);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        Logger.d(TAG, "onItemLongClick");

        pause = true;

        new AlertDialog.Builder(this)
                .setTitle("Delete City")
                .setMessage("Are you sure you want to delete city?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeFromSystem(i);
                        pause = false;
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                pause = false;
            }
        }).show();

        return false;
    }

    private void removeFromSystem(int index){
        String location = WeatherApp.getLatLngList().get(index);
        Intent i = new Intent(this, RemoveWeatherIntentService.class);
        i.putExtra(RemoveWeatherIntentService.REMOVE_LOCATION, location);
        startService(i);
        mModels.remove(index);
    }

    /* Post message here*/
    Handler mHandler = new Handler(Looper.getMainLooper()){
        public void handleMessage(android.os.Message msg) {

            Bundle reply = msg.getData();
            String location = reply.getString(FetchTodayWeatherIntentService.FETCH_WEATHER, null);
            if(location != null){
                WeatherModel weatherModel = TodayWeatherContainer.getInstance().getWeatherModel(location);
                mModels.add(weatherModel);
                mAdapter.notifyDataSetChanged();
                ListView listView = (ListView) findViewById(R.id.list_city_view);
                listView.setSelection(mModels.size()-1);
                return;
            }


            switch (msg.what){
                case ADD_CITY:
                    WeatherModel model = (WeatherModel) msg.obj;
                    mModels.add(model);
                    mAdapter.notifyDataSetChanged();

                    ListView listView = (ListView) findViewById(R.id.list_city_view);
                    listView.setSelection(mModels.size()-1);
                    break;

                case REMOVE_CITY:
                    int index = (int) msg.obj;
                    removeFromSystem(index);
                    break;
                case UPDATE_TIME: case UPDATE_WEATHER:
                    mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }

        }
    };

}