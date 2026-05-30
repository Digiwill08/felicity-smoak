package com.arrow.acts.application.usecases;

import com.arrow.acts.domain.exceptions.SubjectNotFoundException;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.in.ThreatUseCase;
import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import com.arrow.acts.domain.services.ThreatClassificationService;
import com.arrow.acts.domain.services.ThreatPrioritizationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del caso de uso de análisis y priorización de amenazas.
 *
 * <p>Orquesta ThreatPrioritizationService y ThreatClassificationService
 * para proporcionar vistas priorizadas de amenazas activas en el sistema.</p>
 */
@Service
public class ThreatUseCaseImpl implements ThreatUseCase {

    private final MirakuruSubjectPort subjectPort;
    private final ThreatPrioritizationService prioritizationService;
    private final ThreatClassificationService classificationService;

    public ThreatUseCaseImpl(MirakuruSubjectPort subjectPort,
                              ThreatPrioritizationService prioritizationService,
                              ThreatClassificationService classificationService) {
        this.subjectPort = subjectPort;
        this.prioritizationService = prioritizationService;
        this.classificationService = classificationService;
    }

    @Override
    public List<MirakuruSubject> getPrioritizedThreats() {
        List<MirakuruSubject> all = subjectPort.findAllActive();
        return prioritizationService.prioritize(all);
    }

    @Override
    public List<MirakuruSubject> getHighPriorityThreats() {
        List<MirakuruSubject> all = subjectPort.findAllActive();
        return prioritizationService.getHighPriorityThreats(all);
    }

    @Override
    public void reclassifyThreat(Long subjectId) {
        MirakuruSubject subject = subjectPort.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));
        classificationService.classify(subject);
        subjectPort.save(subject);
    }
}
