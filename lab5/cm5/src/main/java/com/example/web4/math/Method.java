package com.example.web4.math;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public abstract class Method {
    protected Integer size;
    protected Double arg;
    protected ArrayList<Double> xVal;
    protected ArrayList<Double> yVal;

    @Getter
    public ArrayList<ArrayList<Double>> defy;

    private static final Logger logger = LoggerFactory.getLogger(Method.class);

    public Method(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        this.size = size;
        this.arg = arg;
        this.xVal = xVal;
        this.yVal = yVal;
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
