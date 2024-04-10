package com.example.web4.validators;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Double.NaN;

public class DataValidation {
    private static final Logger logger = LoggerFactory.getLogger(DataValidation.class);
    public CalculateError validateAtt(RequestFuncUser requestFuncUser) {
        List<PointDto> points = requestFuncUser.getPoints();
        Set<String> uniquePoints = new HashSet<>();

        for (PointDto point : points) {
            String pointAsString = point.getX() + "," + point.getY();

            if (!uniquePoints.add(pointAsString)) {
                logger.error("Ошибка, среди точек есть повторы!");
                return CalculateError.INCORRECT_POINTS;
            }
        }


        return null;
    }


}
