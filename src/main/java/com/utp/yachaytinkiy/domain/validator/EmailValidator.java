package com.utp.yachaytinkiy.domain.validator;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_REGEX = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    public static boolean isValid(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }
}
