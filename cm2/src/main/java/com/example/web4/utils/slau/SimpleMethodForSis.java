package com.example.web4.utils.slau;

import com.example.web4.dto.Coordinates;
import com.example.web4.utils.IterationsForBisection;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleMethodForSis {
    public static void simpl(Coordinates coordinates) {
        double a = coordinates.getA();
        double b = coordinates.getB();
        double epsilon = coordinates.getEps();
        IterationsForSis iterations = new IterationsForSis();
        double pdv11 = coordinates.getFiSisPdv(a, b, 1);
        double pdv12 = coordinates.getFiSisPdv(a, b, 2);
        double pdv21 = coordinates.getFiSisPdv(a, b, 3);
        double pdv22 = coordinates.getFiSisPdv(a, b, 4);

        if ((Math.abs(pdv11)+Math.abs(pdv12)>=1) || (Math.abs(pdv21)+Math.abs(pdv22)>=1)) {
            System.out.println("Incorrect initial [a, b].");
            return;
        }

        double x = a;
        double y = b;
        int iteration = 0;
        while (true) {
            iteration++;
            double newX = coordinates.getFiSis(x, y, 1);
            double newY = coordinates.getFiSis(x, y, 2);

            // Check for convergence criteria
            if ((Math.max(Math.abs(newX - x), Math.abs(newY - y)) < epsilon)||(iteration>100)){
                iterations.absX = Math.abs(newX - x);
                iterations.absY = Math.abs(newY - y);
                iterations.iteration = iteration;
                iterations.xAnswer = newX;
                iterations.yAnswer = newY;
                break;
            }
            x = newX;
            y = newY;
            System.out.println(x+ " " +y);
        }
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("tmp.json")) {
            writer.write(gson.toJson(iterations));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The root is x:"+x+" y: "+y);
    }
}
