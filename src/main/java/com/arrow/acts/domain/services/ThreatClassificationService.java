package com.arrow.acts.domain.services;

import com.arrow.acts.domain.exceptions.InvalidMirakuruConcentrationException;
import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio: Clasificación de amenazas Mirakuru.
 *
 * <p>Responsabilidad única: calcular el {@link ThreatLevel} de un
 * {@link MirakuruSubject} basado en su concentración del suero y su
 * estado conductual, aplicando las reglas de clasificación del equipo Arrow.</p>
 *
 * <p>Reglas de clasificación (orden de prioridad):
 * <ul>
 *   <li>BERSERK + concentración &gt; 8.0 → CRITICAL</li>
 *   <li>BERSERK (cualquier concentración) → HIGH</li>
 *   <li>AGGRESSIVE + concentración &gt; 5.0 → HIGH</li>
 *   <li>AGGRESSIVE (concentración ≤ 5.0) → MEDIUM</li>
 *   <li>STABLE + concentración &gt; 3.0 → MEDIUM</li>
 *   <li>UNKNOWN → MEDIUM (se asume riesgo por falta de información)</li>
 *   <li>Resto → LOW</li>
 * </ul>
 * </p>
 *
 * <p>Principio SOLID aplicado: SRP — esta clase solo clasifica amenazas.</p>
 */
@Service
public class ThreatClassificationService {

    private static final double MAX_CONCENTRATION = 10.0;
    private static final double MIN_CONCENTRATION = 0.0;
    private static final double CRITICAL_THRESHOLD = 8.0;
    private static final double HIGH_THRESHOLD = 5.0;
    private static final double MEDIUM_THRESHOLD = 3.0;

    /**
     * Calcula y asigna el nivel de amenaza al sujeto dado.
     *
     * @param subject El sujeto Mirakuru a clasificar
     * @throws InvalidMirakuruConcentrationException si la concentración está fuera del rango válido
     */
    public void classify(MirakuruSubject subject) {
        validateConcentration(subject.getMirakuruConcentration());
        ThreatLevel level = calculateThreatLevel(
                subject.getBehavioralStatus(),
                subject.getMirakuruConcentration()
        );
        subject.setThreatLevel(level);
    }

    /**
     * Calcula el nivel de amenaza sin modificar el sujeto (útil para previews).
     *
     * @param status Estado conductual del sujeto
     * @param concentration Concentración de Mirakuru
     * @return El ThreatLevel calculado
     */
    public ThreatLevel calculate(BehavioralStatus status, double concentration) {
        validateConcentration(concentration);
        return calculateThreatLevel(status, concentration);
    }

    private ThreatLevel calculateThreatLevel(BehavioralStatus status, double concentration) {
        if (status == BehavioralStatus.BERSERK && concentration > CRITICAL_THRESHOLD) {
            return ThreatLevel.CRITICAL;
        }
        if (status == BehavioralStatus.BERSERK) {
            return ThreatLevel.HIGH;
        }
        if (status == BehavioralStatus.AGGRESSIVE && concentration > HIGH_THRESHOLD) {
            return ThreatLevel.HIGH;
        }
        if (status == BehavioralStatus.AGGRESSIVE) {
            return ThreatLevel.MEDIUM;
        }
        if (status == BehavioralStatus.UNKNOWN) {
            return ThreatLevel.MEDIUM;
        }
        if (status == BehavioralStatus.STABLE && concentration > MEDIUM_THRESHOLD) {
            return ThreatLevel.MEDIUM;
        }
        return ThreatLevel.LOW;
    }

    private void validateConcentration(double concentration) {
        if (concentration < MIN_CONCENTRATION || concentration > MAX_CONCENTRATION) {
            throw new InvalidMirakuruConcentrationException(concentration);
        }
    }
}
