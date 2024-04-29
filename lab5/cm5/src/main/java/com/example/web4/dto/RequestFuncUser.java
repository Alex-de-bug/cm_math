package com.example.web4.dto;

import lombok.*;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestFuncUser {
    private Double val;
    private Integer function;
    private Double step;
    private List<PointDto> points;
    private Double a;
    private Double b;
    private Integer type;

    public void sortPointsByX() {
        points.sort(Comparator.comparing(PointDto::getX));
    }

}
