package com.example.web4.validators;

public enum CalculateError {
    INCORRECT_POINTS("Есть повторяющаяся точка, перепроверьте ввод");

    private final String errorMessage;

    CalculateError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
