package com.example.web4.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {
    private double x;
    private double y;
    private double r;
    private String token;
}
