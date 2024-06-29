package com.cieloscopio.domain.entities.weather;

public record CityForecast(
        int id,
        String name,
        String country,
        int population
) {
}
