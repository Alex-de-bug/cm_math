package com.example.web4.math.methods;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.AnswerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Double.NaN;

public class TrapMethod extends MathMethod {
    private static final Logger logger = LoggerFactory.getLogger(TrapMethod.class);

    public TrapMethod(RequestFuncUser data) {
        super(data);
    }

    @Override
    public void calculate() {
        logger.info("Метод Трапеций");

        if (a > b) {
            logger.warn("Замена границ");
            double tmp = a;
            a = b;
            b = tmp;
        }

        double aNew = a, step, sum = 0, r = e + 1, I = functions.getI(a, b, (int) number);
        long n = 2;
        double y0 = functions.f(a, (int) number);
        double yn = functions.f(b, (int) number);
        double currAns = NaN;

        while (r > e) {
            n *= 2;
            step = (b - a) / n;
            sum = 0;
            aNew = a + step;
            for (int i = 1; i < n; i++) {
                sum += functions.f(aNew, (int) number);
                aNew += step;
            }
            currAns = step * ((y0 + yn) / 2 + sum);
            r = Math.abs(I - currAns);
        }
        if (Double.isNaN(currAns) || Double.isNaN(I) || Double.isNaN(r) || Double.isNaN(Math.abs(100 * r / ((I + sum) / 2)))) {
            logger.warn("В выбранном интервале присутсвует разрыв первого рода!\n");
        } else {
            answerInfo = new AnswerInfo(e, currAns, I, r, n);
        }
    }
}
