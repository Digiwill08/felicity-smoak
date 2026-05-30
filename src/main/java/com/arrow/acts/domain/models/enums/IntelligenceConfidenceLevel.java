package com.arrow.acts.domain.models.enums;

/**
 * Nivel de confianza de un reporte de inteligencia.
 * Determina el peso que tiene la información al actualizar el estado de un sujeto.
 */
public enum IntelligenceConfidenceLevel {

    /** Información sin verificar. Puede ser incorrecta. */
    UNVERIFIED,

    /** Información con evidencia parcial. Requiere confirmación. */
    LOW,

    /** Información con múltiples fuentes o evidencia directa moderada. */
    MEDIUM,

    /** Información confirmada por fuentes confiables o evidencia directa. */
    HIGH,

    /** Información verificada por múltiples fuentes independientes de primer nivel. */
    CONFIRMED
}
