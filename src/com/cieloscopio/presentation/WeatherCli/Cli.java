package com.cieloscopio.presentation.WeatherCli;

import com.cieloscopio.domain.datasource.WeatherDataSource;
import com.cieloscopio.infrastructure.datasource.OpenWeatherDatasourceImpl;

import java.util.Scanner;

public class Cli {
    protected final Scanner input = new Scanner(System.in);
    // Datasource
    protected final WeatherDataSource openWeatherDataSource = new OpenWeatherDatasourceImpl();
    protected final WeatherCliDetails details = new WeatherCliDetails();
}
