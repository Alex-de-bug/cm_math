package com.example.web4.math;

import java.util.ArrayList;
import java.util.List;

public class ImprovedEulerMethod extends Method{
    public List<String> solve(DifferentialEquation eq, double x0, double y0, double xn, double h, double eps) {
        List<String> points = new ArrayList<>();
        double y = y0;
        double x = x0;
        points.add("("+formatScientificNotation(x)+", "+formatScientificNotation(y)+")");
        while (x < xn) {
            if (x + h > xn) h = xn - x;
            double yEulerTilda = y + h * eq.eval(x, y);
            double newY = y + 0.5 * h *(eq.eval(x, y) + eq.eval(x, yEulerTilda));

            double yEulerTildaHalf = y + 0.5 * h * eq.eval(x, y);
            double newYHalf = y + 0.5 * 0.5 * h *(eq.eval(x, y) + eq.eval(x, yEulerTildaHalf));

            double R = Math.abs(newY - newYHalf) / (Math.pow(2, 2) - 1);

            if (R <= eps) {
                y = newY;
                x += h;
                points.add("("+formatScientificNotation(x)+", "+formatScientificNotation(y)+")");
            }
            h *= R > eps ? 0.5 : 1.25;
        }
        return points;
    }

    @Override
    public String getNameMethod() {
        return "Модифицированный метод Эйлера";
    }
}

