package com.arrow.acts.application.adapters.api.response;

import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.Capability;
import com.arrow.acts.domain.models.enums.TeamRole;

import java.util.Set;

/**
 * DTO de salida: información de un miembro del equipo Arrow.
 * Implementado como Java record para garantizar inmutabilidad.
 */
public record TeamMemberResponse(
        Long id,
        String codename,
        String realName,
        TeamRole role,
        Set<Capability> capabilities,
        AgentStatus currentStatus
) {}
