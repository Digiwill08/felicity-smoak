package com.arrow.acts.domain.models.enums;

/**
 * Rol de un miembro dentro del equipo Arrow.
 * Determina qué tipo de misiones puede liderar o apoyar.
 */
public enum TeamRole {

    /** Agente operativo en campo (ej: Arrow, Canary, Arsenal). */
    FIELD_AGENT,

    /** Soporte técnico y de inteligencia (ej: Felicity Smoak). */
    TECH_SUPPORT,

    /** Analista estratégico y de datos. */
    ANALYST
}
