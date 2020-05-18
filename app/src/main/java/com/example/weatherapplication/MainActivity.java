package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView hum;
    private ImageView imgView;

    private FloatingActionButton polish,english,russian;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = findViewById(R.id.cityText);
        condDescr =  findViewById(R.id.condDescr);
        temp =  findViewById(R.id.temp);
        hum =  findViewById(R.id.hum);
        press =  findViewById(R.id.press);
        windSpeed =  findViewById(R.id.windSpeed);
        windDeg =  findViewById(R.id.windDeg);
        imgView =  findViewById(R.id.condIcon);


        polish = findViewById(R.id.languagePolish);
        english = findViewById(R.id.languageEnglish);
        russian = findViewById(R.id.languageRussian);
        loadLocale();
        polish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("pl");
                recreate();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                recreate();
            }
        });

        russian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ru");
                recreate();
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);

        String provider = locationManager.getBestProvider(criteria, false);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);
            //location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            MyLocationListener mylistener = new MyLocationListener();

            if (location != null) {
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                // add location to the location listener for location changes
                mylistener.onLocationChanged(location);
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

            // location updates: at least 1 meter and 500 milli seconds change
            locationManager.requestLocationUpdates(provider, 500, 1, mylistener);
        } catch (SecurityException e) {
            Log.e("SecurityException", e.getMessage());
        }
    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }
    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration confg = new Configuration();
        confg.locale = locale;
        getBaseContext().getResources().updateConfiguration(confg,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }
   private class MyLocationListener implements LocationListener {

       @Override
       public void onLocationChanged(android.location.Location location) {
           Log.i("Provider: ", location.getProvider());
           Log.i("Latitude: ", String.valueOf(location.getLatitude()));
           Log.i("Longitude: ", String.valueOf(location.getLongitude()));
           JSONWeatherTask task = new JSONWeatherTask();
           task.execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
       }

       @Override
       public void onStatusChanged(String provider, int status, Bundle extras) {
           Log.i("onStatusChanged: ",  "Do something with the status: " + status );
       }

       @Override
       public void onProviderEnabled(String provider) {
           Log.i("onProviderEnabled: ", "Do something with the provider-> " + provider);
       }

       @Override
       public void onProviderDisabled(String provider) {
           Log.i("onProviderDisabled:", "Do something with the provider-> " + provider);
       }
   }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0],params[1]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "°");

        }






    }


}
