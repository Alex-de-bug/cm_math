package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Lagrange extends Method {
    private static final Logger logger = LoggerFactory.getLogger(Lagrange.class);
    private double interpolatedValue = 0.0;


    public Lagrange(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

    @Override
    public void calculate() {
        double v = this.arg;
        double sum = 0.0;

        for (int i = 0; i < this.size; i++) {
            double term = 1.0;
            for (int j = 0; j < this.size; j++) {
                if (j != i) {
                    term *= (v - this.xVal.get(j)) / (this.xVal.get(i) - this.xVal.get(j));
                }
            }
            sum += this.yVal.get(i) * term;
        }
        logger.info(sum + "");
        this.interpolatedValue = sum;
    }

    @Override
    public String getNameMethod() {
        return "Lagrange\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ " + this.arg + " \\ is\\ y\\ =\\ " + interpolatedValue;
    }
}
