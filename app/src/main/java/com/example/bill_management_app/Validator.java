package com.example.bill_management_app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final String firstNameRegex =
            "^[A-Za-z]+(?:-[A-Za-z]+)?(?: [A-Za-z]+(?:-[A-Za-z]+)?)?$";
    private static final String lastNameRegex =
            "^[A-Za-z]+(?:-[A-Za-z]+)?(?: [A-Za-z]+(?:-[A-Za-z]+)?)*$";
    private static final String phoneRegex =
            "^[0-9]{10}$";
    private static final String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String passwordRegex =
            "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*[a-zA-Z]).{8,12}$";

    private static final Pattern firstNamePattern = Pattern.compile(firstNameRegex);
    private static final Pattern lastNamePattern = Pattern.compile(lastNameRegex);
    private static final Pattern phonePattern = Pattern.compile(phoneRegex);
    private static final Pattern emailPattern = Pattern.compile(emailRegex);
    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);

    public boolean isValidFirstName(String firstName) {
        Matcher matcher = firstNamePattern.matcher(firstName);
        return matcher.matches();
    }
    public boolean isValidLastName(String lastName) {
        Matcher matcher = lastNamePattern.matcher(lastName);
        return matcher.matches();
    }
    public boolean isValidPhone(String phone) {
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }
    public boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

}
