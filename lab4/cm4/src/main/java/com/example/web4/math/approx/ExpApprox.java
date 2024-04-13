package com.example.web4.math.approx;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import lombok.Getter;
import org.apache.commons.math3.linear.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
@Getter
public class ExpApprox extends Method {
    private Double a;
    private Double b;

    private static final Logger logger = LoggerFactory.getLogger(ExpApprox.class);

    public ExpApprox(RequestFuncUser requestFuncUser) {
        super(requestFuncUser);
    }

    private double f(double x) {
        return a * Math.exp(b * x);
    }

    @Override
    public void calculate() {
        int n = requestFuncUser.getPoints().size();
        double sumX = 0;
        double sumYln = 0;
        double sumX2 = 0;
        double sumXYln = 0;

        for (PointDto point : requestFuncUser.getPoints()) {
            double x = point.getX();
            double y = Math.log(point.getY()); // Линеаризация: используем ln(y) вместо y

            sumX += x;
            sumYln += y;
            sumX2 += x * x;
            sumXYln += x * y;
        }

        // Расчет коэффициентов для линеаризованной модели
        double b = (n * sumXYln - sumX * sumYln) / (n * sumX2 - sumX * sumX);
        double aLn = (sumYln - b * sumX) / n; // ln(a)

        this.a = Math.exp(aLn);
        this.b = b;
        logger.info("exp: a = "+a+"; b = "+b);

        for (PointDto point : requestFuncUser.getPoints()) {
            double fi = f(point.getX());
            double eps = fi - point.getY();
            ArrayList<Double> row = new ArrayList<>();
            row.add(point.getX());
            row.add(point.getY());
            row.add(fi);
            row.add(eps);
            table.add(row);
            S += Math.pow(eps, 2);
        }
        sko = Math.sqrt(S/n);

        double meanY = requestFuncUser.getPoints().stream().mapToDouble(PointDto::getY).average().orElse(0);
        double ssTot = 0;
        double ssRes = 0;

        for (PointDto point : requestFuncUser.getPoints()) {
            double fi = f(point.getX());
            ssTot += Math.pow(point.getY() - meanY, 2);
            ssRes += Math.pow(point.getY() - fi, 2);
        }

        // Расчёт коэффициента детерминации R^2
        determ = 1 - (ssRes / ssTot);

        // Расчёт коэффициента корреляции Пирсона
        korrelPirs = Math.sqrt(determ);
    }

    @Override
    protected String getNameMethod() {
        return "Экспоненциальная\\ аппроксимация\\\\";
    }
    @Override
    protected String getStringFun() {
//        a * Math.exp(b * x);
        return "\\phi (x)="+formatScientificNotation(a.toString())+"\\cdot e^{"+formatScientificNotation(b.toString())+"\\cdot x}";
    }
}