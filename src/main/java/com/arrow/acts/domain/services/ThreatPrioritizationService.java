package com.arrow.acts.domain.services;

import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Servicio de dominio: Priorización de amenazas activas.
 *
 * <p>Responsabilidad única: ordenar una lista de sujetos Mirakuru activos
 * según su nivel de amenaza, de mayor a menor urgencia táctica.</p>
 *
 * <p>Criterio de ordenamiento:
 * <ol>
 *   <li>ThreatLevel (CRITICAL > HIGH > MEDIUM > LOW)</li>
 *   <li>Concentración de Mirakuru (mayor concentración = mayor prioridad)</li>
 *   <li>Fecha de registro más reciente (sujetos nuevos primero)</li>
 * </ol>
 * </p>
 *
 * <p>Principio SOLID aplicado: OCP — el criterio de ordenamiento puede
 * extenderse via Comparator sin modificar este servicio.</p>
 */
@Service
public class ThreatPrioritizationService {

    /**
     * Retorna la lista de sujetos ordenada por prioridad táctica descendente.
     *
     * @param subjects Lista de sujetos activos en el sistema
     * @return Lista ordenada de mayor a menor prioridad táctica
     */
    public List<MirakuruSubject> prioritize(List<MirakuruSubject> subjects) {
        return subjects.stream()
                .sorted(buildPriorityComparator())
                .toList();
    }

    /**
     * Retorna únicamente los sujetos con amenaza CRITICAL o HIGH.
     *
     * @param subjects Lista de sujetos activos
     * @return Lista filtrada de amenazas altas y críticas, ordenada
     */
    public List<MirakuruSubject> getHighPriorityThreats(List<MirakuruSubject> subjects) {
        return subjects.stream()
                .filter(s -> s.getThreatLevel() == ThreatLevel.CRITICAL
                          || s.getThreatLevel() == ThreatLevel.HIGH)
                .sorted(buildPriorityComparator())
                .toList();
    }

    // ── Comparador privado ─────────────────────────────────────────────────────

    private Comparator<MirakuruSubject> buildPriorityComparator() {
        return Comparator
                // 1. ThreatLevel descendente: CRITICAL(3) > HIGH(2) > MEDIUM(1) > LOW(0)
                .<MirakuruSubject, Integer>comparing(
                        s -> threatLevelOrdinal(s.getThreatLevel()),
                        Comparator.reverseOrder()
                )
                // 2. Concentración descendente (mayor concentración = mayor prioridad)
                .thenComparing(
                        Comparator.comparingDouble(MirakuruSubject::getMirakuruConcentration)
                                  .reversed()
                )
                // 3. Registro más reciente primero
                .thenComparing(
                        s -> s.getRegisteredAt() != null ? s.getRegisteredAt() : java.time.Instant.MIN,
                        Comparator.reverseOrder()
                );
    }

    private int threatLevelOrdinal(ThreatLevel level) {
        if (level == null) return 0;
        return switch (level) {
            case LOW      -> 0;
            case MEDIUM   -> 1;
            case HIGH     -> 2;
            case CRITICAL -> 3;
        };
    }
}
