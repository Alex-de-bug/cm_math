package com.example.web4.math.methods;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.AnswerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetangleLeftMethod extends MathMethod {
    private static final Logger logger = LoggerFactory.getLogger(RetangleLeftMethod.class);

    public RetangleLeftMethod(RequestFuncUser data) {
        super(data);
    }

    @Override
    public void calculate() {
        logger.info("Метод левых");

        if (a > b) {
            logger.warn("Замена границ");
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
            logger.warn("В выбранном интервале присутсвует разрыв первого рода!\n");
        } else {
            answerInfo = new AnswerInfo(e, sum, I, r, n);
        }
    }
}
