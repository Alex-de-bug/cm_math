package com.example.web4.validators;

public enum CalculateError {
    //    INCORRECT_USERNAME_OR_PASSWORD("Некорректное имя или пароль"),
//    INVALID_PASSWORD("Пароль не менее 1 символа и включает только буквы и цифры"),
//    INVALID_LOGIN("Логин не менее 1 символа и включает только буквы и цифры"),
//    INVALID_EMAIL("Почта введена неверно"),
//    USER_ALREADY_EXIST("Пользователь уже существует, придумайте другое имя или измените почту"),
//    SESSION_EXPIRED("Действие токена истекло");
    INCORRECT_BOUNDS_NULL_S("Некорректные границы, площадь - 0"),
    INCORRECT_EPSILON("Некорректная погрешность");

    private final String errorMessage;

    CalculateError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
