package com.example.web4.controllers;

import com.example.web4.dto.Coordinates;
import com.example.web4.dto.HitResult;
import com.example.web4.models.User;
import com.example.web4.repositories.UserRepository;
import com.example.web4.security.jwt.AuthTokenFilter;
import com.example.web4.security.jwt.JwtUtils;
import com.example.web4.security.sevices.AuthUsersDetails;
import com.example.web4.service.AttemptService;
import com.example.web4.service.UserService;
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
@RequestMapping("/api/auth/attempt")
public class AttemptController {
    private final AttemptService attemptService;
    private final JwtUtils jwtUtils;
    private final AuthTokenFilter authTokenFilter;
    private final UserRepository userRepository;

    @Autowired
    public AttemptController(AttemptService attemptService, JwtUtils jwtUtils, AuthTokenFilter authTokenFilter, UserRepository userRepository) {
        this.attemptService = attemptService;
        this.jwtUtils = jwtUtils;
        this.authTokenFilter = authTokenFilter;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public ResponseEntity<?> createPoint(@RequestBody Coordinates pointRequest, HttpServletRequest request) {
        try {
            System.out.println(pointRequest.getX()+" "+pointRequest.getY()+" "+pointRequest.getR()+" \ntoken is: "+jwtUtils.validateJwtToken(pointRequest.getToken()));
            CoordinatesValidation.validate(pointRequest);
            HitResult hitResult = attemptService.save(pointRequest);
            return new ResponseEntity<>(hitResult, HttpStatus.CREATED);
        } catch (OutOfCoordinatesBoundsException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getHits(HttpServletRequest request) {
        List<HitResult> hits = new ArrayList<>();
        try {
            hits = attemptService.findAllByOwnerId(getUserIdFromRequest(request));
            return new ResponseEntity<>(hits, HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping()
    public void deleteAll(HttpServletRequest request) {
        System.out.println("delete");
        attemptService.deleteAttempt(getUserIdFromRequest(request));
//        attemptService.deleteAttempt(userService.findById(getUserIdFromRequest(request)));
    }



    private Long getUserIdFromRequest(HttpServletRequest request) {
        AuthUsersDetails userDetails = (AuthUsersDetails) authTokenFilter.getUserDetails(request);
        return userDetails.getId();
    }

}
