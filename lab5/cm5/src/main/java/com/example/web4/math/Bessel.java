package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Bessel extends Method {
    private static final Logger logger = LoggerFactory.getLogger(Bessel.class);
    private double interpolatedValue = 0.0;

    public Bessel(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

    @Override
    public void calculate() {
        int n = xVal.size();
        double tSub = (arg - xVal.get(n / 2)) / (xVal.get(1) - xVal.get(0));
        if ((size % 2 != 0) || (Math.abs(tSub) > 0.75) || (Math.abs(tSub) < 0.25)) {
            logger.info(String.valueOf(tSub));
            return;
        }
        for (int i = 0; i < n; i++) {
            defy.get(i).set(0, yVal.get(i));
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                defy.get(j).set(i, defy.get(j + 1).get(i - 1) - defy.get(j).get(i - 1));
            }
        }
        n = xVal.size() - 1;
        int center = n / 2;
        double a = xVal.get(center);
        double t = (arg - a) / (xVal.get(1) - xVal.get(0));
        double result = (defy.get(center).get(0) + defy.get(center + 1).get(0)) * 0.5 + (t - 0.5) * defy.get(center).get(1) + t * (t - 1) * 0.5 * (defy.get(center - 1).get(2) + defy.get(center).get(2)) * 0.5;
        double term = t * (t - 1) / 2;
        for (int k = 3; k < n + 1; k++) {
            if (k % 2 == 0) {
                term /= (t - 0.5);
                term *= (t + (k * 0.5 - 1)) * (t - (k * 0.5)) / k;
                result += term * (defy.get((int) (center - 1 - (k * 0.5 - 1))).get(k) + defy.get((int) (center - (k * 0.5 - 1))).get(k)) / 2;
            } else {
                term *= (t - 0.5) / k;
                result += term * defy.get(center - k / 2).get(k);
            }
        }
        interpolatedValue = result;
    }

//    public double estimateError() {
//        int n = xVal.size();
//        if (n % 2 != 0) {
//            logger.error("Ошибка: Интерполяционный многочлен Бесселя строится только по чётному количеству точек.");
//            return Double.NaN;
//        }
//
//        double h = xVal.get(1) - xVal.get(0); // Равномерный шаг предполагается между узлами
//        double tSub = (arg - xVal.get(n / 2 - 1)) / h;
//        if (Math.abs(tSub) > 0.75 || Math.abs(tSub) < 0.25) {
//            logger.error("Ошибка: Значение t не входит в интервал [0.25; 0.75], Бессель даёт большую погрешность.");
//            return Double.NaN;
//        }
//
//        // Используем последнюю доступную разность для оценки погрешности
//        double lastDelta = delta[0][n - 1];
//        return Math.abs(lastDelta * Math.pow(h, n)) / factorial(n - 1)*100;
//    }


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

        double tSub = (arg - xVal.get(n / 2)) / (xVal.get(1) - xVal.get(0));
        if ((Math.abs(tSub) > 0.75) || (Math.abs(tSub) < 0.25)) {
            return "Так\\ как\\ t\\ не\\ входит\\ в\\ интервал\\ [0.25; 0.75],\\ то\\ Бессель\\ даёт\\ большую\\ погрешность.";
        }
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ " + this.arg + " \\ is\\ y\\ =\\ " + this.interpolatedValue;// + "\\\\ Примерная\\ погрешность: "+ formatScientificNotation(String.valueOf(estimateError()))+"\\ \\%";
    }
}
