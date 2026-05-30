package com.arrow.acts.application.adapters.persistence.sql.entities;

import com.arrow.acts.domain.models.enums.MissionPriority;
import com.arrow.acts.domain.models.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidad JPA para persistir misiones tácticas en MySQL.
 *
 * <p>Representa la tabla {@code missions} en la base de datos
 * {@code felicity_smoak}. Las listas de IDs de objetivos y agentes
 * se almacenan como texto JSON serializado.</p>
 */
@Entity
@Table(name = "missions")
@Getter
@Setter
@NoArgsConstructor
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String objective;

    /**
     * IDs de sujetos objetivo, almacenados como CSV (ej: "1,2,5").
     * La conversión se hace en el adaptador de persistencia.
     */
    @Column(name = "target_subject_ids", columnDefinition = "TEXT")
    private String targetSubjectIds;

    /**
     * IDs de agentes asignados, almacenados como CSV (ej: "2,3").
     */
    @Column(name = "assigned_agent_ids", columnDefinition = "TEXT")
    private String assignedAgentIds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MissionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MissionPriority priority;

    @Column(name = "operational_zone", length = 150)
    private String operationalZone;

    @Column(name = "scheduled_start")
    private Instant scheduledStart;

    @Column(name = "actual_start")
    private Instant actualStart;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
