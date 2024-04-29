package com.example.web4.math;

import com.example.web4.dto.RequestFuncUser;

import java.util.ArrayList;

public class Tracing extends Method {

    private final RequestFuncUser requestFuncUser;

    public Tracing(RequestFuncUser requestFuncUser) {
        super(requestFuncUser.getPoints().size(), requestFuncUser.getVal(), null, null);
        this.requestFuncUser = requestFuncUser;
    }

    private Double F(Double x) {
        return switch (requestFuncUser.getFunction()) {
            case 0 -> 3 * Math.sin(x) + Math.cos(8 * x) + x;
            case 1 -> Math.cos(8 * x);
            default -> Math.random();
        };
    }

    @Override
    public void calculate() {
        this.xVal = new ArrayList<>();
        this.yVal = new ArrayList<>();
        for (Double i = requestFuncUser.getA(); i <= requestFuncUser.getB(); i += requestFuncUser.getStep()) {
            this.xVal.add(i);
            this.yVal.add(F(i));
        }
    }

    @Override
    public String getNameMethod() {
        return "Трассировка функции";
    }

    @Override
    public String getAnswer() {
        return null;
    }

    public Integer getSize() {
        return xVal.size();
    }

    public ArrayList<Double> getX() {
        return (ArrayList<Double>) xVal.clone();
    }

    public ArrayList<Double> getY() {
        return (ArrayList<Double>) yVal.clone();
    }
}
