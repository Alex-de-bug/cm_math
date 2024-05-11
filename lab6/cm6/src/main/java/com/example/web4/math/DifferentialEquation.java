package com.example.web4.math;


public interface DifferentialEquation {
    double eval(double x, double y);
    double getYRight(double xs, double ys, double x);
    String getLatexEquation(double x, double y);
}
