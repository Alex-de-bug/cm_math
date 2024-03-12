package com.example.web4.controllers;

import com.example.web4.dto.Coordinates;
import com.example.web4.utils.CoordinatesValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/attempt")
public class AttemptController {




    @PostMapping("")
    public ResponseEntity<?> createPoint(@RequestBody Coordinates pointRequest, HttpServletRequest request) {

        System.out.println("Функция: "+pointRequest.getFunc()+"\n"+
                "Метод: "+pointRequest.getMetod()+"\n"+
                "Граница левая: "+pointRequest.getA()+"\n"+
                "Граница правая: "+pointRequest.getB()+"\n"+
                "Точность: "+pointRequest.getEps()+"\n"+
                "Файл: "+pointRequest.getFile()+"\n"
                );
        double func  = pointRequest.getFunc();
        double metod = pointRequest.getMetod();
        double a = pointRequest.getA();
        double b = pointRequest.getB();
        double epsil = pointRequest.getEps();
        boolean file = pointRequest.getFile();

        if(!CoordinatesValidator.validateAndCreate(func, metod, a, b, epsil, file)){

            return new ResponseEntity<>("Ошибочно введены данные", HttpStatus.BAD_REQUEST);
        }
        if(!pointRequest.checkRootsCount()&&func!=3&&func!=4){
            return new ResponseEntity<>("На интервале несколько корней или\n" +
                    "они отсутствуют", HttpStatus.BAD_REQUEST);
        }

        String result = pointRequest.calculate();
        String replacedString = result.replace("E", "*10^");
        try {
            String data = new String(Files.readAllBytes(Path.of("tmp.json")));
            return new ResponseEntity<>(replacedString, HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping()
    public ResponseEntity<?> getHits(HttpServletRequest request) {
            return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping()
    public void deleteAll(HttpServletRequest request) {
        System.out.println("delete");
//        attemptService.deleteAttempt(userService.findById(getUserIdFromRequest(request)));
    }



}
