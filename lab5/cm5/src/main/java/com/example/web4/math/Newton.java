package com.example.web4.math;

import java.util.ArrayList;

public class Newton extends Method {
    private double interpolatedValue = 0.0;
    private ArrayList<ArrayList<Double>> dividedDifferences;
    private String polynomialString = "";

    public Newton(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
        this.dividedDifferences = new ArrayList<>();
    }

    @Override
    public void calculate() {
        initializeDividedDifferenceTable();
        computeDividedDifferences();

        double v = this.arg;
        double sum = this.yVal.get(0);
        for (int i = 1; i < this.size; i++) {
            double term = 1.0;
            for (int j = 0; j < i; j++) {
                term *= (v - this.xVal.get(j));
            }
            sum += term * this.dividedDifferences.get(0).get(i);
        }
        this.interpolatedValue = sum;
    }

    private void initializeDividedDifferenceTable() {
        for (int i = 0; i < this.size; i++) {
            dividedDifferences.add(new ArrayList<>());
            dividedDifferences.get(i).add(this.yVal.get(i));
        }
    }

    private void computeDividedDifferences() {
        for (int i = 1; i < this.size; i++) {
            for (int j = 0; j < this.size - i; j++) {
                double diff = dividedDifferences.get(j + 1).get(i - 1) - dividedDifferences.get(j).get(i - 1);
                double denom = this.xVal.get(j + i) - this.xVal.get(j);
                double dividedDifference = diff / denom;
                dividedDifferences.get(j).add(dividedDifference);
            }
        }
    }

    public String buildPolynomialString() {
        StringBuilder polyBuilder = new StringBuilder();
        polyBuilder.append(formatScientificNotation(dividedDifferences.get(0).get(0).toString()));
        for (int i = 1; i < this.size; i++) {
            polyBuilder.append(" + (");
            polyBuilder.append(formatScientificNotation(dividedDifferences.get(0).get(i).toString()));
            for (int j = 0; j < i; j++) {
                polyBuilder.append(" * (x - ");
                polyBuilder.append(this.xVal.get(j).toString());
                polyBuilder.append(")");
            }
            polyBuilder.append(")");
        }
        this.polynomialString = polyBuilder.toString();
        String s = polyBuilder.toString().replaceAll("\\s\\+\\s-", " - ");
        return s;
    }

    @Override
    public String getNameMethod() {
        return "Newton's\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ " + this.arg + " \\ is\\ y\\ =\\ " + this.interpolatedValue;
    }
}
