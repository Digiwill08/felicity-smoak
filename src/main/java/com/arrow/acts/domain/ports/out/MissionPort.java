package com.arrow.acts.domain.ports.out;

import com.arrow.acts.domain.models.mission.Mission;
import com.arrow.acts.domain.models.enums.MissionStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia de misiones tácticas.
 *
 * <p>Abstracción de las operaciones CRUD sobre misiones del equipo Arrow.
 * Desacopla completamente el dominio de la tecnología de persistencia.</p>
 */
public interface MissionPort {

    void save(Mission mission);

    Optional<Mission> findById(Long id);

    List<Mission> findAll();

    List<Mission> findByStatus(MissionStatus status);

    void deleteById(Long id);
}
