package com.utp.yachaytinkiy.domain.validator;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    
    public static boolean isStrong(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }
        
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        
        return hasUpper && hasLower && hasDigit;
    }
    
    public static String getRequirements() {
        return "Mínimo " + MIN_LENGTH + 
               " caracteres, incluir mayúsculas, minúsculas y números";
    }
}
