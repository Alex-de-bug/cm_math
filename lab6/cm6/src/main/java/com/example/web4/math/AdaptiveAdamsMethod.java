package com.example.web4.math;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveAdamsMethod extends Method {
    private static final int ORDER = 4;

    public List<String> solve(DifferentialEquation eq, double x0, double y0, double xn, double h, double eps) {
        List<String> points = new ArrayList<>();
        double[] y = new double[ORDER];
        double[] x = new double[ORDER];
        y[0] = y0;
        x[0] = x0;
        points.add("(" + formatScientificNotation(x[0]) + ", " + formatScientificNotation(y[0]) + ")");
        for (int i = 1; i < ORDER; i++) {
            double h1 = h;
            boolean acceptStep = false;
            while (!acceptStep) {
                // Первый проход с полным шагом
                double k1 = h1 * eq.eval(x[i-1], y[i-1]);
                double k2 = h1 * eq.eval(x[i-1] + h1 / 2, y[i-1] + k1 / 2);
                double k3 = h1 * eq.eval(x[i-1] + h1 / 2, y[i-1] + k2 / 2);
                double k4 = h1 * eq.eval(x[i-1] + h1, y[i-1] + k3);
                double yNext = y[i-1] + (k1 + 2 * k2 + 2 * k3 + k4) / 6;

                // Второй проход с половинным шагом
                double halfStep = h1 / 2;
                double k1_half = halfStep * eq.eval(x[i-1], y[i-1]);
                double k2_half = halfStep * eq.eval(x[i-1] + halfStep / 2, y[i-1] + k1_half / 2);
                double k3_half = halfStep * eq.eval(x[i-1] + halfStep / 2, y[i-1] + k2_half / 2);
                double k4_half = halfStep * eq.eval(x[i-1] + halfStep, y[i-1] + k3_half);
                double yHalfStep = y[i-1] + (k1_half + 2 * k2_half + 2 * k3_half + k4_half) / 6;

                // Оценка ошибки с использованием правила Рунге
                double R = Math.abs(yNext - yHalfStep) / (Math.pow(2, 4) - 1);

                if (R <= eps) {
                    acceptStep = true;
                    y[i] = yNext;
                    x[i] = x[i-1] + h1;
                    points.add("(" + formatScientificNotation(x[i]) + ", " + formatScientificNotation(y[i]) + ")");
                } else {
                    h1 *= 0.5;
                }
            }
        }

        double maxError = 0;
        while (x[ORDER - 1] < xn) {
            double fI = eq.eval(x[ORDER - 4], y[ORDER - 4]);
            double fI1 = eq.eval(x[ORDER - 3], y[ORDER - 3]);
            double fI2 = eq.eval(x[ORDER - 2], y[ORDER - 2]);
            double fI3 = eq.eval(x[ORDER - 1], y[ORDER - 1]);
            double deltaFI = fI3 - fI2;
            double delta2FI = fI3 - 2*fI2 + fI1;
            double delta3FI = fI3 - 3*fI2 + 3*fI1 - fI;
            double yNext = y[ORDER - 1] + h*fI3 + Math.pow(h, 2)*0.5*deltaFI + 5/12.f*Math.pow(h, 3)*delta2FI + 3/8.f*Math.pow(h, 4)*delta3FI;
            maxError = Math.max(maxError, Math.abs(eq.getYRight(x0, y0, x[ORDER - 1]) - yNext));
            System.arraycopy(x, 1, x, 0, ORDER - 1);
            System.arraycopy(y, 1, y, 0, ORDER - 1);
            x[ORDER - 1] += h;
            y[ORDER - 1] = yNext;
            points.add("(" + formatScientificNotation(x[ORDER - 1]) + ", " + formatScientificNotation(y[ORDER - 1]) + ")");
        }
        if(maxError < eps){
            return points;
        }else return solve(eq,  x0, y0, xn, h*0.5, eps);
    }

    @Override
    public String getNameMethod() {
        return "Метод Адамса с адаптивным шагом";
    }
}


//while (x[ORDER - 1] < xn) {
//        if (x[ORDER - 1] + h > xn) h = xn - x[ORDER - 1];
//double yNext = y[ORDER - 1] + h * (55 * eq.eval(x[ORDER - 1], y[ORDER - 1])
//        - 59 * eq.eval(x[ORDER - 2], y[ORDER - 2])
//        + 37 * eq.eval(x[ORDER - 3], y[ORDER - 3])
//        - 9 * eq.eval(x[ORDER - 4], y[ORDER - 4])) / 24;
//
//double maxError = 0;
//            for (int i = 0; i < ORDER; i++) {
//maxError = Math.max(maxError, Math.abs(eq.getYRight(x0, y0, x[ORDER - 1]) - yNext));
//        }
//
//        if (maxError <= eps) {
//        System.arraycopy(x, 1, x, 0, ORDER - 1);
//                System.arraycopy(y, 1, y, 0, ORDER - 1);
//x[ORDER - 1] += h;
//y[ORDER - 1] = yNext;
//                points.add("(" + formatScientificNotation(x[ORDER - 1]) + ", " + formatScientificNotation(y[ORDER - 1]) + ")");
//        }
//h *= maxError > eps ? 0.5 : 1.25;
//        }