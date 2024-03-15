package com.example.web4.math;

import com.example.web4.dto.RequestFuncUser;

public class RetangleLeftMethod extends MathMethod{
    @Override
    public void calculate(RequestFuncUser data) {
        double a = data.getA();
        double b = data.getB();
        double e = data.getEps();
        double number = data.getTypeFunc();

        if (a > b) {
            double tmp = a;
            a = b;
            b = tmp;
        }
        double aNew = a, step, sum = 0, r = e + 1, I = functions.getI(a, b, (int) number);
        long n = 2;

        while (r > e) {
            n *= 2;
            step = (b - aNew) / n;
            sum = 0;
            for (int i = 0; i < n; i++) {
                sum += functions.f(aNew, (int) number);
                aNew += step;
            }
            sum = sum * step;
            r = Math.abs(I - sum);
            aNew = a;
        }

        if (Double.isNaN(sum) || Double.isNaN(I) || Double.isNaN(r) || Double.isNaN(Math.abs(100 * r / ((I + sum) / 2)))) {
            System.out.println("В выбранном интервале присутсвует разрыв первого рода!\n");
        } else {
            answerInfo = new AnswerInfo();
            answerInfo.setE(e);
            answerInfo.setAnswer(sum);
            answerInfo.setR(r);
            answerInfo.setI(I);
            answerInfo.setN(n);
        }
    }
}
