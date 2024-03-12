package com.example.web4.utils;

import com.example.web4.dto.Coordinates;

public class CoordinatesValidator {
    public static Boolean validateAndCreate(double func, double metodStr, double aStr, double bStr, double epsStr, boolean fileStr) {
        try {
            if (func < 0 || func > 5) {
                System.out.println("funk");
                return false;
            }

            if (metodStr < 0 || metodStr >= 5) {
                System.out.println("metodStr");
                return false;
            }


            if (epsStr < 0.000001) {
                System.out.println("epsStr");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
