package com.example.web4.math;

public class DifferentialEquationImpl implements DifferentialEquation {
    private final int index;

    public DifferentialEquationImpl(int index) {
        this.index = index;
    }

    @Override
    public double eval(double x, double y) {
        return switch (this.index) {
            case 0 -> 4 * x + y / 3.0;
            case 1 -> x * x + y;
            case 2 -> y * Math.cos(x);
            default -> throw new IllegalArgumentException("Invalid function index");
        };
    }

    @Override
    public double getYRight(double xs, double ys, double x) {
        return switch (this.index) {
            case 0 -> (ys + 12 * xs + 36) / Math.exp(xs / 3) * Math.exp(x / 3) - 12 * x - 36;
            case 1 -> (-ys - Math.pow(xs, 2) - 2 * xs - 2) / (-Math.exp(xs)) * Math.exp(x) - Math.pow(x, 2) - 2 * x - 2;
            case 2 -> ys / Math.exp(Math.sin(xs)) * Math.exp(Math.sin(x));
            default -> throw new IllegalArgumentException("Invalid function index");
        };
    }

    @Override
    public String getLatexEquation(double x, double y) {
        return switch (this.index) {
            case 0 -> "y =" + (y + 12 * x + 36) / Math.exp(x / 3) + "*e^{x/3} - 12*x - 36";
            case 1 -> "y =" + (-y - Math.pow(x, 2) - 2 * x - 2) / (-Math.exp(x)) +"* e^{x} - x^2 - 2 * x - 2";
            case 2 -> "y =" + y /Math.exp(Math.sin(x)) + "* e^{\\sin x}";
            default -> throw new IllegalArgumentException("Invalid function index");
        };
    }
}
