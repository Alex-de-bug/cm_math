package com.example.web4.validators;
import com.example.web4.repositories.UserRepository;

import java.util.regex.Pattern;

public class UserValidation {
    public AuthError validateUser(String username, String password, String email) {
        if (!isValidUsername(username)) {
            return AuthError.INVALID_LOGIN;
        }

        if (!isValidPassword(password)) {
            return AuthError.INVALID_PASSWORD;
        }

        if (!isValidEmail(email)) {
            return AuthError.INVALID_EMAIL;
        }
        return null;
    }
    public AuthError validateUser(String username, String password) {
        if (!isValidUsername(username)) {
            return AuthError.INVALID_LOGIN;
        }

        if (!isValidPassword(password)) {
            return AuthError.INVALID_PASSWORD;
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
