package com.example.web4.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HitResult {
    private double x;
    private double y;
    private double r;
    private boolean result;

    public HitResult(Coordinates coordinates, boolean result) {
        this(coordinates.getX(), coordinates.getY(), coordinates.getR(), result);
    }
}
