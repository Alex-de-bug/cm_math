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
    private Double x0;
    private Double y0;
    private Integer func;
    private Double xn;
    private Double step;
    private Double eps;
}
