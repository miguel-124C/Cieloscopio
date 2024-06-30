package com.cieloscopio.presentation.WeatherCli;

import com.cieloscopio.domain.entities.country.CountryResponse;
import com.cieloscopio.domain.entities.weather.ListForecast;
import com.cieloscopio.domain.entities.weather.OpenForecastResponse;
import com.cieloscopio.domain.entities.weather.OpenWeatherResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherCliDetails {

    public void showDetailCountries( List<CountryResponse> countryData ){
        if (countryData.isEmpty()) return;

        int index = 1;
        for (CountryResponse country : countryData){
            System.out.println("|-------------------------------------|");
            System.out.println("| Número: " + index);
            System.out.println("| Nombre : " + country.name() + "(" +country.country()+ ")");
            System.out.println("| Estado: " + ((country.state() == null) ? "" : country.state()));
            index++;
        }
        System.out.println("|-------------------------------------|");
    }

    public void showDetailCurrentWeather(OpenWeatherResponse weather){
        System.out.println("|-------------------------------------|");
        System.out.println("| " + weather.name() ); //+ "(" + weather.country()+ ")");
        System.out.println("|-------------TEMPERATURA-------------|");
        System.out.println("| Actual    : " + weather.main().temp() + "ºC" );
        System.out.println("| Mínima    : " + weather.main().temp_min() + "ºC" );
        System.out.println("| Máxima    : " + weather.main().temp_max() + "ºC" );
        System.out.println("| Sensación : " + weather.main().feels_like() + "ºC");
        System.out.println("| Humedad   : " + weather.main().humidity() + "%");
        System.out.println("|----------------CLIMA----------------|");
        System.out.println("| Condición climática: " + weather.weather().get(0).description());
        if (weather.wind() != null){
            System.out.println("|---------------VIENTOS---------------|");
            System.out.println("| Velocidad : " + weather.wind().speed() + "m/s" + "   Grado : " + weather.wind().deg());
            System.out.println("| Ráfaga : " + weather.wind().gust() + "m/s");
        }
        System.out.println("|-------------------------------------|");
    }

    public void showDetailForecast(OpenForecastResponse forecastResponse){
        System.out.println("|-------------------------------------|");
        System.out.println("| " + forecastResponse.city().name() +"("+ forecastResponse.city().country() + ")" );
        System.out.println("|-------------------------------------|");

        Map<String, List<ListForecast>> forecastDays = forecastResponse.list().stream()
                        .collect(Collectors.groupingBy(
                                forecast ->
                                    LocalDateTime.parse(forecast.dt_txt(),
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                            .toLocalDate().toString(),
                                            LinkedHashMap::new,
                                            Collectors.toList())
                        );

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        forecastDays.forEach( (time, listForecasts) ->{
            LocalDate date = LocalDate.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String dayOfWeek = date.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, new Locale("es","ES"));

            if (currentDate.equals(time)){
                System.out.println("| Hoy" + "                "+ date);
            }else{
                System.out.println("| " + dayOfWeek.toUpperCase() + "                 "+ date);
            }

            double[] temp = getTempWeather( listForecasts );
            System.out.println("|-------------TEMPERATURA-------------|");
            System.out.println("| Maxima    : " + temp[0] + "ºC" );
            System.out.println("| Mínima    : " + temp[1] + "ºC" );
            System.out.println("| Humedad   : " + temp[2] +"%");
            System.out.println("|-------------------------------------|");
        });
    }

    private double[] getTempWeather(List<ListForecast> listForecasts){
        final double[] tempMax = {-9999};
        final double[] tempMin = {9999};

        double size = listForecasts.size();
        final double[] humidity = {0};

        listForecasts.forEach( forecast ->{
            if (tempMax[0] < forecast.main().temp_max()){
                tempMax[0] = forecast.main().temp_max();
            }

            if (tempMin[0] > forecast.main().temp_min()){
                tempMin[0] = forecast.main().temp_min();
            }

            humidity[0] += forecast.main().humidity();
        });
        humidity[0] /= size;

        return new double[]{ tempMax[0], tempMin[0], humidity[0] };
    }

}