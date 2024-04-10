package com.example.web4.validators;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.Functions;
import com.example.web4.math.methods.SimpsonMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static java.lang.Double.NaN;

public class DataValidation {
    private static final Logger logger = LoggerFactory.getLogger(DataValidation.class);
    public CalculateError validateAtt(RequestFuncUser requestFuncUser) {

        CalculateError error = isValidInterval(requestFuncUser);
        if(error!=null){
            return error;
        }
        if (isValidEps(requestFuncUser)) {
            return CalculateError.INCORRECT_EPSILON;
        }
        return null;
    }

    private CalculateError isValidInterval(RequestFuncUser requestFuncUser) {
        ArrayList<Double> kritPoints = new Functions().getErrPoints((int) requestFuncUser.getTypeFunc());
        if(requestFuncUser.getA()>requestFuncUser.getB()){
            double tmp = requestFuncUser.getA();
            requestFuncUser.setA(requestFuncUser.getB());
            requestFuncUser.setB(tmp);
        }
        Double a = requestFuncUser.getA();
        Double b = requestFuncUser.getB();
        Functions functions = new Functions();

        if(a.equals(b)) {
            return CalculateError.INCORRECT_BOUNDS_NULL_S;
        }
        for (Double point : kritPoints) {
            logger.info("point > a: "+(point>a)+"; point<b: "+(point<b));
            if (point.equals(a) && !Double.isNaN(functions.f_dx(a, (int) requestFuncUser.getTypeFunc()))
                    && Double.isFinite(functions.f_dx(a, (int) requestFuncUser.getTypeFunc()))) {
                requestFuncUser.setA(a + 0.0000001);
            } else if (point.equals(b) && !Double.isNaN(functions.f_dx(b, (int) requestFuncUser.getTypeFunc()))
                    && Double.isFinite(functions.f_dx(b, (int) requestFuncUser.getTypeFunc()))) {
                requestFuncUser.setB(b - 0.0000001);
            }else if (Double.isNaN(functions.f_dx(a, (int) requestFuncUser.getTypeFunc())) ||
                    Double.isNaN(functions.f_dx(b, (int) requestFuncUser.getTypeFunc()))
                    || Double.isInfinite(functions.f_dx(a, (int) requestFuncUser.getTypeFunc()))
                    || Double.isInfinite(functions.f_dx(b, (int) requestFuncUser.getTypeFunc()))) {
                return CalculateError.INCORRECT_BOUNDS_INT_ERR;
            }
            if(point>a && point<b && (Double.isNaN(functions.f_dx(point, (int) requestFuncUser.getTypeFunc()))
                    || Double.isInfinite(functions.f_dx(point, (int) requestFuncUser.getTypeFunc())) )){
                logger.warn("Интеграл является странным, но будем устранять разрыв, который не является устранимым.");
//                return CalculateError.INCORRECT_BOUNDS_INT_ERR_IN_INTER;
            }
        }
        logger.info("f_dx(a): "+functions.f_dx(a, (int) requestFuncUser.getTypeFunc())+"; f_dx(b): "+functions.f_dx(b, (int) requestFuncUser.getTypeFunc()));
        return null;
    }

    private boolean isValidEps(RequestFuncUser requestFuncUser) {
        return requestFuncUser.getEps() <= 0;
    }
}
