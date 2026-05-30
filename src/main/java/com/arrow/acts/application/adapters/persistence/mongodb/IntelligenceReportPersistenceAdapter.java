package com.arrow.acts.application.adapters.persistence.mongodb;

import com.arrow.acts.application.adapters.persistence.mongodb.documents.IntelligenceReportDocument;
import com.arrow.acts.application.adapters.persistence.mongodb.repositories.IntelligenceReportMongoRepository;
import com.arrow.acts.domain.models.intelligence.IntelligenceReport;
import com.arrow.acts.domain.ports.out.IntelligenceSourcePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia MongoDB para reportes de inteligencia.
 *
 * <p>Implementa {@link IntelligenceSourcePort} usando Spring Data MongoDB.
 * Traduce entre el documento MongoDB ({@link IntelligenceReportDocument})
 * y el modelo de dominio ({@link IntelligenceReport}).</p>
 *
 * <p>MongoDB es ideal aquí por:
 * <ul>
 *   <li>Alta frecuencia de escritura de reportes en tiempo real.</li>
 *   <li>Estructura de datos variable según la fuente (ARGUS, cámaras, informantes).</li>
 *   <li>Consultas rápidas por subjectId sin joins complejos.</li>
 * </ul>
 * </p>
 */
@Service
public class IntelligenceReportPersistenceAdapter implements IntelligenceSourcePort {

    private final IntelligenceReportMongoRepository mongoRepository;

    public IntelligenceReportPersistenceAdapter(IntelligenceReportMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<IntelligenceReport> fetchPendingReports(Long subjectId) {
        return mongoRepository.findBySubjectIdAndProcessedFalse(subjectId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<IntelligenceReport> fetchAllRecentReports() {
        return mongoRepository.findByProcessedFalse()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveReport(IntelligenceReport report) {
        mongoRepository.save(toDocument(report));
    }

    // ── Mappers Document ↔ Domain ──────────────────────────────────────────────

    private IntelligenceReportDocument toDocument(IntelligenceReport report) {
        IntelligenceReportDocument doc = new IntelligenceReportDocument();
        doc.setSubjectId(report.getSubjectId());
        doc.setSource(report.getSource());
        doc.setObservation(report.getObservation());
        doc.setLocation(report.getLocation());
        doc.setEstimatedConcentration(report.getEstimatedConcentration());
        doc.setConfidenceLevel(report.getConfidenceLevel());
        doc.setReportedAt(report.getReportedAt());
        doc.setProcessed(false);
        return doc;
    }

    private IntelligenceReport toDomain(IntelligenceReportDocument doc) {
        IntelligenceReport report = new IntelligenceReport();
        report.setSubjectId(doc.getSubjectId());
        report.setSource(doc.getSource());
        report.setObservation(doc.getObservation());
        report.setLocation(doc.getLocation());
        report.setEstimatedConcentration(doc.getEstimatedConcentration());
        report.setConfidenceLevel(doc.getConfidenceLevel());
        report.setReportedAt(doc.getReportedAt());
        return report;
    }
}
