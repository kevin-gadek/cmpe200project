package com.proj.cmpe200.sjsu.weatherapp.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.proj.cmpe200.sjsu.weatherapp.R;
import com.proj.cmpe200.sjsu.weatherapp.util.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Kevin on 11/6/2017.
 */

public class WeatherMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    public final static String TAG = WeatherMapActivity.class.getSimpleName();
    //private final List<WeatherModel> mModels = new ArrayList<>();
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(37.3382, -121.8863);
    private TileOverlay mTileOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "onCreate");
        setContentView(R.layout.activity_weather_map);
        onloadUI();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Logger.d(TAG, "onStart");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Logger.d(TAG, "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Logger.d(TAG, "onResume");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Logger.d(TAG, "onStop");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }
    protected void onloadUI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Weather Map");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_temp:
                        setTempLayer();
                        break;
                    case R.id.action_precip:
                        setPrecipLayer();
                        break;
                    case R.id.action_wind:
                        setWindLayer();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in default location, should set at detected location in future
        LatLng sanJose = mDefaultLocation;
        mMap.addMarker(new MarkerOptions().position(sanJose)
                .title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sanJose));

        setTempLayer();
    }

    protected void setTempLayer(){
        if(mTileOverlay != null){
            mTileOverlay.remove();
        }
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String s = String.format(Locale.US, "http://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=b54f500d4a53fdfc96813a4ba9210417", zoom, x, y);
                try{
                    return new URL(s);
                }catch(MalformedURLException e){
                    throw new AssertionError(e);
                }
            }
        };

        mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));

    }

    protected void setPrecipLayer(){
        if(mTileOverlay != null){
            mTileOverlay.remove();
        }
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String s = String.format(Locale.US, "http://tile.openweathermap.org/map/precipitation_new/%d/%d/%d.png?appid=b54f500d4a53fdfc96813a4ba9210417", zoom, x, y);
                try{
                    return new URL(s);
                }catch(MalformedURLException e){
                    throw new AssertionError(e);
                }
            }
        };

        mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    protected void setWindLayer(){
        if(mTileOverlay != null){
            mTileOverlay.remove();
        }
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String s = String.format(Locale.US, "http://tile.openweathermap.org/map/wind_new/%d/%d/%d.png?appid=b54f500d4a53fdfc96813a4ba9210417", zoom, x, y);
                try{
                    return new URL(s);
                }catch(MalformedURLException e){
                    throw new AssertionError(e);
                }
            }
        };

       mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));


    }

}
