package com.example.web4.math.methods;

import com.example.web4.dto.RequestFuncUser;
import com.example.web4.math.AnswerInfo;
import com.example.web4.math.Functions;

public abstract class MathMethod {
    protected final Functions functions = new Functions();
    public AnswerInfo answerInfo;
    protected double a;
    protected double b;
    protected double e;
    protected double number;

    public MathMethod(RequestFuncUser data) {
        a = data.getA();
        b = data.getB();
        e = data.getEps();
        number = data.getTypeFunc();
    }

    public abstract void calculate();

    public AnswerInfo getAnswer() {
        return answerInfo;
    }

    ;
}
