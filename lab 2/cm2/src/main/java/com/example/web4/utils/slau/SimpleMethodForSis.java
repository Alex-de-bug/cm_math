package com.example.web4.utils.slau;

import com.example.web4.dto.Coordinates;
import com.example.web4.utils.IterationsForBisection;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleMethodForSis {
    public static String simpl(Coordinates coordinates) {
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
            return "Incorrect initial [a, b]. Не соблюдается достаточное условие сходимости.";
        }

        double x = a;
        double y = b;
        int iteration = 0;
        String last;
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
                last = "|x_1^{k} - x_1^{k-1}| = "+Math.abs(newX - x)+ "; |x_2^{k} - x_2^{k-1}| = "+Math.abs(newY - y);
                break;
            }
            x = newX;
            y = newY;
        }
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("tmp.json")) {
            writer.write(gson.toJson(iterations));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The root is x:"+x+" y: "+y);
        return "Вектор неизвестных: ("+x+", "+y+")^T; \n"+
                "Итерации: "+iteration+"; \n"+
                "Последняя проверка разницы: "+ last+";\n"+
                "Проверка правильности: f_1(x_1, x_2) = "+ coordinates.getValueSis(x,y,1)+", должно f_1(x_1, x_2) = 0; \n"+
                "Проверка правильности: f_2(x_1, x_2) = "+ coordinates.getValueSis(x,y,2)+", должно f_2(x_1, x_2) = 0; \n";
    }
}
