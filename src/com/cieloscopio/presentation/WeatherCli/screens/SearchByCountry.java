package com.cieloscopio.presentation.WeatherCli.screens;

import com.cieloscopio.domain.entities.country.CountryResponse;
import com.cieloscopio.domain.entities.weather.OpenForecastResponse;
import com.cieloscopio.domain.entities.weather.OpenWeatherResponse;
import com.cieloscopio.domain.usecases.GetCoordByCityName;
import com.cieloscopio.domain.usecases.GetCurrentWeatherData;
import com.cieloscopio.domain.usecases.GetWeatherForecastFiveDay;
import com.cieloscopio.presentation.WeatherCli.Cli;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class SearchByCountry extends Cli {
    private List<CountryResponse> countryData = new ArrayList<>();
    public void start(){
        boolean search = true;
        while (search){
            this.searchCountry();
            while (true){
                this.details.showDetailCountries(countryData);
                System.out.println("| Ingrese el número de la ciudad");
                System.out.println("| Opciones:");
                System.out.println("| 98) Volver a buscar por ciudad.");
                System.out.println("| 99) Volver.");

                try {
                    int option = input.nextInt();
                    if (option == 98) {
                        input.nextLine();
                        break;
                    }
                    if (this.actions( option )) {
                        search = false;
                        break;
                    }
                }catch (InputMismatchException e ) {
                    System.out.println("Ingrese un número");
                }
                input.nextLine();
            }
        }
    }
    public List<CountryResponse> searchCountry(){
        System.out.println("Ingrese el nombre de la ciudad");
        String cityName = input.nextLine();
        this.countryData = new GetCoordByCityName( this.openWeatherDataSource ).execute(cityName);

        if (this.countryData.isEmpty()){
            System.out.println("-------------------------------------");
            System.out.println("----No se encontró ninguna ciudad----");
            System.out.println("-------------------------------------");
        }

        return this.countryData;
    }
    private boolean actions( int option ){
        input.nextLine();
        if ( option > 0 && option <= this.countryData.size() ){
            this.optionsOfCountry( option, this.countryData );
            return false;
        }else if (option == 99){
            return true;
        }else{
            System.out.println("Ingrese un número valido.");
            return false;
        }
    }
    public void optionsOfCountry( int index, List<CountryResponse> countries ){
        CountryResponse country = countries.get(index - 1);

        do {
            System.out.println("|-------------------------------------|");
            System.out.println("| " + country.name() + "(" + country.country() + ")");
            System.out.println("|-------------------------------------|");
            System.out.println("""
                        | Opciones:
                        | 1)  Clima actual.
                        | 2)  Pronostico, proximos 5 días.
                        | 99) Volver.
                        """);

        } while (!this.viewCurrentOrForecastWeather(country));
    }
    private boolean viewCurrentOrForecastWeather(CountryResponse country ){
        try {
            int option = input.nextInt();
            if (option == 99) return true;
            if (option == 1){
                OpenWeatherResponse weather = new GetCurrentWeatherData(this.openWeatherDataSource)
                        .execute( country.lat(), country.lon() );

                while (true){
                    this.details.showDetailCurrentWeather(weather);
                    System.out.println("| 99) Volver");
                    int op = input.nextInt();
                    if (op == 99) {
                        break;
                    }else{
                        System.out.println("Ingrese un número valido.");
                    }
                }
            }else if (option == 2){
                OpenForecastResponse forecast = new GetWeatherForecastFiveDay( this.openWeatherDataSource )
                        .execute( country.lat(), country.lon() );

                while (true){
                    this.details.showDetailForecast( forecast );
                    System.out.println("| 99) Volver");
                    int op = input.nextInt();
                    if (op == 99) {
                        break;
                    }else{
                        System.out.println("Ingrese un número valido.");
                    }
                }
            }else{
                System.out.println("Ingrese un número valido");
            }
        }catch (InputMismatchException e ) {
            System.out.println("Ingrese un número");
            input.nextLine();
        }
        return false;
    }
}