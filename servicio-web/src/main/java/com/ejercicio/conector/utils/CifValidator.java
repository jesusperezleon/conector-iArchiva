package com.ejercicio.conector.utils;

public class CifValidator {

    public static boolean isValid(String cif) {
        if (cif == null || cif.isEmpty()) return false;

        String cifRegex = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$";

        // Se podría validar también el dígito de control

        return cif.matches(cifRegex);
    }

}