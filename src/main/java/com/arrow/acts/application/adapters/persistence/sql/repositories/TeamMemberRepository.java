package com.arrow.acts.application.adapters.persistence.sql.repositories;

import com.arrow.acts.application.adapters.persistence.sql.entities.TeamMemberEntity;
import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para miembros del equipo Arrow.
 */
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {

    List<TeamMemberEntity> findByCurrentStatus(AgentStatus status);

    List<TeamMemberEntity> findByRole(TeamRole role);

    boolean existsById(Long id);
}
