package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Gauss extends Method {
    private static final Logger logger = LoggerFactory.getLogger(Gauss.class);
    private double interpolatedValue = 0.0;
    private ArrayList<Double> coefficients = new ArrayList<>();
    private ArrayList<Double> nodes = new ArrayList<>();
    private double tmp = 0.0;

    public Gauss(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

    @Override
    public void calculate() {
        double[] yDiff = new double[size];
        for (int i = 0; i < size; i++) {
            yDiff[i] = yVal.get(i);
        }

        double x_target = arg;
        double h = xVal.get(1) - xVal.get(0);
        int n = (size - 1) / 2;
        int nearestIndex = findNearestIndex(x_target);

        double p = (x_target - xVal.get(nearestIndex)) / h;
        interpolatedValue = yDiff[nearestIndex];

        if (nearestIndex <= n) {
            logger.info("Forward interpolation");
            for (int i = 1; i <= n; i++) {
                for (int j = nearestIndex; j < size - i; j++) {
                    yDiff[j] = yDiff[j + 1] - yDiff[j];
                }
                double term = p;
                for (int k = 1; k < i; k++) {
                    term *= (p - k) / (k + 1);
                }
                interpolatedValue += term * yDiff[nearestIndex];
            }
        } else {
            logger.info("Backward interpolation");
            for (int i = 1; i <= n; i++) {
                for (int j = nearestIndex; j >= i; j--) {
                    yDiff[j] = yDiff[j] - yDiff[j - 1];
                }
                double term = p;
                for (int k = 1; k < i; k++) {
                    term *= (p + k) / (k + 1);
                }
                interpolatedValue += term * yDiff[nearestIndex - i + 1];
            }
        }
        nodesForGraph();
    }

    private void nodesForGraph() {
        double[] yDiff = new double[size];
        for (int i = 0; i < size; i++) {
            yDiff[i] = yVal.get(i);
        }
        double xTarget = arg;
        for (int i = 0; i < size; i++) {
            coefficients.add(yDiff[i]);
            nodes.add(xVal.get(i));

            for (int j = size - 1; j > i; j--) {
                yDiff[j] = (yDiff[j] - yDiff[j - 1]) / (xVal.get(j) - xVal.get(j - i - 1));
            }
        }
        for (int i = size - 1; i >= 0; i--) {
            tmp = tmp * (xTarget - xVal.get(i)) + coefficients.get(i);
        }
    }

    private int findNearestIndex(double x_target) {
        double minDiff = Double.MAX_VALUE;
        int nearestIndex = 0;
        for (int i = 0; i < size; i++) {
            double diff = Math.abs(xVal.get(i) - x_target);
            if (diff < minDiff) {
                nearestIndex = i;
                minDiff = diff;
            }
        }
        return nearestIndex;
    }


    public String getPolynomial() {
        StringBuilder poly = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {

            if (coefficients.get(i) < 0) {
                poly.append(" + ").append(formatScientificNotation(coefficients.get(i).toString()));
            } else {
                if (i != coefficients.size() - 1) poly.append(" + ");
                poly.append(String.format(formatScientificNotation(coefficients.get(i).toString())));
            }
            for (int j = 0; j < i; j++) {
                poly.append("\\cdot (x - ").append(formatScientificNotation(nodes.get(j).toString())).append(")");
            }
        }
        return poly.toString();
    }


    @Override
    public String getNameMethod() {
        return "Gauss\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ " + this.arg + "\\ is\\ y\\ =\\ " + tmp;
    }
}
