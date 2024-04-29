package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Bessel extends Method {
    private double interpolatedValue = 0.0;
    private static final Logger logger = LoggerFactory.getLogger(Bessel.class);

    public Bessel(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

    @Override
    public void calculate() {
        // Bessel's interpolation requires an odd number of points
        if (size % 2 != 0) {
            logger.warn("Bessel's interpolation requires an odd number of data points.");
            return;
        }

        // Assuming yVal is an ArrayList<Double> and y_diff is a double[]
        double[] y_diff = new double[yVal.size()];
        for (int i = 0; i < yVal.size(); i++) {
            y_diff[i] = yVal.get(i); // Auto-unboxing from Double to double
        }


        double x_target = arg;
        double h = xVal.get(1) - xVal.get(0);
        int middleIndex = size / 2;  // This is the central index when size is odd

        // Initial interpolated value is the average of central two values
        interpolatedValue = (yVal.get(middleIndex) + yVal.get(middleIndex + 1)) / 2;

        // Bessel interpolation formula
        double p = (x_target - xVal.get(middleIndex)) / h;
        double factorial = 1;
        double p_term = p * (p - 1) / 2.0;  // Initial p term calculation for Bessel using central differences

        for (int i = 1; i <= middleIndex; i++) {
            int start = Math.max(middleIndex - i + 1, 1);  // Ensure that j-1 is never less than 0
            int end = Math.min(middleIndex + i - 1, size - 2);  // Ensure that j+1 is never more than size - 1

            for (int j = start; j <= end; j++) {
                y_diff[j] = (y_diff[j+1] - y_diff[j-1]) / 2;
            }

            if (i % 2 == 1) {  // Only add terms for odd i (1, 3, 5, ...)
                factorial *= i;
                interpolatedValue += p_term * y_diff[middleIndex] / factorial;

                // Update p_term for the next applicable i
                p_term *= (p + i) * (p - i - 1);
                factorial *= (i + 1);
            }
        }
    }

    @Override
    public String getNameMethod() {
        return "Bessel's\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        if (size % 2 != 0) {
            return "Bessel's\\ interpolation\\ requires\\ an\\ even\\ number\\ of\\ data\\ points.";
        }
        return getNameMethod() + String.format("\\\\ Interpolated\\ value\\ at\\ x\\ =\\ %.2f\\ is\\ y\\ =\\ %.2f", this.arg, interpolatedValue);
    }
}
