package com.example.web4.math.approx;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import lombok.Getter;
import org.apache.commons.math3.linear.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
@Getter
public class LogApprox extends Method {
    private Double a;
    private Double b;


    private static final Logger logger = LoggerFactory.getLogger(LogApprox.class);

    public LogApprox(RequestFuncUser requestFuncUser) {
        super(requestFuncUser);
    }

    private double f(double x) {
        return a * Math.log(x) + b;
    }

    @Override
    public void calculate() {
        int n = requestFuncUser.getPoints().size();
        double sumLnX = 0;
        double sumY = 0;
        double sumLnX2 = 0;
        double sumYLnX = 0;

        for (PointDto point : requestFuncUser.getPoints()) {
            double x = point.getX();
            double y = point.getY();
            double lnX = Math.log(x);

            sumLnX += lnX;
            sumY += y;
            sumLnX2 += lnX * lnX;
            sumYLnX += y * lnX;
        }

        // Расчет коэффициентов для логарифмической модели
        double b = (sumY * sumLnX2 - sumLnX * sumYLnX) / (n * sumLnX2 - sumLnX * sumLnX);
        double a = (n * sumYLnX - sumLnX * sumY) / (n * sumLnX2 - sumLnX * sumLnX);

        this.a = a;
        this.b = b;
        logger.info("log: a = "+a+"; b = "+b);

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
        return "Логарифмическая\\ аппроксимация\\\\";
    }
    @Override
    protected String getStringFun() {
//        a * Math.log(x) + b;
        return "\\phi (x)="+formatScientificNotation(a.toString())+"\\cdot ln(x)+"+formatScientificNotation(b.toString());
    }
}
