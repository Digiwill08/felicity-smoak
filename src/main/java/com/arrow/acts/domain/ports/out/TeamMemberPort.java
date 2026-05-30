package com.arrow.acts.domain.ports.out;

import com.arrow.acts.domain.models.team.TeamMember;
import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.TeamRole;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: acceso a información del equipo Arrow.
 *
 * <p>Permite consultar el estado y las capacidades de los miembros del equipo
 * sin acoplarse a ninguna tecnología de almacenamiento concreta.</p>
 */
public interface TeamMemberPort {

    void save(TeamMember member);

    Optional<TeamMember> findById(Long id);

    List<TeamMember> findAll();

    List<TeamMember> findByStatus(AgentStatus status);

    List<TeamMember> findByRole(TeamRole role);

    boolean existsById(Long id);

    void deleteById(Long id);
}
