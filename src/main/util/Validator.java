package src.main.util;


import src.main.dao.ProductsDAO;

import src.main.dao.FornitoreDAO;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Check if a string is not null and not empty
     */
    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Check if a string represents a positive integer
     */
    public static boolean isPositiveInteger(String s) {
        try {
            return Integer.parseInt(s) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string represents a positive decimal number
     */
    public static boolean isPositiveDecimal(String s) {
        try {
            return Double.parseDouble(s) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if email format is valid
     */
    public static boolean isValidEmail(String email) {
        if(email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Check if product code is unique (not already in CSV)
     */
    public static boolean isUniqueProductCode(String code) {
        return !ProductsDAO.existsByCode(code);
    }

    /**
     * Check if supplier VAT is unique
     */
    public static boolean isUniqueVAT(String vat) {
        return !FornitoreDAO.existsByVAT(vat);
    }
}
