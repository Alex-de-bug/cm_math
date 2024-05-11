package com.example.web4.math;

public class Functions {
    public static DifferentialEquation getFunction(int index) {
        return new DifferentialEquationImpl(index);
    }
}
