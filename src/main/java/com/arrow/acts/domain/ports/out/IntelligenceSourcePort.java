package com.arrow.acts.domain.ports.out;

import com.arrow.acts.domain.models.intelligence.IntelligenceReport;

import java.util.List;

/**
 * Puerto de salida: recepción de reportes de inteligencia externa.
 *
 * <p>Abstrae la integración con múltiples fuentes de datos externas:
 * ARGUS, cámaras de vigilancia, informantes, satélites, etc.
 * Cada adaptador de infraestructura implementa este puerto para su fuente.</p>
 */
public interface IntelligenceSourcePort {

    /**
     * Recupera todos los reportes pendientes de procesar para un sujeto específico.
     *
     * @param subjectId ID del sujeto en el sistema ACTS
     * @return Lista de reportes de inteligencia disponibles
     */
    List<IntelligenceReport> fetchPendingReports(Long subjectId);

    /**
     * Recupera todos los reportes recientes sin filtrar por sujeto.
     *
     * @return Lista de todos los reportes recientes de todas las fuentes
     */
    List<IntelligenceReport> fetchAllRecentReports();

    /**
     * Guarda un reporte de inteligencia procesado en el sistema.
     *
     * @param report Reporte a persistir
     */
    void saveReport(IntelligenceReport report);
}
