package com.cieloscopio.domain.entities.weather;

import java.util.List;

public record ListForecast(
        int dt,
        Main main,
        List<Weather> weather,
        Wind wind,
        int visibility,
        String dt_txt
) {}
