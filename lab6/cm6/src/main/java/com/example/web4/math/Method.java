package com.example.web4.math;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public abstract class Method {
    private static final Logger logger = LoggerFactory.getLogger(Method.class);

    public Method() {

    }


    public abstract List<String> solve(DifferentialEquation eq, double x0, double y0, double xn, double h, double eps);

    protected String formatScientificNotation(double value) {
        String tmp = Double.toString(value);
        if (tmp.contains("E")) {
            return tmp.replace("E", "\\cdot10 ^{") + "}";
        } else {
            return tmp;
        }
    }

    public abstract String getNameMethod();

}
