package com.arrow.acts.domain.models.enums;

/**
 * Nivel de amenaza de un individuo Mirakuru.
 * Calculado por ThreatClassificationService en función de concentración y estado conductual.
 */
public enum ThreatLevel {

    /** Sin amenaza activa. Monitoreo estándar. */
    LOW,

    /** Amenaza moderada. Se recomienda vigilancia reforzada. */
    MEDIUM,

    /** Amenaza elevada. Se requiere intervención táctica. */
    HIGH,

    /** Amenaza máxima. Respuesta inmediata del equipo Arrow. */
    CRITICAL
}
