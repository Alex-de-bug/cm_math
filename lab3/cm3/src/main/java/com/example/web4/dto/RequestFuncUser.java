package com.example.web4.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestFuncUser implements Cloneable {
    private Integer sliderValue;
    private List<PointDto> points;
    private Boolean saveToFile;
}
