package com.arrow.acts.application.adapters.api.request;

import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.Capability;
import com.arrow.acts.domain.models.enums.TeamRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO de entrada para registrar o actualizar un miembro del equipo Arrow.
 */
@Getter
@Setter
public class TeamMemberRequest {

    @NotBlank(message = "El nombre en clave (codename) no puede estar vacío.")
    private String codename;

    private String realName;

    @NotNull(message = "El rol del agente es obligatorio.")
    private TeamRole role;

    private Set<Capability> capabilities;

    @NotNull(message = "El estado del agente es obligatorio.")
    private AgentStatus currentStatus;
}
