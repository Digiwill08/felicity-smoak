package com.arrow.acts.domain.models.enums;

/**
 * Estado conductual de un individuo bajo el efecto del suero Mirakuru.
 * Determina parcialmente el nivel de amenaza calculado por ThreatClassificationService.
 */
public enum BehavioralStatus {

    /** El sujeto muestra comportamiento controlado. Sin amenaza inmediata. */
    STABLE,

    /** El sujeto muestra signos de irritabilidad y agresión intermitente. */
    AGGRESSIVE,

    /** El sujeto está fuera de control. Amenaza inmediata máxima. */
    BERSERK,

    /** No hay información suficiente para clasificar el estado del sujeto. */
    UNKNOWN
}
