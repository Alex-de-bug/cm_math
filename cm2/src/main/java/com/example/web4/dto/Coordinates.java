package com.example.web4.dto;

import com.example.web4.utils.BisectionMethod;
import com.example.web4.utils.NewtownMethod;
import com.example.web4.utils.SimpleMethod;
import com.example.web4.utils.slau.SimpleMethodForSis;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {
    private double func;
    private double metod;
    private double a;
    private double b;
    private double eps;
    private Boolean file;

    public double getValue(double x){
        return switch ((int) func) {
            case 0 -> x * x * x - 4.5 * x * x - 9.21 * x - 0.383;
            case 1 -> x * x * x - x + 4;
            case 2 -> Math.sin(x) + 0.1;
            default -> Math.random() * 100;
        };
    }
    public double getValueSis(double x, int number){
        if(func == 3){
            return switch (number) {
                case 1 -> x*x*x - x;
                case 2 -> 3 * (x * x) + 12 * x +3;
                default -> Math.random() * 100;
            };
        }else{
            return switch (number) {
                case 1 -> 5 * (x * x) + 4 * x - 7;
                case 2 -> Math.abs(Math.cos(x)+0.3);
                default -> Math.random() * 100;
            };
        }

    }

    public double getFiSis(double x, double y, int number){
        if(func == 3){
            return switch (number) {
                case 1 -> 2-Math.cos(1-y);
                case 2 -> 1-0.5*Math.sin(x);
                default -> Math.random() * 100;
            };
        }else{
            return switch (number) {
                case 1 -> 3+Math.cos(y);
                case 2 -> 0.5-Math.cos(x-1);
                default -> Math.random() * 100;
            };
        }
    }

    public double getFiSisPdv(double x, double y, int number){
        if(func == 3){
            return switch (number) {
                case 1 -> 0;
                case 2 -> -Math.sin(1-y);
                case 3 -> -0.5*Math.cos(x);
                case 4 -> 0;
                default -> Math.random() * 100;
            };
        }else{
            return switch (number) {
                case 1 -> 0;
                case 2 -> -Math.sin(y);
                case 3 -> Math.sin(x-1);
                case 4 -> 0;
                default -> Math.random() * 100;
            };
        }
    }


    public double getDev(int y, double x){
        if(y==1) {
            return switch ((int) func) {
                case 0 -> 3*x*x-9*x-9.21;
                case 1 -> 3*x*x-1;
                case 2 -> Math.cos(x);
                default -> Math.random() * 100;
            };
        }
        return switch ((int) func) {
            case 0 -> 6*x-9;
            case 1 -> 6*x;
            case 2 -> -Math.sin(x);
            default -> Math.random() * 100;
        };
    }

    public void calculate(){
        if(metod == 0){
            BisectionMethod bisectionMethod = new BisectionMethod();
            bisectionMethod.bisection(this);
        }
        if(metod == 1){
            NewtownMethod newtownMethod = new NewtownMethod();
            newtownMethod.newtown(this);
        }
        if(metod == 2){
            SimpleMethod simpleMethod = new SimpleMethod();
            simpleMethod.simple(this);
        }
        if(metod == 3){
            SimpleMethodForSis simpleMethodForSis = new SimpleMethodForSis();
            simpleMethodForSis.simpl(this);
        }

    }

    public boolean checkRootsCount() {
        final int steps = 1000;
        double stepSize = (b - a) / steps;
        boolean hasRoot = false;

        double prevValue = getValue(a);
        for (int i = 1; i <= steps; i++) {
            double x = a + i * stepSize;
            double currentValue = getValue(x);

            if (currentValue * prevValue < 0) {
                if (hasRoot) {
                    return false;
                }
                hasRoot = true;
            }
            prevValue = currentValue;
        }

        return hasRoot;
    }

}
