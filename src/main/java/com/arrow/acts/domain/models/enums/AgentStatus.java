package com.arrow.acts.domain.models.enums;

/**
 * Estado operativo actual de un miembro del equipo Arrow.
 */
public enum AgentStatus {

    /** El agente está disponible para ser asignado a una misión. */
    AVAILABLE,

    /** El agente está actualmente ejecutando una misión. */
    ON_MISSION,

    /** El agente está fuera de servicio por lesión. */
    INJURED,

    /** El agente no está localizable o no está en el equipo actualmente. */
    OFFLINE
}
