package com.arrow.acts.application.adapters.persistence.sql;

import com.arrow.acts.application.adapters.persistence.sql.entities.TeamMemberEntity;
import com.arrow.acts.application.adapters.persistence.sql.repositories.TeamMemberRepository;
import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.Capability;
import com.arrow.acts.domain.models.enums.TeamRole;
import com.arrow.acts.domain.models.team.TeamMember;
import com.arrow.acts.domain.ports.out.TeamMemberPort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia SQL para miembros del equipo Arrow.
 *
 * <p>Implementa {@link TeamMemberPort} usando Spring Data JPA + MySQL.
 * Convierte el Set de capacidades a/desde CSV para el almacenamiento relacional.</p>
 */
@Service
public class TeamMemberPersistenceAdapter implements TeamMemberPort {

    private final TeamMemberRepository repository;

    public TeamMemberPersistenceAdapter(TeamMemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(TeamMember member) {
        repository.save(toEntity(member));
    }

    @Override
    public Optional<TeamMember> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<TeamMember> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamMember> findByStatus(AgentStatus status) {
        return repository.findByCurrentStatus(status)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamMember> findByRole(TeamRole role) {
        return repository.findByRole(role)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // ── Mappers Entity ↔ Domain ────────────────────────────────────────────────

    private TeamMemberEntity toEntity(TeamMember member) {
        TeamMemberEntity entity = new TeamMemberEntity();
        entity.setId(member.getId());
        entity.setCodename(member.getCodename());
        entity.setRealName(member.getRealName());
        entity.setRole(member.getRole());
        entity.setCapabilities(capabilitiesToCSV(member.getCapabilities()));
        entity.setCurrentStatus(member.getCurrentStatus());
        return entity;
    }

    private TeamMember toDomain(TeamMemberEntity entity) {
        TeamMember member = new TeamMember();
        member.setId(entity.getId());
        member.setCodename(entity.getCodename());
        member.setRealName(entity.getRealName());
        member.setRole(entity.getRole());
        member.setCapabilities(csvToCapabilities(entity.getCapabilities()));
        member.setCurrentStatus(entity.getCurrentStatus());
        return member;
    }

    /** Convierte Set de Capability a CSV: {ARCHERY, HACKING} → "ARCHERY,HACKING" */
    private String capabilitiesToCSV(Set<Capability> capabilities) {
        if (capabilities == null || capabilities.isEmpty()) return "";
        return capabilities.stream()
                .map(Capability::name)
                .collect(Collectors.joining(","));
    }

    /** Convierte CSV a Set de Capability: "ARCHERY,HACKING" → {ARCHERY, HACKING} */
    private Set<Capability> csvToCapabilities(String csv) {
        if (csv == null || csv.isBlank()) return Collections.emptySet();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Capability::valueOf)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
