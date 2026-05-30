package com.arrow.acts.application.adapters.persistence.mongodb.repositories;

import com.arrow.acts.application.adapters.persistence.mongodb.documents.IntelligenceReportDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repositorio Spring Data MongoDB para reportes de inteligencia.
 */
public interface IntelligenceReportMongoRepository
        extends MongoRepository<IntelligenceReportDocument, String> {

    /** Obtiene reportes pendientes de procesar para un sujeto específico. */
    List<IntelligenceReportDocument> findBySubjectIdAndProcessedFalse(Long subjectId);

    /** Obtiene todos los reportes recientes sin procesar de todas las fuentes. */
    List<IntelligenceReportDocument> findByProcessedFalse();

    /** Obtiene todos los reportes de un sujeto (historial completo). */
    List<IntelligenceReportDocument> findBySubjectId(Long subjectId);
}
