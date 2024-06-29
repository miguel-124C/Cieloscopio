package com.cieloscopio.domain.entities.weather;

import java.util.List;

public record OpenForecastResponse(
        List<ListForecast> list,
        CityForecast city
) {}
