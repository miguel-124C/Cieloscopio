package com.cieloscopio.domain.entities.weather;

public record Weather(
        int id,
        String main,
        String description,
        String icon
) { }
