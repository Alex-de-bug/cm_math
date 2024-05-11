package com.example.web4.math;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveRungeKutta4 extends Method{
    public List<String> solve(DifferentialEquation eq, double x0, double y0, double xn, double h, double eps) {
        List<String> points = new ArrayList<>();
        double y = y0;
        double x = x0;
        points.add("("+formatScientificNotation(x)+", "+formatScientificNotation(y)+")");

        while (x < xn) {
            if (x + h > xn) h = xn - x;  // Адаптируем последний шаг к границе интервала
            double k1 = h * eq.eval(x, y);
            double k2 = h * eq.eval(x + h / 2, y + k1 / 2);
            double k3 = h * eq.eval(x + h / 2, y + k2 / 2);
            double k4 = h * eq.eval(x + h, y + k3);
            double yNext = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;

            double k1H = 0.5 * h * eq.eval(x, y);
            double k2H = 0.5 * h * eq.eval(x + h / 4, y + k1 / 2);
            double k3H = 0.5 * h * eq.eval(x + h / 4, y + k2 / 2);
            double k4H = 0.5 * h * eq.eval(x + h / 2, y + k3);
            double yHalfStep = y + (k1H + 2 * k2H + 2 * k3H + k4H) / 6;

            double R = Math.abs(yNext - yHalfStep) / (Math.pow(2, 4) - 1);  // Рассчитываем R по правилу Рунге

            if (R <= eps) {
                y = yNext;
                x += h;
                points.add("("+formatScientificNotation(x)+", "+formatScientificNotation(y)+")");
            }
            h *= R > eps ? 0.5 : 1.25;  // Адаптация шага
        }

        return points;
    }

    @Override
    public String getNameMethod() {
        return "Рунге Кутты 2 порядка";
    }
}
