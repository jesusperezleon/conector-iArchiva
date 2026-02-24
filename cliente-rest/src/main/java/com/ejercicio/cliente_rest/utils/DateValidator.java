package com.ejercicio.cliente_rest.utils;

public class DateValidator {

    public static boolean isValid(String date) {
        String dateRegex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        return date.matches(dateRegex);
    }

}