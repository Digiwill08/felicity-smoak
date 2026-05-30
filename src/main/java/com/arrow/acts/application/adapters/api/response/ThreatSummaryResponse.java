package com.arrow.acts.application.adapters.api.response;

import com.arrow.acts.domain.models.enums.ThreatLevel;

/**
 * DTO de salida: resumen táctico de una amenaza activa.
 * Usado en la vista priorizada del sistema ACTS.
 */
public record ThreatSummaryResponse(
        Long subjectId,
        String alias,
        ThreatLevel threatLevel,
        double mirakuruConcentration,
        String lastKnownLocation,
        String behavioralStatus
) {}
