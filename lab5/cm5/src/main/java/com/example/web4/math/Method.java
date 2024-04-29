package com.example.web4.math;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public abstract class Method {
    private static final Logger logger = LoggerFactory.getLogger(Method.class);
    @Getter
    @Setter
    public ArrayList<ArrayList<Double>> defy;
    protected Integer size;
    protected Double arg;
    protected ArrayList<Double> xVal;
    protected ArrayList<Double> yVal;

    public Method(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        this.size = size;
        this.arg = arg;
        this.xVal = xVal;
        this.yVal = yVal;
    }

    protected static double factorial(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public abstract void calculate();

    protected String formatScientificNotation(String value) {
        if (value.contains("E")) {
            return value.replace("E", "\\cdot10 ^{") + "}";
        } else {
            return value;
        }
    }

    public abstract String getNameMethod();

    public abstract String getAnswer();
}
