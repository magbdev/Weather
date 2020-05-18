package com.example.weatherapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

public class WeatherAsyncTask extends AsyncTask<String, Void, Weather> {
    ImageView imageView;
    TextView city;
    TextView temperature;
    Context context;

    public WeatherAsyncTask(ImageView imageView, TextView city, TextView temperature, Context context) {
        this.imageView = imageView;
        this.city = city;
        this.temperature = temperature;
        this.context = context;
    }
    @Override
    protected Weather doInBackground(String... strings) {
        Weather weather = new Weather();
        String data = ( (new WeatherHttpClient()).getWeatherCity(strings[0]));

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
            imageView.setImageBitmap(img);
        }
        city.setText(weather.location.getCity() + "," + weather.location.getCountry());
        temperature.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");

    }
}
