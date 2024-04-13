package com.example.web4.math.approx;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import lombok.Getter;
import org.apache.commons.math3.linear.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
@Getter

public class CubicApprox extends Method{
    private Double a;
    private Double b;
    private Double c;
    private Double d;

    private static final Logger logger = LoggerFactory.getLogger(CubicApprox.class);

    public CubicApprox(RequestFuncUser requestFuncUser) {
        super(requestFuncUser);
    }

    private double f(double x) {
        return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
    }

    @Override
    public void calculate() {
        int n = requestFuncUser.getPoints().size();
        double[][] matrixData = new double[4][4];
        double[] vector = new double[4];

        for (PointDto point : requestFuncUser.getPoints()) {
            double x = point.getX();
            double y = point.getY();
            double x2 = Math.pow(x, 2);
            double x3 = Math.pow(x, 3);
            double x4 = Math.pow(x, 4);
            double x5 = Math.pow(x, 5);
            double x6 = Math.pow(x, 6);

            // Заполнение матрицы коэффициентов
            matrixData[0][0] += x6;
            matrixData[0][1] += x5;
            matrixData[0][2] += x4;
            matrixData[0][3] += x3;
            matrixData[1][0] += x5;
            matrixData[1][1] += x4;
            matrixData[1][2] += x3;
            matrixData[1][3] += x2;
            matrixData[2][0] += x4;
            matrixData[2][1] += x3;
            matrixData[2][2] += x2;
            matrixData[2][3] += x;
            matrixData[3][0] += x3;
            matrixData[3][1] += x2;
            matrixData[3][2] += x;
            matrixData[3][3] += 1;

            // Заполнение вектора констант
            vector[0] += y * x3;
            vector[1] += y * x2;
            vector[2] += y * x;
            vector[3] += y;
        }

        RealMatrix coefficients = new Array2DRowRealMatrix(matrixData, false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(vector, false);
        RealVector solution = solver.solve(constants);

        a = solution.getEntry(0);
        b = solution.getEntry(1);
        c = solution.getEntry(2);
        d = solution.getEntry(3);
        logger.info("cubic: a = "+a+"; b = "+b+"; c = "+c+"; d = "+d);

        for (PointDto pointDto : requestFuncUser.getPoints()){
            ArrayList<Double> tmp = new ArrayList<>();
            tmp.add(pointDto.getX());
            tmp.add(pointDto.getY());
            tmp.add(f(pointDto.getX()));
            tmp.add(f(pointDto.getX()) - pointDto.getY());
            table.add(tmp);
            S += Math.pow(f(pointDto.getX()) - pointDto.getY(), 2);
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
        return "Кубическая\\ аппроксимация\\\\";
    }

    @Override
    protected String getStringFun() {
//        a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
        return "\\phi (x)="+formatScientificNotation(a.toString())+"\\cdot x^3 +"+formatScientificNotation(b.toString())+"\\cdot x^2+"+formatScientificNotation(c.toString())+"\\cdot x +"+formatScientificNotation(d.toString());
    }
}