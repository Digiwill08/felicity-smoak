package com.arrow.acts.application.adapters.persistence.sql;

import com.arrow.acts.application.adapters.persistence.sql.entities.MissionEntity;
import com.arrow.acts.application.adapters.persistence.sql.repositories.MissionRepository;
import com.arrow.acts.domain.models.enums.MissionStatus;
import com.arrow.acts.domain.models.mission.Mission;
import com.arrow.acts.domain.ports.out.MissionPort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia SQL para misiones tácticas.
 *
 * <p>Implementa {@link MissionPort} usando Spring Data JPA + MySQL.
 * Convierte las listas de IDs (targetSubjectIds, assignedAgentIds)
 * a/desde formato CSV para el almacenamiento relacional.</p>
 */
@Service
public class MissionPersistenceAdapter implements MissionPort {

    private final MissionRepository repository;

    public MissionPersistenceAdapter(MissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Mission mission) {
        repository.save(toEntity(mission));
    }

    @Override
    public Optional<Mission> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Mission> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mission> findByStatus(MissionStatus status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // ── Mappers Entity ↔ Domain ────────────────────────────────────────────────

    private MissionEntity toEntity(Mission mission) {
        MissionEntity entity = new MissionEntity();
        entity.setId(mission.getId());
        entity.setObjective(mission.getObjective());
        entity.setTargetSubjectIds(idsToCSV(mission.getTargetSubjectIds()));
        entity.setAssignedAgentIds(idsToCSV(mission.getAssignedAgentIds()));
        entity.setStatus(mission.getStatus());
        entity.setPriority(mission.getPriority());
        entity.setOperationalZone(mission.getOperationalZone());
        entity.setScheduledStart(mission.getScheduledStart());
        entity.setActualStart(mission.getActualStart());
        entity.setEndTime(mission.getEndTime());
        entity.setNotes(mission.getNotes());
        return entity;
    }

    private Mission toDomain(MissionEntity entity) {
        Mission mission = new Mission();
        mission.setId(entity.getId());
        mission.setObjective(entity.getObjective());
        mission.setTargetSubjectIds(csvToIds(entity.getTargetSubjectIds()));
        mission.setAssignedAgentIds(csvToIds(entity.getAssignedAgentIds()));
        mission.setStatus(entity.getStatus());
        mission.setPriority(entity.getPriority());
        mission.setOperationalZone(entity.getOperationalZone());
        mission.setScheduledStart(entity.getScheduledStart());
        mission.setActualStart(entity.getActualStart());
        mission.setEndTime(entity.getEndTime());
        mission.setNotes(entity.getNotes());
        return mission;
    }

    /** Convierte lista de IDs a String CSV: [1, 2, 5] → "1,2,5" */
    private String idsToCSV(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return "";
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /** Convierte String CSV a lista de IDs: "1,2,5" → [1, 2, 5] */
    private List<Long> csvToIds(String csv) {
        if (csv == null || csv.isBlank()) return Collections.emptyList();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
