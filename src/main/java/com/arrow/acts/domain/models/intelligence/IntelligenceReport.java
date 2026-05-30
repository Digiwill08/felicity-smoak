package com.arrow.acts.domain.models.intelligence;

import com.arrow.acts.domain.models.enums.IntelligenceConfidenceLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Reporte de inteligencia recibido de una fuente externa.
 *
 * <p>Representa información sobre un sujeto Mirakuru proveniente de
 * fuentes como ARGUS, cámaras de vigilancia, informantes, etc.
 * Es usado por IntelligenceAggregatorService para actualizar el
 * estado de un MirakuruSubject.</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class IntelligenceReport {

    private Long id;

    /** ID del sujeto Mirakuru al que se refiere este reporte. */
    private Long subjectId;

    /** Nombre o identificador de la fuente del reporte (ej: "ARGUS-CAM-04"). */
    private String source;

    /** Descripción del comportamiento observado. */
    private String observation;

    /** Ubicación donde fue observado el sujeto. */
    private String location;

    /** Concentración estimada de Mirakuru según esta fuente. */
    private double estimatedConcentration;

    /** Nivel de confianza de la información de este reporte. */
    private IntelligenceConfidenceLevel confidenceLevel;

    /** Momento en que se generó el reporte. */
    private Instant reportedAt;
}
