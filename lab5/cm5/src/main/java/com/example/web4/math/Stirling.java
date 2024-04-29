package com.example.web4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Stirling extends Method {
    private double interpolatedValue = 0.0;
    private double[] deltas;
    private static final Logger logger = LoggerFactory.getLogger(Stirling.class);

    public Stirling(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal, ArrayList<ArrayList<Double>> defy) {
        super(size, arg, xVal, yVal);
        this.defy = defy;
    }

    @Override
    public void calculate() {
        int n = xVal.size();
        double tSub = (arg - xVal.get(n / 2))/(xVal.get(1) - xVal.get(0));
        logger.info(String.valueOf(tSub));
        if ((size % 2 == 0)||(Math.abs(tSub) > 0.25)) {
            return;
        }
        deltas = new double[n];
        double factorial = 1;
        double result = yVal.get(n / 2);
        for (int i = 0; i < n; i++) {
            deltas[i] = yVal.get(i);
        }
        for (int i = 1; i < n; i++) {
            for (int j = n - 1; j >= i; j--) {
                deltas[j] = deltas[j] - deltas[j - 1];
            }
        }
        for (int i = 1; i <= n / 2; i++) {
            factorial *= i;
            double tPower = Math.pow(tSub, 2 * i);
            double deltaSum = deltas[2 * i - 1] + deltas[2 * i];
            result += (tPower / (factorial * factorial)) * deltaSum / 2;
            if (i % 2 == 0) {
                tPower *= (tSub - ( i / 2) * (tSub / i));
                result -= (tPower / (2 * factorial * factorial)) * deltas[2 * i];
            }
        }

        interpolatedValue = result;
    }

    public double estimateError() {
        int n = xVal.size();
        if (n % 2 == 0) {
            logger.error("Ошибка: Интерполяционный многочлен Стирлинга строится только по нечётному количеству точек.");
            return Double.NaN;
        }

        double h = xVal.get(1) - xVal.get(0); // Равномерный шаг предполагается между узлами
        double tSub = (arg - xVal.get(n / 2)) / h;
        if (Math.abs(tSub) > 0.25) {
            logger.error("Ошибка: Стирлинг даёт большую погрешность.");
            return Double.NaN;
        }

        // Используем последнюю доступную разность для оценки погрешности
        // Получаем последнюю значимую разность из массива deltas, которая находится в предпоследнем столбце таблицы разностей.
        double lastDelta = deltas[n - 1];
        return Math.abs(lastDelta * Math.pow(h, n)) / factorial(n)*100;
    }






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
        double tSub = (arg - xVal.get(n / 2))/(xVal.get(1) - xVal.get(0));
        if (Math.abs(tSub)>0.25) {
            return "Так\\ как\\ t\\ >\\ 0.25,\\ Стирлинг\\ даёт\\ большую\\ погрешность.";
        }
        return getNameMethod() + "\\\\ Interpolated\\ value\\ at\\ x\\ =\\ "+this.arg+" \\ is\\ y\\ =\\ " +this.interpolatedValue + "\\\\ Примерная\\ погрешность: "+ formatScientificNotation(String.valueOf(estimateError()))+"\\ \\%";


    }
}
