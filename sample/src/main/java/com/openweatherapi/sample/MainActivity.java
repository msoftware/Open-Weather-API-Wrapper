/*
 * Copyright 2016 Keval Patel.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.openweatherapi.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openweatherweapper.OpenWeatherApi;
import com.openweatherweapper.Unit;
import com.openweatherweapper.interfaces.CurrentWeatherListener;
import com.openweatherweapper.interfaces.ForecastListener;
import com.openweatherweapper.models.CurrentWeather;
import com.openweatherweapper.models.WeatherForecast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView sunsetTv;
    private TextView sunriseTv;
    private TextView windSpeedTv;
    private TextView tempLowTv;
    private TextView tempHighTv;
    private TextView cityTv;
    private TextView tempTv;

    private CurrentWeatherListener mCurrentWeatherListener = new CurrentWeatherListener() {
        @Override
        public void onResponse(CurrentWeather currentWeather) {
            cityTv.setText(currentWeather.getCityName());

            //set the temperature
            tempHighTv.setText(currentWeather.getMain().getTempMax() + " " + getString(R.string.degree_fernhight));
            tempLowTv.setText(currentWeather.getMain().getTempMin() + " " + getString(R.string.degree_fernhight));
            tempTv.setText(currentWeather.getMain().getTemp() + " " + getString(R.string.degree_fernhight));

            windSpeedTv.setText(currentWeather.getWindInfo().getSpeed() + " km/s");

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            sunriseTv.setText(dateFormat.format(currentWeather.getSys().getSunrise()));
            sunsetTv.setText(dateFormat.format(currentWeather.getSys().getSunset()));
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the api
        OpenWeatherApi.initialize(getString(R.string.your_open_weather_api_key), Unit.STANDARD);

        cityTv = (TextView) findViewById(R.id.city_tv);
        tempTv = (TextView) findViewById(R.id.temp_tv);
        tempLowTv = (TextView) findViewById(R.id.temp_low_tv);
        tempHighTv = (TextView) findViewById(R.id.temp_high_tv);
        windSpeedTv = (TextView) findViewById(R.id.wind_speed_tv);
        sunriseTv = (TextView) findViewById(R.id.sunrise_tv);
        sunsetTv = (TextView) findViewById(R.id.sunset_tv);

        final EditText cityEt = (EditText) findViewById(R.id.city_et);

        findViewById(R.id.go_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = cityEt.getText().toString().trim();
                if (!s.isEmpty())
                    OpenWeatherApi.getCurrentWeather(s, mCurrentWeatherListener);

                OpenWeatherApi.getThreeHoursForecast("Landon,uk", new ForecastListener() {
                    @Override
                    public void onResponse(WeatherForecast weatherForecasts) {
                        Log.d("Three hour", "Success");
                    }

                    @Override
                    public void onError(String message) {
                        Log.d("Three hour", message);
                    }
                });
            }
        });
    }
}
