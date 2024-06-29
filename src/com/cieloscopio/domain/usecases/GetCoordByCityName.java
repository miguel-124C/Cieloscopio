package com.cieloscopio.domain.usecases;

import com.cieloscopio.domain.datasource.WeatherDataSource;
import com.cieloscopio.domain.entities.country.CountryResponse;

import java.util.List;

public class GetCoordByCityName {
    private final WeatherDataSource weatherDataSource;
    public GetCoordByCityName(WeatherDataSource weatherDataSource){
        this.weatherDataSource = weatherDataSource;
    }
    public List<CountryResponse> execute(String cityName ) {
        return this.weatherDataSource.getCoordinatesByCityName( cityName );
    }

}
