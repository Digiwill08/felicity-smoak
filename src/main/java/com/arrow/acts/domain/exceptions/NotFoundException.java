package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando un recurso del dominio no es encontrado.
 * Corresponde a un HTTP 404 en la capa de presentación.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
