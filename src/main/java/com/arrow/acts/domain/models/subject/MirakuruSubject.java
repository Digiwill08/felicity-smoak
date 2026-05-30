package com.arrow.acts.domain.models.subject;

import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Agregado raíz: Individuo afectado por el suero Mirakuru.
 *
 * <p>Representa a una persona bajo los efectos del suero, con
 * información sobre su nivel de concentración, ubicación, estado
 * conductual y nivel de amenaza calculado por el dominio.</p>
 *
 * <p>Invariantes:
 * <ul>
 *   <li>La concentración de Mirakuru debe estar en rango [0.0, 10.0].</li>
 *   <li>Un sujeto CRITICAL debe tener al menos un avistamiento confirmado.</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
public class MirakuruSubject {

    private Long id;

    /** Nombre real o alias conocido del sujeto. */
    private String alias;

    /**
     * Concentración estimada del suero Mirakuru en escala 0.0 (mínimo) a 10.0 (máximo).
     * Validada en el servicio de dominio antes de ser asignada.
     */
    private double mirakuruConcentration;

    /** Última ubicación geográfica conocida del sujeto. */
    private String lastKnownLocation;

    /** Estado conductual actual del sujeto. */
    private BehavioralStatus behavioralStatus;

    /** Nivel de amenaza calculado por ThreatClassificationService. */
    private ThreatLevel threatLevel;

    /** Fuente o descripción breve del avistamiento más reciente. */
    private String lastSightingSource;

    /** Momento en que el sujeto fue registrado por primera vez en el sistema. */
    private Instant registeredAt;

    /** Momento de la última actualización del registro. */
    private Instant updatedAt;
}
