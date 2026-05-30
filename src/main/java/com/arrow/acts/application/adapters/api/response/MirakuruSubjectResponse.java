package com.arrow.acts.application.adapters.api.response;

import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;

import java.time.Instant;

/**
 * DTO de salida: información de un sujeto Mirakuru.
 * Implementado como Java record para garantizar inmutabilidad.
 */
public record MirakuruSubjectResponse(
        Long id,
        String alias,
        double mirakuruConcentration,
        String lastKnownLocation,
        BehavioralStatus behavioralStatus,
        ThreatLevel threatLevel,
        String lastSightingSource,
        Instant registeredAt,
        Instant updatedAt
) {}
