package com.arrow.acts.application.adapters.api.request;

import com.arrow.acts.domain.models.enums.BehavioralStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para registrar o actualizar un sujeto Mirakuru.
 * Contiene los datos provenientes del cliente (REST API).
 */
@Getter
@Setter
public class MirakuruSubjectRequest {

    @NotBlank(message = "El alias del sujeto no puede estar vacío.")
    private String alias;

    @NotNull(message = "La concentración de Mirakuru es obligatoria.")
    @DecimalMin(value = "0.0", message = "La concentración mínima es 0.0.")
    @DecimalMax(value = "10.0", message = "La concentración máxima es 10.0.")
    private Double mirakuruConcentration;

    @NotBlank(message = "La ubicación conocida no puede estar vacía.")
    private String lastKnownLocation;

    @NotNull(message = "El estado conductual es obligatorio.")
    private BehavioralStatus behavioralStatus;

    private String lastSightingSource;
}
