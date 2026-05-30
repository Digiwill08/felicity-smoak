package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando se intenta registrar un sujeto Mirakuru
 * que ya existe en el sistema (duplicado por alias).
 */
public class DuplicateSubjectException extends BusinessException {

    public DuplicateSubjectException(String alias) {
        super(String.format(
            "Ya existe un sujeto registrado con el alias '%s'. No se permiten duplicados.", alias
        ));
    }
}
