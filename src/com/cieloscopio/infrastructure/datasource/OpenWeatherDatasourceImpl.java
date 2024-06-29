package com.cieloscopio.infrastructure.datasource;

import com.cieloscopio.domain.datasource.WeatherDataSource;
import com.cieloscopio.domain.entities.country.CountryResponse;
import com.cieloscopio.domain.entities.weather.OpenForecastResponse;
import com.cieloscopio.domain.entities.weather.OpenWeatherResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

public class OpenWeatherDatasourceImpl implements WeatherDataSource {
    private final String API_KEY;
    private final String urlPath = "http://api.openweathermap.org/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public OpenWeatherDatasourceImpl() {
        this.API_KEY = this.setApiKey();
    }

    @Override
    public List<CountryResponse> getCoordinatesByCityName(String cityName) {
        String param = "";
        try {
            param = "?q=" + URLEncoder.encode(cityName, "UTF-8") + "&limit=5&appid=" + this.API_KEY;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String uri = this.urlPath + "geo/1.0/direct" + param;

        try {
            String json = this.getBody( uri );
            Type listType = new TypeToken<List<CountryResponse>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (IOException | InterruptedException e) {
            throw new Error("error interno del servidor");
        }
    }

    @Override
    public OpenWeatherResponse getCurrentWeatherData(double latitude, double longitude) {
        String param = "?lat="+latitude+"&lon="+longitude+"&lang=es&units=metric&appid="+this.API_KEY;
        String uri = this.urlPath + "data/2.5/weather" + param;

        try {
            String json = this.getBody( uri );
            return gson.fromJson(json, OpenWeatherResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OpenForecastResponse getWeatherForecastFiveDay(double latitude, double longitude) {
        String param = "?lat="+latitude+"&lon="+longitude+"&lang=es&units=metric"+"&appid="+this.API_KEY;
        String uri = this.urlPath + "data/2.5/forecast" + param;

        try {
            String json = this.getBody( uri );
            return gson.fromJson(json, OpenForecastResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String setApiKey() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String apiKey = properties.getProperty("API_KEY");
        if (apiKey == null) {
            throw new IllegalStateException("API key not found in config.properties");
        }

        return apiKey;
    }

    private String getBody( String uri ) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( uri ))
                .build();
        HttpResponse<String> response = this.client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}