package com.example.web4.validators;

import com.example.web4.dto.RequestFuncUser;

public class DataValidation {
    public CalculateError validateAtt(RequestFuncUser requestFuncUser) {
        if (isValidInterval(requestFuncUser)) {
            return CalculateError.INCORRECT_BOUNDS_NULL_S;
        }
        if (isValidEps(requestFuncUser)) {
            return CalculateError.INCORRECT_EPSILON;
        }
        return null;
    }

    private boolean isValidInterval(RequestFuncUser requestFuncUser) {
        return requestFuncUser.getA() == requestFuncUser.getB();
    }

    private boolean isValidEps(RequestFuncUser requestFuncUser) {
        return requestFuncUser.getEps() <= 0;
    }
}
