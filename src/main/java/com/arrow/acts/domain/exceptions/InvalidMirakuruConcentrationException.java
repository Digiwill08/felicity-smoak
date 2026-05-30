package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando la concentración de Mirakuru está fuera del rango válido [0.0, 10.0].
 */
public class InvalidMirakuruConcentrationException extends BusinessException {

    public InvalidMirakuruConcentrationException(double value) {
        super(String.format(
            "Concentración de Mirakuru inválida: %.2f. El rango permitido es [0.0, 10.0].", value
        ));
    }
}
