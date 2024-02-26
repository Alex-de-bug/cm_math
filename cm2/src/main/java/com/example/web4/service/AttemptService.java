package com.example.web4.service;

import com.example.web4.dto.Coordinates;
import com.example.web4.dto.HitResult;
import com.example.web4.models.Attempt;
import com.example.web4.models.User;
import com.example.web4.repositories.AttemptRepository;
import com.example.web4.repositories.UserRepository;
import com.example.web4.security.jwt.JwtUtils;
import com.example.web4.utils.AreaCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptService {
    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    public List<Attempt> listAttempt(Long id) {
        return attemptRepository.findAll();
    }

    @Transactional
    public HitResult save(Coordinates coordinates) {
        boolean isHit = AreaCheck.isHit(coordinates);
        HitResult hitResult = new HitResult(coordinates, isHit);

        Attempt attempt = new Attempt(coordinates.getX(),coordinates.getY(),coordinates.getR(), isHit ? 1 : 0, userRepository.getUserByName(jwtUtils.getUserNameFromJwtToken(coordinates.getToken())));
        attemptRepository.save(attempt);
        return hitResult;
    }

    @Transactional
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    @Transactional
    public List<HitResult> findAllByOwnerId(Long id) {
        return attemptRepository.findByUserId(id).stream()
                .map(Attempt::toHitResult).collect(Collectors.toList());
    }



    @Transactional
    public void deleteAttempt(Long id) {
        System.out.println("Delete "+id);
        attemptRepository.deleteByUserId(id);
    }

    public Attempt getAttemptById(Long id) {
        return attemptRepository.findById(id).orElse(null);
    }
}
