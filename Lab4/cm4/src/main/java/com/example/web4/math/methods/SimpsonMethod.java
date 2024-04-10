package com.example.web4.math.methods;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.AnswerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpsonMethod extends MathMethod {
    private static final Logger logger = LoggerFactory.getLogger(SimpsonMethod.class);

    public SimpsonMethod(RequestFuncUser data) {
        super(data);
    }

    @Override
    public void calculate() {
        logger.info("Метод Симпсона");

        if (a > b) {
            logger.warn("Замена границ");
            double tmp = a;
            a = b;
            b = tmp;
        }

        double aNew = a, step, sum = 0, r = e + 1;
        long n = 4;
        double y0 = functions.f(a, (int) number);
        double yn = functions.f(b, (int) number);
        double currAns = 0, prevAns = 0;

        while (r >= e) {
            step = (b - a) / n;
            sum = 0;
            aNew = a + step;
            for (int i = 1; i < n; i++) {
                if (i % 2 == 0) {
                    sum += 2 * functions.f(aNew, (int) number);
                } else {
                    sum += 4 * functions.f(aNew, (int) number);
                }
                aNew += step;
            }
            prevAns = currAns;
            currAns = step / 3 * (y0 + sum + yn);
            if (n > 4) {
                r = Math.abs(currAns - prevAns) / 15.0;
            }
            n *= 2;
            if(n > 1000000000){
                logger.error("Первышено количесвто итераций");
                break;
            }
        }

        answerInfo = new AnswerInfo(e, currAns, prevAns, r, n / 2);
    }
}
