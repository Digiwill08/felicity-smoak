package com.arrow.acts.application.adapters.persistence.sql.repositories;

import com.arrow.acts.application.adapters.persistence.sql.entities.MissionEntity;
import com.arrow.acts.domain.models.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para misiones tácticas.
 */
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    List<MissionEntity> findByStatus(MissionStatus status);
}
