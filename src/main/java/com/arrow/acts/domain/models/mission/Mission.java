package com.arrow.acts.domain.models.mission;

import com.arrow.acts.domain.models.enums.MissionPriority;
import com.arrow.acts.domain.models.enums.MissionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Agregado raíz: Misión táctica del equipo Arrow.
 *
 * <p>Representa una operación planificada o activa orientada a neutralizar
 * una o más amenazas Mirakuru en una zona específica de la ciudad.</p>
 *
 * <p>Invariantes:
 * <ul>
 *   <li>Una misión CRITICAL requiere al menos un agente de campo asignado.</li>
 *   <li>No puede activarse una misión sin objetivos (subjectIds) definidos.</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
public class Mission {

    private Long id;

    /** Descripción del objetivo principal de la misión. */
    private String objective;

    /** IDs de los sujetos Mirakuru que son objetivo de esta misión. */
    private List<Long> targetSubjectIds = new ArrayList<>();

    /** IDs de los agentes del equipo asignados a esta misión. */
    private List<Long> assignedAgentIds = new ArrayList<>();

    /** Estado actual del ciclo de vida de la misión. */
    private MissionStatus status;

    /** Nivel de prioridad táctica de la misión. */
    private MissionPriority priority;

    /** Nombre o descripción de la zona de operación. */
    private String operationalZone;

    /** Hora planificada de inicio de la misión. */
    private Instant scheduledStart;

    /** Hora real de inicio (se asigna al activar la misión). */
    private Instant actualStart;

    /** Hora de finalización (completada o abortada). */
    private Instant endTime;

    /** Notas adicionales o bitácora de la misión. */
    private String notes;
}
