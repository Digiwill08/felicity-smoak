package com.arrow.acts.domain.models.team;

import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.Capability;
import com.arrow.acts.domain.models.enums.TeamRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Agregado raíz: Miembro del equipo Arrow.
 *
 * <p>Representa a cualquier integrante del equipo, ya sea agente de campo,
 * soporte técnico o analista. Cada miembro tiene capacidades específicas
 * y un estado operativo que determina su disponibilidad para misiones.</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class TeamMember {

    private Long id;

    /** Nombre en clave del agente dentro del equipo (ej: "Arrow", "Canary"). */
    private String codename;

    /** Nombre real del miembro (puede ser confidencial). */
    private String realName;

    /** Rol funcional del miembro dentro del equipo. */
    private TeamRole role;

    /** Conjunto de habilidades y capacidades del miembro. */
    private Set<Capability> capabilities = new HashSet<>();

    /** Estado operativo actual del miembro. */
    private AgentStatus currentStatus;
}
