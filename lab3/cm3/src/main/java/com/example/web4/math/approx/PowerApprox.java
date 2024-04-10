package com.example.web4.math.approx;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
@Getter
public class PowerApprox extends Method {
    private Double a;
    private Double b;

    private static final Logger logger = LoggerFactory.getLogger(PowerApprox.class);

    public PowerApprox(RequestFuncUser requestFuncUser) {
        super(requestFuncUser);
    }

    private double f(double x) {
        return a * Math.pow(x, b);
    }

    @Override
    public void calculate() {
        int n = requestFuncUser.getPoints().size();
        double sumLnX = 0;
        double sumLnY = 0;
        double sumLnX2 = 0;
        double sumLnXLnY = 0;

        for (PointDto point : requestFuncUser.getPoints()) {
            double lnX = Math.log(point.getX());
            double lnY = Math.log(point.getY());

            sumLnX += lnX;
            sumLnY += lnY;
            sumLnX2 += lnX * lnX;
            sumLnXLnY += lnX * lnY;
        }

        // Расчет коэффициентов для линеаризованной модели
        b = (n * sumLnXLnY - sumLnX * sumLnY) / (n * sumLnX2 - sumLnX * sumLnX);
        double lnA = (sumLnY - b * sumLnX) / n;

        this.a = Math.exp(lnA);
        this.b = b;
        logger.info("power: a = "+a+"; b = "+b);

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
        return "Степенная\\ аппроксимация\\\\";
    }

    @Override
    protected String getStringFun() {
//        a * Math.pow(x, b);
        return "\\phi (x)="+formatScientificNotation(a.toString())+"\\cdot x^{"+formatScientificNotation(b.toString())+"}";
    }
}
