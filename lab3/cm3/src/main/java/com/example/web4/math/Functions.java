package com.example.web4.math;

public class Functions {
    public double f(double x, int number) {
        switch (number) {
            case (0) -> {
//                3x^3-2x^2+7x+26
                return 3 * Math.pow(x, 3) - 2 * Math.pow(x, 2) + 7 * x + 26;
            }
            case (1) -> {
                return 3 * Math.pow(x, 5) + Math.pow(x, 2) + 0.1;
            }
            case (2) -> {
                return Math.sin(x) + Math.cos(x);
            }
            case (3) -> {
                return 1 / x;
            }
            default -> {
                System.out.println("Введите число в диапазоне 1-4");
                return f(x, number);
            }
        }

    }
    private double f_dx(double x, int number) {
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
                return Math.log(x);
            }
            default -> {
                System.out.println("Введите число в диапазоне 1-4");
                return f_dx(x, number);
            }
        }
    }
    public double getI(double a, double b, int number) {
        return f_dx(b, number) - f_dx(a, number);
    }
}
