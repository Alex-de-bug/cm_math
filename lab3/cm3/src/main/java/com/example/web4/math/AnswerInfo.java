package com.example.web4.math;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnswerInfo {
    public double e;
    public double answer;
    public double I;
    public double r;
    public long n;
    public String outAnswer(){
        String ans = "Достигнута\\ точность\\ = \\ " + e+
                ";\\\\ \\ Вычисленный\\ ответ \\ = \\ "+answer+
                ";\\\\ \\ Вычисление\\ прямое\\ =\\ "+ I+
                ";\\\\ \\ Абсолютная\\ погрешность\\ =\\ "+r+
                ";\\\\ \\ Относительная\\ погрешность\\ =\\ "+Math.abs(100*r/((I+answer)/2))+"\\%"+
                ";\\\\ \\ Число\\ разбиений\\ интервала\\ = \\ "+n;
        return ans.replace("E", "*10 ^");
    }
}