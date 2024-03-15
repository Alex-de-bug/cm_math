package com.example.web4.controllers;

import com.example.web4.dto.RequestFuncUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/integration")
public class AttemptController {

    @PostMapping("/calculate")
    public ResponseEntity<?> createPoint(@RequestBody RequestFuncUser pointRequest, HttpServletRequest request) {

        System.out.println("Функция: "+pointRequest.getTypeFunc()+"\n"+
                "Метод: "+pointRequest.getMethod()+"\n"+
                "Граница левая: "+pointRequest.getA()+"\n"+
                "Граница правая: "+pointRequest.getB()+"\n"+
                "Точность: "+pointRequest.getEps()+"\n"
                );

        return new ResponseEntity<>("", HttpStatus.OK);
//        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

//        return new ResponseEntity<>("Ошибочно введены данные", HttpStatus.BAD_REQUEST);
//        String result = pointRequest.calculate();
//        String replacedString = result.replace("E", "*10^");
//        return new ResponseEntity<>(replacedString, HttpStatus.CREATED);

    }
//    @GetMapping()
//    public ResponseEntity<?> getHits(HttpServletRequest request) {
//            return new ResponseEntity<>(HttpStatus.OK);
//
//    }
//
//    @DeleteMapping()
//    public void deleteAll(HttpServletRequest request) {
//        System.out.println("delete");
////        attemptService.deleteAttempt(userService.findById(getUserIdFromRequest(request)));
//    }



}
