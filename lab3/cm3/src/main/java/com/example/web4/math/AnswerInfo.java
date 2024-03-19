package com.example.web4.math;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@ToString
public class AnswerInfo {
    private static final Logger logger = LoggerFactory.getLogger(AnswerInfo.class);
    public Double e;
    public Double answer;
    public Double I;
    public Double r;
    public Long n;

    public AnswerInfo(double e, double answer, double i, double r, long n) {
        this.e = e;
        this.answer = answer;
        this.I = i;
        this.r = r;
        this.n = n;
    }

    private String formatScientificNotation(String value) {
        if (value.contains("E")) {
            return value.replace("E", "\\cdot10 ^{") + "}";
        } else {
            return value;
        }
    }

    public String outAnswer() {
        logger.info("т: " + e + "; ans: " + answer + "; abs: " + r + "; count: " + n);
        String eStr = formatScientificNotation(e.toString());
        String answerStr = formatScientificNotation(answer.toString());
        String IStr = formatScientificNotation(Double.toString(I));
//        String rStr = formatScientificNotation(Double.toString(Math.abs(I-answer)/3.0));
        String rStr = formatScientificNotation(Double.toString(r));
        String relativeErrorStr = formatScientificNotation(Double.toString(Math.abs(100 * r / ((I + answer) / 2))));
        String nStr = formatScientificNotation(Long.toString(n));
        String ans = "Достигнута\\ точность\\ = \\ " + eStr +
                ";\\\\ \\ Вычисление\\ n\\ итерации\\ = \\ " + answerStr +
                ";\\\\ \\ Вычисление\\ \\frac{n}{2}\\ итерации\\ =\\ " + IStr +
                ";\\\\ \\ Погрешность\\ по\\ Рунге\\ =\\ " + rStr +
                ";\\\\ \\ Относительная\\ погрешность\\ между\\ n\\ и\\ \\frac{n}{2}\\ итер\\ =\\ " + relativeErrorStr + "\\%" +
                ";\\\\ \\ Число\\ разбиений\\ интервала\\ = \\ " + nStr + ";";

        return ans;
    }
}