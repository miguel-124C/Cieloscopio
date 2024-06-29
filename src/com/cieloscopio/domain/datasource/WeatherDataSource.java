package com.cieloscopio.domain.datasource;

import com.cieloscopio.domain.entities.country.CountryResponse;
import com.cieloscopio.domain.entities.weather.OpenForecastResponse;
import com.cieloscopio.domain.entities.weather.OpenWeatherResponse;

import java.util.List;

public interface WeatherDataSource {
    public List<CountryResponse> getCoordinatesByCityName(String cityName );
    public OpenWeatherResponse getCurrentWeatherData( double latitude, double longitude );
    public OpenForecastResponse getWeatherForecastFiveDay(double latitude, double longitude );

    public String  setApiKey();
}
