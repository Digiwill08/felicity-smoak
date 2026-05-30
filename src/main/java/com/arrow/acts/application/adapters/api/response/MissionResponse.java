package com.arrow.acts.application.adapters.api.response;

import com.arrow.acts.domain.models.enums.MissionPriority;
import com.arrow.acts.domain.models.enums.MissionStatus;

import java.time.Instant;
import java.util.List;

/**
 * DTO de salida: información de una misión táctica.
 * Implementado como Java record para garantizar inmutabilidad.
 */
public record MissionResponse(
        Long id,
        String objective,
        List<Long> targetSubjectIds,
        List<Long> assignedAgentIds,
        MissionStatus status,
        MissionPriority priority,
        String operationalZone,
        Instant scheduledStart,
        Instant actualStart,
        Instant endTime,
        String notes
) {}
