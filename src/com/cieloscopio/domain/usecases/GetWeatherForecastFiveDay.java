package com.cieloscopio.domain.usecases;

import com.cieloscopio.domain.datasource.WeatherDataSource;
import com.cieloscopio.domain.entities.weather.OpenForecastResponse;

public class GetWeatherForecastFiveDay {
    private final WeatherDataSource weatherDataSource;
    public GetWeatherForecastFiveDay(WeatherDataSource weatherDataSource){
        this.weatherDataSource = weatherDataSource;
    }
    public OpenForecastResponse execute(double latitude, double longitude ) {
        return this.weatherDataSource.getWeatherForecastFiveDay( latitude, longitude );
    }
}
