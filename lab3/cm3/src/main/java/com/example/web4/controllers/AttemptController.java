package com.example.web4.controllers;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.MathMethod;
import com.example.web4.math.RetangleLeftMethod;
import com.example.web4.validators.CalculateError;
import com.example.web4.validators.DataValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/integration")
public class AttemptController {
    DataValidation dataValidation = new DataValidation();

    @PostMapping("/calculate")
    public ResponseEntity<?> createPoint(@RequestBody RequestFuncUser pointRequest, HttpServletRequest request) {

        System.out.println("Функция: "+pointRequest.getTypeFunc()+"\n"+
                "Метод: "+pointRequest.getMethod()+"\n"+
                "Граница левая: "+pointRequest.getA()+"\n"+
                "Граница правая: "+pointRequest.getB()+"\n"+
                "Точность: "+pointRequest.getEps()+"\n"
                );

        CalculateError test = dataValidation.validateAtt(pointRequest);
        if(test != null){
            return new ResponseEntity<>(test.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
        MathMethod mathMethod;
        String tmp ="";


        switch ((int) pointRequest.getMethod()) {
            case (1) -> {
                mathMethod = new RetangleLeftMethod();
                mathMethod.calculate(pointRequest);
                tmp = mathMethod.getAnswer();
            }
            case (2) -> {
                mathMethod = new RetangleLeftMethod();
                mathMethod.calculate(pointRequest);
                tmp = mathMethod.getAnswer();
            }
        }


        return new ResponseEntity<>(tmp, HttpStatus.OK);


//        return new ResponseEntity<>("Ошибочно введены данные", HttpStatus.BAD_REQUEST);
//        String result = pointRequest.calculate();
//        String replacedString = result.replace("E", "*10^");
//        return new ResponseEntity<>(replacedString, HttpStatus.CREATED);

    }
}
