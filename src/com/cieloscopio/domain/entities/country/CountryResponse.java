package com.cieloscopio.domain.entities.country;

import com.google.gson.JsonObject;

import java.util.Map;

public record CountryResponse(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {
}
