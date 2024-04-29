package com.example.web4.controllers;

import com.example.web4.dto.PointDto;
import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.*;
import com.example.web4.validators.DataValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        logger.info("Тип: " + pointRequest.getType());
        logger.info("Аргумент: " + pointRequest.getVal());


        Integer size = 0;
        ArrayList<Double> xVal = new ArrayList<>();
        ArrayList<Double> yVal = new ArrayList<>();

        switch (pointRequest.getType()) {
            case 0: {
                size = pointRequest.getPoints().size();
                pointRequest.sortPointsByX();
                for (PointDto pointDto : pointRequest.getPoints()) {
                    xVal.add(pointDto.getX());
                    yVal.add(pointDto.getY());
                    logger.info("Точка: (" + pointDto.getX() + ", " + pointDto.getY() + ")");
                }
                break;
            }
            case 1: {
                logger.info("Функция: " + pointRequest.getFunction() + "; " +
                        "A: " + pointRequest.getA() + "; " +
                        "B: " + pointRequest.getB() + "; " +
                        "Step: " + pointRequest.getStep());
                Tracing tracing = new Tracing(pointRequest);
                tracing.calculate();
                size = tracing.getSize();
                xVal = tracing.getX();
                yVal = tracing.getY();
                break;
            }
        }

        EndDifference endDifference = new EndDifference(size, pointRequest.getVal(), xVal, yVal);
        endDifference.calculate();
        List<Object> diffTable = new ArrayList<>();
        diffTable.add(endDifference.getAnswer());
        diffTable.add(xVal);
        diffTable.add(yVal);

        Lagrange lagrange = new Lagrange(size, pointRequest.getVal(), xVal, yVal);
        lagrange.calculate();

        Newton newton = new Newton(size, pointRequest.getVal(), xVal, yVal);
        newton.calculate();
        List<String> newt = new ArrayList<>();
        newt.add(newton.getAnswer());
        newt.add(newton.buildPolynomialString());

        Gauss gauss = new Gauss(size, pointRequest.getVal(), xVal, yVal);
        gauss.calculate();
        List<String> gaus = new ArrayList<>();
        gaus.add(gauss.getAnswer());
        gaus.add(gauss.getPolynomial());

        Stirling stir = new Stirling(size, pointRequest.getVal(), xVal, yVal, endDifference.getDefy());
        stir.calculate();

        Bessel bess = new Bessel(size, pointRequest.getVal(), xVal, yVal);
        bess.setDefy(endDifference.getDefy());
        bess.calculate();

        String lag = lagrange.getAnswer();

        String stirl = stir.getAnswer();
        String besse = bess.getAnswer();

        List<Object> response = new ArrayList<>();
        response.add(diffTable);
        response.add(lag);
        response.add(newt);
        response.add(gaus);
        response.add(stirl);
        response.add(besse);
//        logger.info("Успешно");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

