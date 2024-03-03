package com.example.web4.utils;

import com.example.web4.dto.Coordinates;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewtownMethod {
    public static boolean newtown(Coordinates coordinates) {
        double a = coordinates.getA();
        double b = coordinates.getB();
        double epsilon = coordinates.getEps();
        List<IterationsForNewt> iterations = new ArrayList<>();

        // Check the initial interval conditions for the derivatives
        if ((coordinates.getDev(1, a) * coordinates.getDev(1, b) < 0) || (coordinates.getDev(2, a) * coordinates.getDev(2, b) < 0)) {
            System.out.println("Incorrect initial interval [a, b]. f'(a) and f'(b) or f''(a) and f''(b) should have opposite signs.");
            return false;
        }

        double x = b;
        if (coordinates.getValue(a) * coordinates.getDev(2, a) > 0) {
            x = a;
        }

        double newX;
        int iteration = 0;
        int maxIterations = 1000;

        do {
            IterationsForNewt data = new IterationsForNewt();
            data.iteration = iteration++;
            data.xi = x;
            data.f_xi = coordinates.getValue(x);
            data.f1_xi = coordinates.getDev(1, x);

            // Check for division by zero
            if (Math.abs(data.f1_xi) < epsilon) {
                System.out.println("Division by zero in derivative.");
                return false;
            }

            newX = x - (data.f_xi / data.f1_xi);
            data.x_2i = newX;
            data.absX = Math.abs(newX - x);

            iterations.add(data); // If you want to keep track of each iteration

//            System.out.println("Iteration " + iteration + ": x = " + x + ", f(x) = " + data.f_xi + ", f'(x) = " + data.f1_xi+" new x "+ newX);

            if (Math.abs(coordinates.getValue(newX)) < epsilon && Math.abs(newX - x) < epsilon) {
                break;
            }

            x = newX; // Update x for the next iteration



            if (iteration > maxIterations) {
                System.out.println("Max iterations reached without convergence.");
                return false;
            }

        } while (true);

        System.out.printf("The root is %.6f\n", x);
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("tmp.json")) {
            writer.write(gson.toJson(iterations));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}



