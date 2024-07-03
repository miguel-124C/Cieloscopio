package com.cieloscopio.domain.entities.country;

public record CountryResponse(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {
}
