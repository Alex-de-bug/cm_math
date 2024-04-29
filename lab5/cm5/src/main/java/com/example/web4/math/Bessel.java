package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Bessel extends Method {
    private double interpolatedValue = 0.0;
    private double[][] delta;
    private static final Logger logger = LoggerFactory.getLogger(Bessel.class);

    public Bessel(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

//    @Override
//    public void calculate() {
//        int n = xVal.size();
//        double tSub = (arg - xVal.get(n / 2))/(xVal.get(1) - xVal.get(0));
//        if ((size % 2 != 0)||(Math.abs(tSub)>0.75)||(Math.abs(tSub)<0.25)) {
//            logger.info(String.valueOf(tSub));
//            return;
//        }
//        delta = new double[n][n];
//        double result = (yVal.get(n / 2 - 1) + yVal.get(n / 2)) / 2;
//
//        for (int i = 0; i < n; i++) {
//            delta[i][0] = yVal.get(i);
//        }
//
//        for (int i = 1; i < n; i++) {
//            for (int j = 0; j < n - i; j++) {
//                delta[j][i] = delta[j + 1][i - 1] - delta[j][i - 1];
//            }
//        }
//
//        double tProduct = 1;
//        for (int i = 1; i < n; i++) {
//            tProduct *= (i % 2 == 0) ? (tSub - (i / 2)) : (tSub + (i / 2));
//            double coeff = delta[n / 2 - 1][i] + (i % 2 == 0 ? delta[n / 2 - i / 2][i - 1] : delta[n / 2 - i / 2 - 1][i]);
//            result += (tProduct / factorial(i)) * coeff / (i % 2 == 0 ? 2 : 1);
//        }
//
//        interpolatedValue = result;
//    }

    @Override
    public void calculate() {
        int n = xVal.size();
        double tSub = (arg - xVal.get(n / 2))/(xVal.get(1) - xVal.get(0));
        if ((size % 2 != 0)||(Math.abs(tSub)>0.75)||(Math.abs(tSub)<0.25)) {
            logger.info(String.valueOf(tSub));
            return;
        }
        delta = new double[n][n];
        double result = (yVal.get(n / 2 - 1) + yVal.get(n / 2)) / 2;

        for (int i = 0; i < n; i++) {
            delta[i][0] = yVal.get(i);
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                delta[j][i] = delta[j + 1][i - 1] - delta[j][i - 1];
            }
        }

        double tProduct = 1;
        for (int i = 1; i < n; i++) {
            tProduct *= (i % 2 == 0) ? (tSub - (i / 2)) : (tSub + (i / 2));
            double coeff = delta[n / 2 - 1][i] + (i % 2 == 0 ? delta[n / 2 - i / 2][i - 1] : delta[n / 2 - i / 2 - 1][i]);
            result += (tProduct / factorial(i)) * coeff / (i % 2 == 0 ? 2 : 1);
        }

        interpolatedValue = result;
    }

    public double estimateError() {
        int n = xVal.size();
        if (n % 2 != 0) {
            logger.error("Ошибка: Интерполяционный многочлен Бесселя строится только по чётному количеству точек.");
            return Double.NaN;
        }

        double h = xVal.get(1) - xVal.get(0); // Равномерный шаг предполагается между узлами
        double tSub = (arg - xVal.get(n / 2 - 1)) / h;
        if (Math.abs(tSub) > 0.75 || Math.abs(tSub) < 0.25) {
            logger.error("Ошибка: Значение t не входит в интервал [0.25; 0.75], Бессель даёт большую погрешность.");
            return Double.NaN;
        }

        // Используем последнюю доступную разность для оценки погрешности
        double lastDelta = delta[0][n - 1];
        return Math.abs(lastDelta * Math.pow(h, n)) / factorial(n - 1)*100;
    }


    @Override
    public String getNameMethod() {
        return "Bessel's\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        int n = xVal.size();
        if (n % 2 != 0) {
            return "Интерполяционный\\ многочлен\\ Бесселя\\ строится\\ только\\ по\\ чётному\\ количетсву\\ точек.";
        }

        double tSub = (arg - xVal.get(n / 2))/(xVal.get(1) - xVal.get(0));
        if ((Math.abs(tSub)>0.75)||(Math.abs(tSub)<0.25)) {
            return "Так\\ как\\ t\\ не\\ входит\\ в\\ интервал\\ [0.25; 0.75],\\ то\\ Бессель\\ даёт\\ большую\\ погрешность.";
        }
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ "+this.arg+" \\ is\\ y\\ =\\ " +this.interpolatedValue + "\\\\ Примерная\\ погрешность: "+ formatScientificNotation(String.valueOf(estimateError()))+"\\ \\%";
    }
}
