package com.arrow.acts.domain.services;

import com.arrow.acts.domain.exceptions.SubjectNotFoundException;
import com.arrow.acts.domain.models.enums.IntelligenceConfidenceLevel;
import com.arrow.acts.domain.models.intelligence.IntelligenceReport;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Servicio de dominio: Agregación y procesamiento de inteligencia externa.
 *
 * <p>Responsabilidad única: recibir reportes de múltiples fuentes de inteligencia
 * y consolidarlos para actualizar el estado de un sujeto Mirakuru. Aplica
 * ponderación según el nivel de confianza de cada reporte.</p>
 *
 * <p>Regla de consolidación:
 * <ul>
 *   <li>Reportes CONFIRMED tienen peso máximo y sobreescriben los anteriores.</li>
 *   <li>Reportes HIGH se promedian con el estado actual.</li>
 *   <li>Reportes UNVERIFIED se ignoran para la actualización de concentración.</li>
 *   <li>La ubicación siempre se actualiza con el reporte más reciente de HIGH+.</li>
 * </ul>
 * </p>
 *
 * <p>Principio SOLID aplicado: SRP + DIP — solo agrega inteligencia y
 * depende de abstracciones (puertos), no de implementaciones concretas.</p>
 */
@Service
public class IntelligenceAggregatorService {

    private final MirakuruSubjectPort subjectPort;
    private final ThreatClassificationService classificationService;

    public IntelligenceAggregatorService(MirakuruSubjectPort subjectPort,
                                         ThreatClassificationService classificationService) {
        this.subjectPort = subjectPort;
        this.classificationService = classificationService;
    }

    /**
     * Procesa una lista de reportes de inteligencia sobre un sujeto específico
     * y actualiza su estado en el dominio.
     *
     * @param subjectId ID del sujeto a actualizar
     * @param reports Lista de reportes a procesar
     * @throws SubjectNotFoundException si el sujeto no existe
     */
    public void processReports(Long subjectId, List<IntelligenceReport> reports) {
        MirakuruSubject subject = subjectPort.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        reports.stream()
               .filter(r -> r.getConfidenceLevel() != IntelligenceConfidenceLevel.UNVERIFIED)
               .sorted((a, b) -> confidenceWeight(b.getConfidenceLevel())
                               - confidenceWeight(a.getConfidenceLevel()))
               .forEach(report -> applyReport(subject, report));

        subject.setUpdatedAt(Instant.now());
        classificationService.classify(subject);
        subjectPort.save(subject);
    }

    // ── Lógica privada ─────────────────────────────────────────────────────────

    private void applyReport(MirakuruSubject subject, IntelligenceReport report) {
        int weight = confidenceWeight(report.getConfidenceLevel());

        if (weight >= confidenceWeight(IntelligenceConfidenceLevel.HIGH)) {
            subject.setLastKnownLocation(report.getLocation());
            subject.setLastSightingSource(report.getSource());
        }

        if (report.getConfidenceLevel() == IntelligenceConfidenceLevel.CONFIRMED) {
            subject.setMirakuruConcentration(report.getEstimatedConcentration());
        } else if (weight >= confidenceWeight(IntelligenceConfidenceLevel.HIGH)) {
            double averaged = (subject.getMirakuruConcentration() + report.getEstimatedConcentration()) / 2.0;
            subject.setMirakuruConcentration(Math.min(10.0, Math.max(0.0, averaged)));
        }
    }

    private int confidenceWeight(IntelligenceConfidenceLevel level) {
        return switch (level) {
            case UNVERIFIED -> 0;
            case LOW        -> 1;
            case MEDIUM     -> 2;
            case HIGH       -> 3;
            case CONFIRMED  -> 4;
        };
    }
}
