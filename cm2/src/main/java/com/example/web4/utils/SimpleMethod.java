package com.example.web4.utils;

import com.example.web4.dto.Coordinates;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleMethod {
    Coordinates coordinates1;
    private double fi(double x, double lambda){
        return x+lambda*coordinates1.getValue(x);
    }
    public double fiDev(double point, double lambda) {
        return (fi(point + 0.0001, lambda) - fi(point - 0.0001, lambda)) / (2 * 0.0001);
    }
    public String simple(Coordinates coordinates) {
        coordinates1 = coordinates;
        double a = coordinates.getA();
        double b = coordinates.getB();
        double epsilon = coordinates.getEps();
        List<IterationsForSimple> iterations = new ArrayList<>();
        double lambda;
        if(coordinates.getDev(1, a)<0 && coordinates.getDev(1, b)<0){
            lambda = 1/(Math.max(Math.abs(coordinates.getDev(1, a)), Math.abs(coordinates.getDev(1, b))));
        }else{
            lambda = -1/(Math.max(Math.abs(coordinates.getDev(1, a)), Math.abs(coordinates.getDev(1, b))));
        }

        if(fiDev(a, lambda)>1 || fiDev(b, lambda)>1){
            System.out.println("нет сходимости ваще");
            return "Не соблюдается достаточное условие сходимости";
        }

        double x = a;
        int iteration = 0;
        while (true){
            IterationsForSimple data = new IterationsForSimple();
            data.iteration = iteration++;
            data.x = x;
            double xNew = fi(x, lambda);
            data.xNew = xNew;
            data.fi = fi(xNew, lambda);
            data.f = coordinates.getValue(xNew);
            data.absX = Math.abs(xNew - x);
            if(Math.abs(xNew - x)<epsilon){
                break;
            }
            x = xNew;
            iterations.add(data);
        }

        System.out.printf("The root is %.6f\n", x);

        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("tmp.json")) {
            writer.write(gson.toJson(iterations));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "The root is " + x+ "; f(x) = "+coordinates.getValue(x)+"; iter: "+iteration;
    }
}
