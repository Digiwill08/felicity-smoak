package com.arrow.acts.application.adapters.persistence.sql.entities;

import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidad JPA para persistir sujetos Mirakuru en MySQL.
 *
 * <p>Representa la tabla {@code mirakuru_subjects} en la base de datos
 * {@code felicity_smoak}. Completamente desacoplada del modelo de dominio:
 * la traducción se realiza en {@code MirakuruSubjectPersistenceAdapter}.</p>
 */
@Entity
@Table(name = "mirakuru_subjects")
@Getter
@Setter
@NoArgsConstructor
public class MirakuruSubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String alias;

    @Column(name = "mirakuru_concentration", nullable = false)
    private double mirakuruConcentration;

    @Column(name = "last_known_location", length = 255)
    private String lastKnownLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "behavioral_status", nullable = false, length = 20)
    private BehavioralStatus behavioralStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "threat_level", length = 20)
    private ThreatLevel threatLevel;

    @Column(name = "last_sighting_source", length = 150)
    private String lastSightingSource;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
