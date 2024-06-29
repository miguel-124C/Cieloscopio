package com.cieloscopio.domain.entities.weather;

public record Wind(
    double speed,
    int deg,
    double gust
) {}