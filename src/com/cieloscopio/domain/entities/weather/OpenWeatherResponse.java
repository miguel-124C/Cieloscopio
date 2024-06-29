package com.cieloscopio.domain.entities.weather;

import java.util.List;

public record OpenWeatherResponse(
        Coord coord,
        List<Weather> weather,
        String base,
        Main main,
        Wind wind,
        int visibility,
        int dt,
        int timezone,
        int id,
        String name,
        int cod
) {
}
