package com.example.web4.math.methods;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.AnswerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetangleCentralMethod extends MathMethod {
    private static final Logger logger = LoggerFactory.getLogger(RetangleCentralMethod.class);

    public RetangleCentralMethod(RequestFuncUser data) {
        super(data);
    }

    @Override
    public void calculate() {
        logger.info("Метод средних");
        if (a > b) {
            logger.warn("Замена границ");
            double tmp = a;
            a = b;
            b = tmp;
        }

        double step, sum, r = e + 1;
        long n = 4;
        double currAns = 0, prevAns = 0;

        while (r > e){
            step = (b - a) / n;
            sum = 0;
            for (int i = 0; i < n; i++) {
                double aNew = a + step / 2 + step * i;
                sum += functions.f(aNew, (int) number);
            }
            prevAns = currAns;
            currAns = sum * step;
            if (n > 4) {
                r = Math.abs(currAns - prevAns) / 3.0;
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
