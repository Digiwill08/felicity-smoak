package com.arrow.acts.domain.exceptions;

/**
 * Excepción base del dominio ACTS.
 * Todas las excepciones de negocio deben extender esta clase.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
