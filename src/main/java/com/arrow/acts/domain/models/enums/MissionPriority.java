package com.arrow.acts.domain.models.enums;

/**
 * Nivel de prioridad de una misión. Define la urgencia de respuesta
 * y los requisitos mínimos de agentes para su activación.
 */
public enum MissionPriority {

    /** Sin urgencia inmediata. Planificación preventiva. */
    LOW,

    /** Requiere atención en las próximas horas. */
    MEDIUM,

    /** Urgente. Despliegue en las próximas horas. */
    HIGH,

    /**
     * Emergencia máxima. Requiere al menos un agente de campo disponible.
     * No puede activarse sin objetivos definidos.
     */
    CRITICAL
}
