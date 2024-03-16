package com.example.web4.controllers;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.methods.*;
import com.example.web4.validators.CalculateError;
import com.example.web4.validators.DataValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        logger.info("Функция: " + pointRequest.getTypeFunc() + "; " +
                "Метод: " + pointRequest.getMethod() + "; " +
                "Граница левая: " + pointRequest.getA() + "; " +
                "Граница правая: " + pointRequest.getB() + "; " +
                "Точность: " + pointRequest.getEps() + "; "
        );

        CalculateError test = dataValidation.validateAtt(pointRequest);
        if (test != null) {
            logger.error(test.getErrorMessage());
            return new ResponseEntity<>(test.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
        MathMethod mathMethod;
        String tmp = "";


        switch ((int) pointRequest.getMethod()) {
            case (1) -> mathMethod = new RetangleLeftMethod(pointRequest);
            case (2) -> mathMethod = new RetangleRightMethod(pointRequest);
            case (3) -> mathMethod = new RetangleCentralMethod(pointRequest);
            case (4) -> mathMethod = new TrapMethod(pointRequest);
            case (5) -> mathMethod = new SimpsonMethod(pointRequest);
            default -> mathMethod = new RetangleCentralMethod(pointRequest);
        }
        mathMethod.calculate();
        tmp = mathMethod.getAnswer();
        logger.info("Успешно");


        return new ResponseEntity<>(tmp, HttpStatus.OK);


//        return new ResponseEntity<>("Ошибочно введены данные", HttpStatus.BAD_REQUEST);
//        String result = pointRequest.calculate();
//        String replacedString = result.replace("E", "*10^");
//        return new ResponseEntity<>(replacedString, HttpStatus.CREATED);

    }
}
