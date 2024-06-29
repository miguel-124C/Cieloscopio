package com.cieloscopio.domain.usecases;

import com.cieloscopio.domain.datasource.WeatherDataSource;
import com.cieloscopio.domain.entities.weather.OpenWeatherResponse;

public class GetCurrentWeatherData {
    private final WeatherDataSource weatherDataSource;
    public GetCurrentWeatherData(WeatherDataSource weatherDataSource){
        this.weatherDataSource = weatherDataSource;
    }
    public OpenWeatherResponse execute(double latitude, double longitude ) {
        return this.weatherDataSource.getCurrentWeatherData( latitude, longitude );
    }
}
