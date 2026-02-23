package com.ejercicio.conector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String mensaje) {
        super(mensaje);
    }
}

