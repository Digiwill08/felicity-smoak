package com.arrow.acts.application.adapters.api.request;

import com.arrow.acts.domain.models.enums.AgentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para actualizar únicamente el estado operativo de un agente.
 */
@Getter
@Setter
public class AgentStatusRequest {

    @NotNull(message = "El nuevo estado del agente es obligatorio.")
    private AgentStatus newStatus;
}
