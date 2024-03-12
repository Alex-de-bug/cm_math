package com.example.web4.utils;

import com.example.web4.dto.Coordinates;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BisectionMethod {

    public static String bisection(Coordinates coordinates) {
        double a = coordinates.getA();
        double b = coordinates.getB();
        double epsilon = coordinates.getEps();
        List<IterationsForBisection> iterations = new ArrayList<>();

        if (coordinates.getValue(a) * coordinates.getValue(b) >= 0) {
            System.out.println("Incorrect initial interval [a, b]. f(a) and f(b) should have similar signs.");
            return "Incorrect initial interval [a, b]. f(a) and f(b) should have similar signs.";
        }

        int iteration = 0;

        double c = a;
        while ((b-a) >= epsilon) {
            IterationsForBisection data = new IterationsForBisection();
            data.iteration = iteration++;
            data.a = a;
            data.b = b;
            // Find middle point
            c = (a + b) / 2;
            data.x = c;
            data.fA = coordinates.getValue(a);
            data.fB = coordinates.getValue(b);
            data.fX = coordinates.getValue(c);
            data.absAB = Math.abs(a-b);
            iterations.add(data);
            if (coordinates.getValue(c) == 0.0)
                break;
            else if (coordinates.getValue(c) * coordinates.getValue(a) < 0)
                b = c;
            else
                a = c;
            if (Math.abs(coordinates.getValue(c)) <= epsilon && Math.abs(b - a) < epsilon)
                break;
        }
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("tmp.json")) {
            writer.write(gson.toJson(iterations));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("The root is %.6f\n", c);
        return "The root is " + c+ "; f(x) = "+coordinates.getValue(c)+"; iter: "+iteration;
    }
}
