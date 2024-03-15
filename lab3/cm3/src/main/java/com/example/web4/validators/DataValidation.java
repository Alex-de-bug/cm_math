package com.example.web4.validators;

public class DataValidation {
    public CalculateError validateUser(String username, String password, String email) {
        if (!isValidUsername(username)) {
            return CalculateError.INVALID_LOGIN;
        }

        if (!isValidPassword(password)) {
            return CalculateError.INVALID_PASSWORD;
        }

        if (!isValidEmail(email)) {
            return CalculateError.INVALID_EMAIL;
        }
        return null;
    }
    public CalculateError validateUser(String username, String password) {
        if (!isValidUsername(username)) {
            return CalculateError.INVALID_LOGIN;
        }

        if (!isValidPassword(password)) {
            return CalculateError.INVALID_PASSWORD;
        }
        return null;
    }

    private boolean isValidUsername(String username) {
        return username != null && username.matches("[a-zA-Z0-9]+") ;
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 1 && password.matches("[a-zA-Z0-9]+");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }
}
