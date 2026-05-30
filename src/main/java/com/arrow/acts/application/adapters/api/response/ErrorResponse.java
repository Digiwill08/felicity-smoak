package com.arrow.acts.application.adapters.api.response;

/**
 * DTO de salida para respuestas de error del sistema ACTS.
 */
public record ErrorResponse(
        int status,
        String error,
        String message
) {}
