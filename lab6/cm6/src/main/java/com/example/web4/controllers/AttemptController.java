package com.example.web4.controllers;

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
//x0: 0,
//y0: 0,
//func: 0,
//xn: 10,
//step: 1,
//eps: 0.1

        logger.info("x0: " + pointRequest.getX0());
        logger.info("y0: " + pointRequest.getY0());
        logger.info("func: " + pointRequest.getFunc());
        logger.info("xn: " + pointRequest.getXn());
        logger.info("step: " + pointRequest.getStep());
        logger.info("eps: " + pointRequest.getEps());
        double x0 =  pointRequest.getX0();
        double y0 =  pointRequest.getY0();
        DifferentialEquation func = Functions.getFunction(pointRequest.getFunc());
        double xn = pointRequest.getXn();
        double step = pointRequest.getStep();
        double eps = pointRequest.getEps();

        List<String> euler = new ImprovedEulerMethod().solve(func, x0, y0, xn, step, eps);
        logger.info("Euler success");
        List<String> runge = new AdaptiveRungeKutta4().solve(func, x0, y0, xn, step, eps);
        logger.info("Runge success");
        List<String> adam = new AdaptiveAdamsMethod().solve(func, x0, y0, xn, step, eps);
        logger.info("Adam success");


        List<Object> response = new ArrayList<>();
        List<String> names = new ArrayList<>();

        names.add(new ImprovedEulerMethod().getNameMethod());
        names.add(new AdaptiveRungeKutta4().getNameMethod());
        names.add(new AdaptiveAdamsMethod().getNameMethod());

        response.add(euler);
        response.add(runge);
        response.add(adam);
        response.add(func.getLatexEquation(x0, y0));
        response.add(names);


        logger.info("Успешно");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

