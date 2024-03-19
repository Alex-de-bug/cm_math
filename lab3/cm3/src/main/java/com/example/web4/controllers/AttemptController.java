package com.example.web4.controllers;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.AnswerInfo;
import com.example.web4.math.Functions;
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

import java.util.ArrayList;

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
        Functions test2 = new Functions();
        ArrayList<Double> critInterval = test2.getErrInterval((int) pointRequest.getTypeFunc());

        if(critInterval.isEmpty()){
            mathMethod = getMethod((int) pointRequest.getMethod(), pointRequest);
            mathMethod.calculate();
            tmp = mathMethod.getAnswer().outAnswer();
        }else {
            double a = pointRequest.getA();
            double b = pointRequest.getB();
            double crit1 = critInterval.get(0);
            double crit2 = critInterval.get(1);
            if (a < crit1 && b > crit2) {
                // Случай 1: [a, b] охватывает [crit1, crit2]
                RequestFuncUser interval1 = pointRequest.clone();
                interval1.setB(crit1);
                logger.info(interval1.toString());

                RequestFuncUser interval2 = pointRequest.clone();
                interval2.setA(crit2);
                logger.info(interval2.toString());

                MathMethod mathMethod1 = getMethod((int) interval1.getMethod(), interval1);
                MathMethod mathMethod2 = getMethod((int) interval2.getMethod(), interval2);


                mathMethod1.calculate();
                mathMethod2.calculate();
                logger.info(mathMethod1.answerInfo.toString());
                logger.info(mathMethod2.answerInfo.toString());

                double e = mathMethod1.answerInfo.e;
                double ans = mathMethod1.answerInfo.answer + mathMethod2.answerInfo.answer;
                double r = (mathMethod1.answerInfo.r + mathMethod2.answerInfo.r)/2;
                long n = mathMethod1.answerInfo.n + mathMethod2.answerInfo.n;
                AnswerInfo answerInfo = new AnswerInfo(e, ans, ans, r, n);
                if(Math.abs(interval1.getA()) == Math.abs(interval2.getB())){
                    answerInfo = new AnswerInfo(0, 0, 0, 0, 0);
                }
                tmp = answerInfo.outAnswer();
            } else if (a >= crit1 && b <= crit2) {
                // Случай 2: [a, b] полностью внутри [crit1, crit2]
                AnswerInfo answerInfo = new AnswerInfo(0, 0, 0, 0, 0);
                tmp = answerInfo.outAnswer();
            } else if (a >= crit1 && a <= crit2 && b > crit2) {
                // Случай 3: левая граница внутри, правая снаружи
                RequestFuncUser interval1 = pointRequest.clone();
                interval1.setA(crit2);
                MathMethod mathMethod1 = getMethod((int) interval1.getMethod(), interval1);
                mathMethod1.calculate();
                logger.info(mathMethod1.answerInfo.toString());
                tmp = mathMethod1.answerInfo.outAnswer();
            } else if (a < crit1 && b <= crit2 && b >= crit1) {
                // Случай 4: правая граница внутри, левая снаружи
                RequestFuncUser interval1 = pointRequest.clone();
                interval1.setB(crit1);
                MathMethod mathMethod1 = getMethod((int) interval1.getMethod(), interval1);
                mathMethod1.calculate();
                logger.info(mathMethod1.answerInfo.toString());
                tmp = mathMethod1.answerInfo.outAnswer();
            } else if ((b < crit1)||(a > crit2)) {
                // Случай 5: [a, b] полностью слева от [crit1, crit2]
                // Случай 6: [a, b] полностью справа от [crit1, crit2]
                mathMethod = getMethod((int) pointRequest.getMethod(), pointRequest);
                mathMethod.calculate();
                tmp = mathMethod.getAnswer().outAnswer();
            } else {
                AnswerInfo answerInfo = new AnswerInfo(0, 0, 0, 0, 0);
                tmp = answerInfo.outAnswer();
            }
        }
        logger.info("Успешно");
        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }
    private MathMethod getMethod(int x, RequestFuncUser pointRequest){
        switch (x) {
            case (1) -> {
                return new RetangleLeftMethod(pointRequest);
            }
            case (2) -> {
                return new RetangleRightMethod(pointRequest);
            }
            case (3) -> {
                return new RetangleCentralMethod(pointRequest);
            }
            case (4) -> {
                return new TrapMethod(pointRequest);
            }
            case (5) ->  {
                return new SimpsonMethod(pointRequest);
            }
            default -> {
                return new RetangleCentralMethod(pointRequest);
            }
        }
    }
}
