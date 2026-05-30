package com.arrow.acts.application.adapters.api.request;

import com.arrow.acts.domain.models.enums.MissionPriority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * DTO de entrada para crear una nueva misión táctica.
 */
@Getter
@Setter
public class MissionRequest {

    @NotBlank(message = "El objetivo de la misión no puede estar vacío.")
    private String objective;

    @NotEmpty(message = "La misión debe tener al menos un sujeto objetivo.")
    private List<Long> targetSubjectIds;

    @NotNull(message = "La prioridad de la misión es obligatoria.")
    private MissionPriority priority;

    @NotBlank(message = "La zona operacional no puede estar vacía.")
    private String operationalZone;

    @NotNull(message = "La hora de inicio planificada es obligatoria.")
    @Future(message = "La hora de inicio debe ser en el futuro.")
    private Instant scheduledStart;

    private String notes;
}
