package com.example.bill_management_app;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Validator {
    // one or two words, where each word can contain only letters (both uppercase and lowercase) and hyphens
    private static final String NameRegex = "^[A-Za-z]+(?:-[A-Za-z]+)?(?: [A-Za-z]+(?:-[A-Za-z]+)?)*$";
    // words or hyphenated words separated by spaces
    //private static final String lastNameRegex = "^[A-Za-z]+(?:-[A-Za-z]+)?(?: [A-Za-z]+(?:-[A-Za-z]+)?)*$";

    /*
    * Are between 8 and 12 characters in length.
    * Contain at least one digit.
    * Contain at least one special character from the provided set.
    * Contain at least one alphabet character.
    * */
    private static final String passwordRegex ="^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*[a-zA-Z]).{8,12}$";

    private static final String amountRegex = "^\\d+$";

    public static boolean isValidName(String name) {
        return Pattern.compile(NameRegex).matcher(name).matches();
    }
    //public static boolean isValidLastName(String lastName) {
    //    return Pattern.compile(lastNameRegex).matcher(lastName).matches();
    //}

    public static boolean isValidPassword(String password) {
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    public static boolean isValidPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidAmount(String amount) {
        return Pattern.compile(amountRegex).matcher(amount).matches();
    }

}
