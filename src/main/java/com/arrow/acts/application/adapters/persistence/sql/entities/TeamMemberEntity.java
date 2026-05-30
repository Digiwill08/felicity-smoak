package com.arrow.acts.application.adapters.persistence.sql.entities;

import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.TeamRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA para persistir miembros del equipo Arrow en MySQL.
 *
 * <p>Representa la tabla {@code team_members} en la base de datos
 * {@code felicity_smoak}. Las capacidades se almacenan como CSV.</p>
 */
@Entity
@Table(name = "team_members")
@Getter
@Setter
@NoArgsConstructor
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String codename;

    @Column(name = "real_name", length = 150)
    private String realName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TeamRole role;

    /**
     * Capacidades del agente como CSV (ej: "ARCHERY,HACKING,STEALTH_OPERATIONS").
     * La conversión se hace en el adaptador de persistencia.
     */
    @Column(columnDefinition = "TEXT")
    private String capabilities;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false, length = 20)
    private AgentStatus currentStatus;
}
