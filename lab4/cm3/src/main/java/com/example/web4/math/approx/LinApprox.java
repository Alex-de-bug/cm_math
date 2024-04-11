package com.example.web4.math.approx;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;

import java.util.ArrayList;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Getter
public class LinApprox extends Method {
    private Double a;
    private Double b;

    private static final Logger logger = LoggerFactory.getLogger(LinApprox.class);

    public LinApprox(RequestFuncUser requestFuncUser) {
        super(requestFuncUser);
    }
    private double f(double x){
        return a*x+b;
    }

    @Override
    public void calculate(){
        double sumX = 0;
        double sumXX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumYY = 0;
        int n = requestFuncUser.getSliderValue();
        for (PointDto pointDto : requestFuncUser.getPoints()){
            sumX += pointDto.getX();
            sumXX += Math.pow(pointDto.getX(), 2);
            sumY += pointDto.getY();
            sumXY += pointDto.getX()*pointDto.getY();
            sumYY += pointDto.getY() * pointDto.getY();
        }
        a = (sumXY*n - sumX*sumY)/(sumXX*n - sumX*sumX);
        b = (sumXX*sumY - sumX*sumXY)/(sumXX*n - sumX*sumX);

        logger.info("lin: a = "+a+"; b = "+b);

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

        korrelPirs = (sumXY * n - sumX * sumY) / Math.sqrt((sumXX * n - sumX * sumX) * (sumYY * n - sumY * sumY));
        determ = Math.pow(korrelPirs ,2);
    }

    @Override
    protected String getNameMethod() {
        return "Линейная\\ аппроксимация\\\\";
    }

    @Override
    protected String getStringFun() {
//        a*x+b;
        return "\\phi (x)="+formatScientificNotation(a.toString())+"\\cdot x+"+formatScientificNotation(b.toString());
    }
}
