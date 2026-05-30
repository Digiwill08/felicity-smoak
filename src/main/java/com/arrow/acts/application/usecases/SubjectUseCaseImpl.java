package com.arrow.acts.application.usecases;

import com.arrow.acts.domain.exceptions.DuplicateSubjectException;
import com.arrow.acts.domain.exceptions.SubjectNotFoundException;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.in.SubjectUseCase;
import com.arrow.acts.domain.ports.out.AlertNotificationPort;
import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import com.arrow.acts.domain.services.ThreatClassificationService;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Implementación del caso de uso de gestión de sujetos Mirakuru.
 *
 * <p>Orquesta los servicios de dominio y los puertos necesarios para
 * registrar, consultar y actualizar sujetos en el sistema ACTS.</p>
 */
@Service
public class SubjectUseCaseImpl implements SubjectUseCase {

    private final MirakuruSubjectPort subjectPort;
    private final ThreatClassificationService classificationService;
    private final AlertNotificationPort alertPort;

    public SubjectUseCaseImpl(MirakuruSubjectPort subjectPort,
                               ThreatClassificationService classificationService,
                               AlertNotificationPort alertPort) {
        this.subjectPort = subjectPort;
        this.classificationService = classificationService;
        this.alertPort = alertPort;
    }

    @Override
    public void registerSubject(MirakuruSubject subject) {
        if (subjectPort.existsByAlias(subject.getAlias())) {
            throw new DuplicateSubjectException(subject.getAlias());
        }
        subject.setRegisteredAt(Instant.now());
        subject.setUpdatedAt(Instant.now());
        classificationService.classify(subject);
        subjectPort.save(subject);

        if (subject.getThreatLevel() == ThreatLevel.CRITICAL) {
            alertPort.notifyCriticalThreat(subject);
        }
    }

    @Override
    public List<MirakuruSubject> getAllSubjects() {
        return subjectPort.findAll();
    }

    @Override
    public MirakuruSubject getSubjectById(Long id) {
        return subjectPort.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
    }

    @Override
    public void updateSubject(MirakuruSubject subject) {
        subjectPort.findById(subject.getId())
                .orElseThrow(() -> new SubjectNotFoundException(subject.getId()));
        subject.setUpdatedAt(Instant.now());
        classificationService.classify(subject);
        subjectPort.save(subject);

        if (subject.getThreatLevel() == ThreatLevel.CRITICAL) {
            alertPort.notifyCriticalThreat(subject);
        }
    }

    @Override
    public void deleteSubject(Long id) {
        subjectPort.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
        subjectPort.deleteById(id);
    }
}
