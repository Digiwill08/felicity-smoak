package com.arrow.acts.application.adapters.persistence.sql;

import com.arrow.acts.application.adapters.persistence.sql.entities.MirakuruSubjectEntity;
import com.arrow.acts.application.adapters.persistence.sql.repositories.MirakuruSubjectRepository;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia SQL para sujetos Mirakuru.
 *
 * <p>Implementa {@link MirakuruSubjectPort} usando Spring Data JPA + MySQL.
 * Traduce entre la entidad JPA ({@link MirakuruSubjectEntity}) y el
 * modelo de dominio ({@link MirakuruSubject}), manteniendo el dominio
 * completamente aislado de la tecnología de persistencia.</p>
 */
@Service
public class MirakuruSubjectPersistenceAdapter implements MirakuruSubjectPort {

    private final MirakuruSubjectRepository repository;

    public MirakuruSubjectPersistenceAdapter(MirakuruSubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByAlias(String alias) {
        return repository.existsByAlias(alias);
    }

    @Override
    public void save(MirakuruSubject subject) {
        repository.save(toEntity(subject));
    }

    @Override
    public Optional<MirakuruSubject> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<MirakuruSubject> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MirakuruSubject> findAllActive() {
        // Activos = sujetos con ThreatLevel no nulo (registrados y clasificados)
        return repository.findAll()
                .stream()
                .filter(e -> e.getThreatLevel() != null)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // ── Mappers Entity ↔ Domain ────────────────────────────────────────────────

    private MirakuruSubjectEntity toEntity(MirakuruSubject subject) {
        MirakuruSubjectEntity entity = new MirakuruSubjectEntity();
        entity.setId(subject.getId());
        entity.setAlias(subject.getAlias());
        entity.setMirakuruConcentration(subject.getMirakuruConcentration());
        entity.setLastKnownLocation(subject.getLastKnownLocation());
        entity.setBehavioralStatus(subject.getBehavioralStatus());
        entity.setThreatLevel(subject.getThreatLevel());
        entity.setLastSightingSource(subject.getLastSightingSource());
        entity.setRegisteredAt(subject.getRegisteredAt());
        entity.setUpdatedAt(subject.getUpdatedAt());
        return entity;
    }

    private MirakuruSubject toDomain(MirakuruSubjectEntity entity) {
        MirakuruSubject subject = new MirakuruSubject();
        subject.setId(entity.getId());
        subject.setAlias(entity.getAlias());
        subject.setMirakuruConcentration(entity.getMirakuruConcentration());
        subject.setLastKnownLocation(entity.getLastKnownLocation());
        subject.setBehavioralStatus(entity.getBehavioralStatus());
        subject.setThreatLevel(entity.getThreatLevel());
        subject.setLastSightingSource(entity.getLastSightingSource());
        subject.setRegisteredAt(entity.getRegisteredAt());
        subject.setUpdatedAt(entity.getUpdatedAt());
        return subject;
    }
}
