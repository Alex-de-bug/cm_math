package com.example.web4.controllers;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.approx.*;
import com.example.web4.validators.CalculateError;
import com.example.web4.validators.DataValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/integration")
@Slf4j
public class AttemptController {
    private static final Logger logger = LoggerFactory.getLogger(AttemptController.class);
    DataValidation dataValidation = new DataValidation();

    @PostMapping("/calculate")
    public ResponseEntity<?> createPoint(@RequestBody RequestFuncUser pointRequest, HttpServletRequest request) {
//        log.trace("A TRACE Message");
//        log.debug("A DEBUG Message");
//        log.info("An INFO Message");
//        log.warn("A WARN Message");
//        log.error("An ERROR Message");
        logger.info("количество точек: "+pointRequest.getSliderValue());
        logger.info("сохранение в файл: "+pointRequest.getSaveToFile());
        for(PointDto pointDto : pointRequest.getPoints()){
            logger.info("точка: ("+pointDto.getX()+", "+pointDto.getY()+")");
        }

        DataValidation dataValidation1 = new DataValidation();
        CalculateError calculateError = dataValidation1.validateAtt(pointRequest);

        if(calculateError != null){
            return new ResponseEntity<>(calculateError.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        LinApprox linApprox = new LinApprox(pointRequest);
        linApprox.calculate();

        QuadApprox quadApprox = new QuadApprox(pointRequest);
        quadApprox.calculate();

        CubicApprox cubicApprox = new CubicApprox(pointRequest);
        cubicApprox.calculate();

        ExpApprox expApprox = new ExpApprox(pointRequest);
        expApprox.calculate();

        LogApprox logApprox = new LogApprox(pointRequest);
        logApprox.calculate();

        PowerApprox powerApprox = new PowerApprox(pointRequest);
        powerApprox.calculate();

        double[] sValues = new double[]{
                linApprox.getDeterm(),
                quadApprox.getDeterm(),
                cubicApprox.getDeterm(),
                expApprox.getDeterm(),
                logApprox.getDeterm(),
                powerApprox.getDeterm()
        };

        double minValue = sValues[0];

        for (int i = 1; i < sValues.length; i++) {
            if (sValues[i] > minValue) {
                minValue = sValues[i];
            }
        }


        List<?> dataArray = Arrays.asList(
                linApprox.getA(), linApprox.getB(),
                quadApprox.getA(), quadApprox.getB(), quadApprox.getC(),
                cubicApprox.getA(), cubicApprox.getB(), cubicApprox.getC(), cubicApprox.getD(),
                expApprox.getA(), expApprox.getB(),
                logApprox.getA(), logApprox.getB(),
                powerApprox.getA(), powerApprox.getB(),
                Arrays.asList(linApprox.getDeterm() == minValue,
                        quadApprox.getDeterm() == minValue,
                        cubicApprox.getDeterm() == minValue,
                        expApprox.getDeterm() == minValue,
                        logApprox.getDeterm() == minValue,
                        powerApprox.getDeterm() == minValue
                        )
                );


        List<Object> response = Arrays.asList(dataArray,
                linApprox.getAnswer().contains("NaN") ? "": linApprox.getAnswer(),
                quadApprox.getAnswer().contains("NaN") ? "": quadApprox.getAnswer(),
                cubicApprox.getAnswer().contains("NaN") ? "": cubicApprox.getAnswer(),
                expApprox.getAnswer().contains("NaN") ? "": expApprox.getAnswer(),
                logApprox.getAnswer().contains("NaN") ? "": logApprox.getAnswer(),
                powerApprox.getAnswer().contains("NaN") ? "": powerApprox.getAnswer()
        );

        logger.info("Успешно");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

