package com.cieloscopio.presentation.WeatherCli.screens;

import com.cieloscopio.domain.entities.country.CountryResponse;
import com.cieloscopio.presentation.WeatherCli.Cli;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class ListCountries extends Cli {
    private final String directory = "src/com/cieloscopio/data";
    private final String path = "list-countries.json";
    private List<CountryResponse> countriesList = new ArrayList<>();
    private List<CountryResponse> countriesSearched = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final int maxCountriesList = 10;

    public ListCountries() {
        this.createDir();
        this.loadCountriesList();
    }
    private void createDir(){
        Path pathDirectory = Paths.get(this.directory);
        Path pathFile = pathDirectory.resolve(this.path);

        try {
            if (!Files.exists(pathDirectory)) {
                Files.createDirectories(pathDirectory);
                System.out.println("Directorio creado: " + pathDirectory.toString());
            }

            if (!Files.exists(pathFile)) {
                Files.createFile(pathFile);
                System.out.println("Archivo creado: " + pathFile.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadCountriesList() {
        try (FileReader reader = new FileReader(directory + "/" + path)) {
            Type listType = new TypeToken<List<CountryResponse>>() {}.getType();
            this.countriesList = this.gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo JSON", e);
        }
    }
    private void saveCountriesList(){
        try ( FileWriter writer = new FileWriter(directory + "/" + path)) {
            this.gson.toJson( this.countriesList, writer );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void start(){
        while (true){
            this.showListCountries();
            System.out.println("----------------------------------");
            if (!countriesList.isEmpty()){
                System.out.println("--Ingrese el número de la ciudad--");
                System.out.println("----------------------------------");
            }
            System.out.println("Más opciones:");
            if (this.countriesList.size() < this.maxCountriesList){
                System.out.println("97) Añadir ciudad.");
            }else{
                System.out.println("98) Eliminar ciudad.");
            }
            System.out.println("99) Volver.");

            try {
                int option = input.nextInt();
                input.nextLine();
                if (actions( option )) break;
            }catch (InputMismatchException e ){
                System.out.println("Ingrese un número");
                input.nextLine();
            }
        }
    }
    private void showListCountries() {
        System.out.println("----------------------------");
        System.out.println("Lista de Ciudades registradas");
        if (countriesList.isEmpty()){
            System.out.println("--No hay ciudades registradas--");
        }
        for (int i = 0; i < countriesList.size(); i++){
            System.out.println((i+1) + ")" + " " + countriesList.get(i).name() + "(" +countriesList.get(i).country()+ ")");
        }
    }
    private boolean actions( int option ){
        if ( option > 0 && option <= this.countriesList.size() ){
            new SearchByCountry().optionsOfCountry( option, this.countriesList );
            return false;
        }else{
            switch (option){
                case 97: {
                    if (this.countriesList.size() + 1 > this.maxCountriesList){
                        System.out.println("No existe la ciudad de número: " + option);
                        return false;
                    }
                    this.countriesSearched = new SearchByCountry().searchCountry();
                    if (countriesSearched.isEmpty()) return false;

                    while (true){
                        try {
                            this.details.showDetailCountries( this.countriesSearched );
                            System.out.println("Ingrese el número de la ciudad que quiere añadir");
                            System.out.println("99) Volver");
                            int index = input.nextInt();
                            if (this.addOrExit(index)) break;
                        }catch (InputMismatchException e ) {
                            System.out.println("Ingrese un número");
                            input.nextLine();
                        }
                    }

                    return false;
                }
                case 98:{
                    if (this.countriesList.size() == this.maxCountriesList){
                        do {
                            showListCountries();
                            System.out.println("""
                            Ingrese el número de la ciudad a eliminar.
                            Opciones:
                            99) Volver.
                            """);

                        } while (!this.deleteCountry());
                    }else{
                        System.out.println("No existe la ciudad de número: " + option);
                    }
                    return false;
                }
                case 99: return true;
                default: {
                    System.out.println("No existe la ciudad de número: " + option);
                    return false;
                }
            }
        }
    }
    private boolean addOrExit( int option ){
        if (option == 99) return true;

        if ( option > 0 && option <= countriesSearched.size() ){
            CountryResponse country = this.getCountryByIndex(option);

            if(isRepeat(country)){
                System.out.println("La ciudad ya está registrada");
                return false;
            }else {
                this.addCountry(country);
                System.out.println("Ciudad Añadida con exito.");
                return true;
            }
        }else{
            System.out.println("Número no valido.");
            return false;
        }
    }
    private CountryResponse getCountryByIndex( int index ){
        return countriesSearched.get(index - 1);
    }
    private void addCountry( CountryResponse country ){
        this.loadCountriesList();
        this.countriesList.add(country);
        saveCountriesList();
    }
    private boolean isRepeat(CountryResponse newCountry) {
        return this.countriesList.stream().anyMatch(country -> country.equals(newCountry));
    }
    private boolean deleteCountry(){
        try {
            int index = input.nextInt();
            if ( index > 0 && index <= countriesList.size() ){
                this.countriesList.remove(index - 1);
                saveCountriesList();

                System.out.println("Eliminado con exito.");
                return true;
            }else{
                System.out.println("No existe la ciudad de número: " + index);
            }
        }catch (InputMismatchException e ) {
            System.out.println("Ingrese un número");
            input.nextLine();
        }

        return false;
    }
}