package com.example.web4.math.approx;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.apache.commons.math3.linear.*;
@Getter
public class QuadApprox extends Method{
    private Double a;
    private Double b;
    private Double c;

    private static final Logger logger = LoggerFactory.getLogger(QuadApprox.class);

    public QuadApprox(RequestFuncUser requestFuncUser) {
        super(requestFuncUser);
    }

    private double f(double x) {
        return a * Math.pow(x, 2) + b * x + c;
    }

    @Override
    public void calculate() {
        int n = requestFuncUser.getPoints().size();
        double[] terms = new double[3];
        double[][] matrixData = new double[3][3];
        double[] vector = new double[3];

        for (PointDto point : requestFuncUser.getPoints()) {
            double x = point.getX();
            double y = point.getY();
            terms[0] += Math.pow(x, 4);
            terms[1] += Math.pow(x, 3);
            terms[2] += Math.pow(x, 2);

            matrixData[0][0] += Math.pow(x, 4);
            matrixData[0][1] += Math.pow(x, 3);
            matrixData[0][2] += Math.pow(x, 2);
            matrixData[1][0] += Math.pow(x, 3);
            matrixData[1][1] += Math.pow(x, 2);
            matrixData[1][2] += x;
            matrixData[2][0] += Math.pow(x, 2);
            matrixData[2][1] += x;
            matrixData[2][2] += 1;

            vector[0] += x * x * y;
            vector[1] += x * y;
            vector[2] += y;
        }

        RealMatrix coefficients = new Array2DRowRealMatrix(matrixData, false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(vector, false);
        RealVector solution = solver.solve(constants);

        a = solution.getEntry(0);
        b = solution.getEntry(1);
        c = solution.getEntry(2);
        logger.info("quad: a = "+a+"; b = "+b+"; c = "+c);

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
        return "Квадратичная\\ аппроксимация\\\\";
    }
    @Override
    protected String getStringFun() {
//        a * Math.pow(x, 2) + b * x + c;
        return "\\phi (x)="+formatScientificNotation(a.toString())+"\\cdot x^2+"+formatScientificNotation(b.toString())+"\\cdot x+"+formatScientificNotation(c.toString());
    }
}
