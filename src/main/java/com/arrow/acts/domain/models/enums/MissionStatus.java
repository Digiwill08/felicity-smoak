package com.arrow.acts.domain.models.enums;

/**
 * Estado operativo de una misión táctica del equipo Arrow.
 */
public enum MissionStatus {

    /** La misión fue registrada pero aún no ha comenzado. */
    PLANNED,

    /** La misión está en ejecución en campo. */
    ACTIVE,

    /** La misión fue completada exitosamente. */
    COMPLETED,

    /** La misión fue cancelada antes o durante su ejecución. */
    ABORTED
}
