package com.example.web4.validators;

public enum CalculateError {
    INCORRECT_BOUNDS_NULL_S("Некорректные границы, площадь - 0"),
    INCORRECT_BOUNDS_INT_ERR("На границ(е/ах) есть точк(а/и), в которых первообразная функции не существует, расхождение"),
    INCORRECT_BOUNDS_INT_ERR_IN_INTER("В итнервале есть точка разрыва, в которой не сущетсвует первообразная, расхождение"),
    INCORRECT_EPSILON("Некорректная погрешность");

    private final String errorMessage;

    CalculateError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
