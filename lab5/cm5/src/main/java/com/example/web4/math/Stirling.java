package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Stirling extends Method {
    private static final Logger logger = LoggerFactory.getLogger(Stirling.class);
    private double interpolatedValue = 0.0;
    private double[] deltas;

    public Stirling(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal, ArrayList<ArrayList<Double>> defy) {
        super(size, arg, xVal, yVal);
        this.defy = defy;
    }

    @Override
    public void calculate() {
        int n = xVal.size();
        double tSub = (arg - xVal.get(n / 2)) / (xVal.get(1) - xVal.get(0));
        logger.info(String.valueOf(tSub));
        if ((size % 2 == 0) || (Math.abs(tSub) > 0.25)) {
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

        double result = defy.get(center).get(0) + t * (defy.get(center - 1).get(1) + defy.get(center).get(1))*0.5 + (Math.pow(t, 2))* 0.5 * defy.get(center - 1).get(2);
        double term = (Math.pow(t, 2))* 0.5;

        for(int k = 3; k < n; k++) {
            if(k % 2 == 0){
                term *= t / k;
                result =result + term * defy.get((center - k/2)).get(k);
            }
             else{
                term *= (Math.pow(t, 2) - (int)Math.pow(k*0.5, 2))/ (k * t);
                result += term * (defy.get((center - k/2 - 1)).get(k) + defy.get((center - k/2)).get(k))*0.5;
            }

        }
        interpolatedValue = result;
    }

//    public double estimateError() {
//        int n = xVal.size();
//        if (n % 2 == 0) {
//            logger.error("Ошибка: Интерполяционный многочлен Стирлинга строится только по нечётному количеству точек.");
//            return Double.NaN;
//        }
//
//        double h = xVal.get(1) - xVal.get(0); // Равномерный шаг предполагается между узлами
//        double tSub = (arg - xVal.get(n / 2)) / h;
//        if (Math.abs(tSub) > 0.25) {
//            logger.error("Ошибка: Стирлинг даёт большую погрешность.");
//            return Double.NaN;
//        }
//
//        // Используем последнюю доступную разность для оценки погрешности
//        // Получаем последнюю значимую разность из массива deltas, которая находится в предпоследнем столбце таблицы разностей.
//        double lastDelta = deltas[n - 1];
//        return Math.abs(lastDelta * Math.pow(h, n)) / factorial(n) * 100;
//    }


    @Override
    public String getNameMethod() {
        return "Stirling's\\ Interpolation";
    }

    @Override
    public String getAnswer() {
        if (size % 2 == 0) {
            return "Многочлен\\ Стирлинга\\ строится\\ только\\ по\\ нечётному\\ количесту\\ узлов.";
        }
        int n = xVal.size();
        double tSub = (arg - xVal.get(n / 2)) / (xVal.get(1) - xVal.get(0));
        if (Math.abs(tSub) > 0.25) {
            return "Так\\ как\\ t\\ >\\ 0.25,\\ Стирлинг\\ даёт\\ большую\\ погрешность.";
        }
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ " + this.arg + " \\ is\\ y\\ =\\ " + this.interpolatedValue; //+ "\\\\ Примерная\\ погрешность: " + formatScientificNotation(String.valueOf(estimateError())) + "\\ \\%";


    }
}
