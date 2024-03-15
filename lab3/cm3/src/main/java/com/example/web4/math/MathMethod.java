package com.example.web4.math;

import com.example.web4.dto.RequestFuncUser;

public abstract class MathMethod {
    protected final Functions functions = new Functions();
    public AnswerInfo answerInfo;
    public abstract void calculate(RequestFuncUser data);
    public String getAnswer(){
        return answerInfo.outAnswer();
    };
}
