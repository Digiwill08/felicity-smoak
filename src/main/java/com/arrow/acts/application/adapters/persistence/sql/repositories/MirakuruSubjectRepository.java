package com.arrow.acts.application.adapters.persistence.sql.repositories;

import com.arrow.acts.application.adapters.persistence.sql.entities.MirakuruSubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para sujetos Mirakuru.
 */
public interface MirakuruSubjectRepository extends JpaRepository<MirakuruSubjectEntity, Long> {

    boolean existsByAlias(String alias);

    Optional<MirakuruSubjectEntity> findByAlias(String alias);
}
