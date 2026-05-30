package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando se busca un sujeto Mirakuru que no existe en el sistema.
 */
public class SubjectNotFoundException extends NotFoundException {

    public SubjectNotFoundException(Long subjectId) {
        super(String.format("Sujeto Mirakuru con ID [%d] no encontrado en el sistema.", subjectId));
    }

    public SubjectNotFoundException(String alias) {
        super(String.format("Sujeto Mirakuru con alias '%s' no encontrado en el sistema.", alias));
    }
}
