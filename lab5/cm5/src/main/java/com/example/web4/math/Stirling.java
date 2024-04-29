package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Stirling extends Method {
    private double interpolatedValue = 0.0;
    private ArrayList<ArrayList<Double>> defy;
    private static final Logger logger = LoggerFactory.getLogger(Stirling.class);

    public Stirling(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal, ArrayList<ArrayList<Double>> defy) {
        super(size, arg, xVal, yVal);
        this.defy = defy;
    }

    @Override
    public void calculate() {
        if (size % 2 == 0) {
            logger.warn("Stirling's interpolation requires an even number of data points.");
            return;
        }

        int n = size - 1;
        int center = n / 2;
        double a = xVal.get(center);
        double h = xVal.get(1) - xVal.get(0);  // Assuming uniform spacing
        double t = (arg - a) / h;
        logger.info(t+"");

        interpolatedValue = defy.get(center).get(0)
                + t * (defy.get(center - 1).get(1) + defy.get(center).get(1)) / 2
                + Math.pow(t, 2) / 2 * defy.get(center - 1).get(2);

        double term = Math.pow(t, 2) / 2;
        for (int k = 3; k <= n; k++) {
            if (k % 2 == 0) {
                term *= t / k;
                interpolatedValue += term * defy.get(center - k / 2).get(k);
            } else {
                term *= (Math.pow(t, 2) - Math.pow(k / 2, 2)) / (k * t);
                interpolatedValue += term * (defy.get(center - k / 2 - 1).get(k) + defy.get(center - k / 2).get(k)) / 2;
            }
        }
    }

    @Override
    public String getNameMethod() {
        return "Stirling's Interpolation";
    }

    @Override
    public String getAnswer() {
        if (size % 2 == 0) {
            return "Stirling's interpolation requires an even number of data points.";
        }
        return getNameMethod() + String.format(": Interpolated value at x = %.2f is y = %.2f", this.arg, interpolatedValue);
    }
}
