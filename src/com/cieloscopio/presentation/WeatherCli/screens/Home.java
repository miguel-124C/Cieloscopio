package com.cieloscopio.presentation.WeatherCli.screens;

import com.cieloscopio.presentation.WeatherCli.Cli;

import java.util.InputMismatchException;

public class Home extends Cli {
    public void start(){
        while (true){
            this.welcome();
            try {
                int option = input.nextInt();
                if (this.actions(option)) break;
            }catch ( InputMismatchException e ){
                System.out.println("Ingrese un número");
                input.nextLine();
            }
        }
    }
    private boolean actions( int option ){
        return switch (option) {
            case 1 -> {
                new ListCountries().start();
                yield false;
            }
            case 2 -> {
                new SearchByCountry().start();
                yield false;
            }
            case 99 -> {
                System.out.println("Saliendo...");
                yield true;
            }
            default -> {
                System.out.println("No ingresó un número válido.");
                yield false;
            }
        };
    }
    private void welcome(){
        System.out.println("|--------------------------|");
        System.out.println("| Bienvenido a Cieloscopio |");
        System.out.println("|--------------------------|");
        System.out.println();

        System.out.println("Opciones:");
        System.out.println("""
                1) Lista de ciudades.
                2) Buscar por ciudad
                99) Salir.
                """);
    }
}