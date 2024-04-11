package com.example.web4.math;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Functions {
    public double f(double x, int number) {
        switch (number) {
            case (0) -> {
                return 3 * Math.pow(x, 3) - 2 * Math.pow(x, 2) + 7 * x + 26;
            }
            case (1) -> {
                return 3 * Math.pow(x, 5) + Math.pow(x, 2) + 0.1;
            }
            case (2) -> {
                return Math.sin(x) + Math.cos(x);
            }
            case (3) -> {
                return 1/(Math.sqrt(1-Math.pow(x, 2)));
            }
            case (4) -> {
                return 1/x;
            }
            default -> {
                System.out.println("Введите число в диапазоне 1-4");
                return f(x, number);
            }
        }

    }

    public ArrayList<Double> getErrPoints( int number) {
        switch (number) {
            case 3 -> {
                return new ArrayList<>(Arrays.asList(-1.0, 1.0));
            }
            case 4 -> {
                return new ArrayList<>(Arrays.asList(0.0));
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    public ArrayList<Double> getErrInterval( int number) {
        switch (number) {
            case 4 -> {
                return new ArrayList<>(Arrays.asList(-1.0001, 1.0001));
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    public double f_dx(double x, int number) {
        switch (number) {
            case (0) -> {
                return 3 * Math.pow(x, 4) / 4 - 2 * Math.pow(x, 3) / 3 + 7 * Math.pow(x, 2) / 2 + 26 * x;
            }
            case (1) -> {
                return 3 * Math.pow(x, 6) / 6 + Math.pow(x, 3) / 3 + x / 10;
            }
            case (2) -> {
                return Math.sin(x) - Math.cos(x);
            }
            case (3) -> {
                return Math.asin(x);
            }
            case (4) -> {
                return Math.log(Math.abs(x));
            }
            default -> {
                System.out.println("Введите число в диапазоне 1-4");
                return f_dx(x, number);
            }
        }
    }
}
