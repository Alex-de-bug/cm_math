package com.example.web4.controllers;

import com.example.web4.dto.Coordinates;
import com.example.web4.dto.HitResult;
import com.example.web4.utils.CoordinatesValidation;
import com.example.web4.utils.OutOfCoordinatesBoundsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                "Точность: "+pointRequest.getEps()+"\n"
                );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getHits(HttpServletRequest request) {
        List<HitResult> hits = new ArrayList<>();
        try {
            return new ResponseEntity<>(hits, HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping()
    public void deleteAll(HttpServletRequest request) {
        System.out.println("delete");
//        attemptService.deleteAttempt(userService.findById(getUserIdFromRequest(request)));
    }



}
