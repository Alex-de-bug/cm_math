//package com.example.web4.math;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//
//public class Gauss extends Method {
//    private double interpolatedValue = 0.0;
//    private ArrayList<Double> coefficients = new ArrayList<>();
//    private ArrayList<Double> nodes = new ArrayList<>();
//    public Gauss(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
//        super(size, arg, xVal, yVal);
//    }
//
//    @Override
//    public void calculate() {
//
//
//
//
//        double[] y_diff = new double[size];
//        for (int i = 0; i < size; i++) {
//            y_diff[i] = yVal.get(i);
//        }
//        double x_target = arg;
//        for (int i = 0; i < size; i++) {
//            coefficients.add(y_diff[i]);
//            nodes.add(xVal.get(i));
//
//            for (int j = size - 1; j > i; j--) {
//                y_diff[j] = (y_diff[j] - y_diff[j - 1]) / (xVal.get(j) - xVal.get(j - i - 1));
//            }
//        }
//        interpolatedValue = 0.0;
//        for (int i = size - 1; i >= 0; i--) {
//            interpolatedValue = interpolatedValue * (x_target - xVal.get(i)) + coefficients.get(i);
//        }
//    }
//
//
//    public String getPolynomial() {
//        StringBuilder poly = new StringBuilder();
//        for (int i = coefficients.size() - 1; i >= 0; i--) {
//
//            if (coefficients.get(i) < 0) {
//                poly.append(" + ").append(formatScientificNotation(coefficients.get(i).toString()));
//            } else {
//                if (i != coefficients.size() - 1) poly.append(" + ");
//                poly.append(String.format(formatScientificNotation(coefficients.get(i).toString())));
//            }
//            for (int j = 0; j < i; j++) {
//                poly.append("\\cdot (x - ").append(formatScientificNotation(nodes.get(j).toString())).append(")");
//            }
//        }
//        return poly.toString();
//    }
//
//
//    @Override
//    public String getNameMethod() {
//        return "Gauss\\ Interpolation";
//    }
//
//    @Override
//    public String getAnswer() {
//        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ " + this.arg + "\\ is\\ y\\ =\\ " + interpolatedValue;
//    }
//}






package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Gauss extends Method {
    private double interpolatedValue = 0.0;
    private ArrayList<Double> coefficients = new ArrayList<>();
    private ArrayList<Double> nodes = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(Method.class);

    public Gauss(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

    @Override
    public void calculate() {
        double[] y_diff = new double[size];
        for (int i = 0; i < size; i++) {
            y_diff[i] = yVal.get(i);
        }

        double x_target = arg;
        double h = xVal.get(1) - xVal.get(0);
        int n = (size - 1) / 2;
        int nearestIndex = 0;
        double minDiff = Double.MAX_VALUE;

        // Find nearest point to the target x value
        for (int i = 0; i < size; i++) {
            double diff = Math.abs(xVal.get(i) - x_target);
            if (diff < minDiff) {
                nearestIndex = i;
                minDiff = diff;
            }
        }

        // Decide on forward or backward interpolation based on the nearest index
        double p;
        if (nearestIndex <= n) {
            logger.info("Forward interpolation");
            p = (x_target - xVal.get(nearestIndex)) / h;
            interpolatedValue = y_diff[nearestIndex];
            coefficients.add(y_diff[nearestIndex]);
            nodes.add(xVal.get(nearestIndex));
            for (int i = 1; i <= n; i++) {
                for (int j = nearestIndex; j < size - i; j++) {
                    y_diff[j] = y_diff[j + 1] - y_diff[j];
                }
                double term = p;
                for (int k = 1; k < i; k++) {
                    term *= (p - k) / (k + 1);
                }
                interpolatedValue += term * y_diff[nearestIndex];
                coefficients.add(y_diff[nearestIndex]);
                nodes.add(xVal.get(nearestIndex));
            }
        } else {
            logger.info("Backward interpolation");
            p = (x_target - xVal.get(nearestIndex)) / h;
            interpolatedValue = y_diff[nearestIndex];
            coefficients.add(y_diff[nearestIndex]);
            nodes.add(xVal.get(nearestIndex));
            for (int i = 1; i <= n; i++) {
                for (int j = nearestIndex; j >= i; j--) {
                    y_diff[j] = y_diff[j] - y_diff[j - 1];
                }
                double term = p;
                for (int k = 1; k < i; k++) {
                    term *= (p + k) / (k + 1);
                }
                interpolatedValue += term * y_diff[nearestIndex - i + 1];
                coefficients.add(y_diff[nearestIndex - i + 1]);
                nodes.add(xVal.get(nearestIndex - i + 1));
            }
        }
    }

    public String getPolynomial() {
        StringBuilder poly = new StringBuilder();
        for (int i = 0; i < coefficients.size(); i++) {
            poly.append(coefficients.get(i));
            for (int j = 0; j < i; j++) {
                poly.append("*(x -").append(nodes.get(j)).append(" )");
            }
            if (i < coefficients.size() - 1) {
                poly.append(" + ");
            }
        }
        String s = poly.toString().replaceAll("\\s\\+\\s-", " - ");
        return s;
    }

    @Override
    public String getNameMethod() {
        return "Gauss\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ "+this.arg+" \\ is\\ y\\ =\\ " +interpolatedValue;
    }
}
