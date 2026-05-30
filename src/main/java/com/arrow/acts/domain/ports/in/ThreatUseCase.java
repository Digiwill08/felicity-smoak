package com.arrow.acts.domain.ports.in;

import com.arrow.acts.domain.models.subject.MirakuruSubject;

import java.util.List;

/**
 * Puerto de entrada: operaciones de análisis y priorización de amenazas.
 *
 * <p>Define los casos de uso disponibles para consultar, clasificar
 * y obtener listas priorizadas de amenazas activas en la ciudad.</p>
 */
public interface ThreatUseCase {

    /**
     * Obtiene todos los sujetos ordenados por prioridad táctica.
     *
     * @return Lista priorizada de mayor a menor amenaza
     */
    List<MirakuruSubject> getPrioritizedThreats();

    /**
     * Obtiene únicamente los sujetos de amenaza CRITICAL o HIGH.
     *
     * @return Lista de amenazas altas y críticas, ordenada
     */
    List<MirakuruSubject> getHighPriorityThreats();

    /**
     * Reclasifica manualmente el nivel de amenaza de un sujeto.
     *
     * @param subjectId ID del sujeto a reclasificar
     */
    void reclassifyThreat(Long subjectId);
}
