package com.example.web4.utils;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Lombok;
import lombok.Setter;
import lombok.ToString;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
@ToString
public class IterationsForBisection {
    int iteration;
    double a;
    double b;
    double x;
    double fA;
    double fB;
    double fX;
    double absAB;
}
