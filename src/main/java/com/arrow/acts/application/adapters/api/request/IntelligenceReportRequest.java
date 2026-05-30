package com.arrow.acts.application.adapters.api.request;

import com.arrow.acts.domain.models.enums.IntelligenceConfidenceLevel;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para registrar un reporte de inteligencia externa sobre un sujeto.
 */
@Getter
@Setter
public class IntelligenceReportRequest {

    @NotNull(message = "El ID del sujeto es obligatorio.")
    private Long subjectId;

    @NotBlank(message = "La fuente del reporte no puede estar vacía.")
    private String source;

    @NotBlank(message = "La observación no puede estar vacía.")
    private String observation;

    @NotBlank(message = "La ubicación observada no puede estar vacía.")
    private String location;

    @NotNull(message = "La concentración estimada es obligatoria.")
    @DecimalMin(value = "0.0", message = "La concentración mínima es 0.0.")
    @DecimalMax(value = "10.0", message = "La concentración máxima es 10.0.")
    private Double estimatedConcentration;

    @NotNull(message = "El nivel de confianza es obligatorio.")
    private IntelligenceConfidenceLevel confidenceLevel;
}
