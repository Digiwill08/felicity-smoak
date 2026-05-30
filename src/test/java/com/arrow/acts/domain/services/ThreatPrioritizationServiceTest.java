package com.arrow.acts.domain.services;

import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas unitarias para ThreatPrioritizationService.
 *
 * <p>Valida el ordenamiento táctico de amenazas activas.</p>
 */
@DisplayName("ThreatPrioritizationService — Priorización táctica de amenazas")
class ThreatPrioritizationServiceTest {

    private ThreatPrioritizationService service;

    @BeforeEach
    void setUp() {
        service = new ThreatPrioritizationService();
    }

    @Test
    @DisplayName("prioritize() ordena de mayor a menor ThreatLevel")
    void prioritize_OrdersByThreatLevelDescending() {
        List<MirakuruSubject> subjects = List.of(
                buildSubject(1L, ThreatLevel.LOW,      1.0),
                buildSubject(2L, ThreatLevel.CRITICAL, 9.5),
                buildSubject(3L, ThreatLevel.MEDIUM,   3.0),
                buildSubject(4L, ThreatLevel.HIGH,     7.0)
        );

        List<MirakuruSubject> result = service.prioritize(subjects);

        assertThat(result).extracting(MirakuruSubject::getThreatLevel)
                .containsExactly(
                        ThreatLevel.CRITICAL,
                        ThreatLevel.HIGH,
                        ThreatLevel.MEDIUM,
                        ThreatLevel.LOW
                );
    }

    @Test
    @DisplayName("prioritize() con igual ThreatLevel, mayor concentración primero")
    void prioritize_SameThreatLevel_OrdersByConcentrationDescending() {
        List<MirakuruSubject> subjects = List.of(
                buildSubject(1L, ThreatLevel.HIGH, 5.0),
                buildSubject(2L, ThreatLevel.HIGH, 9.0),
                buildSubject(3L, ThreatLevel.HIGH, 6.5)
        );

        List<MirakuruSubject> result = service.prioritize(subjects);

        assertThat(result).extracting(MirakuruSubject::getMirakuruConcentration)
                .containsExactly(9.0, 6.5, 5.0);
    }

    @Test
    @DisplayName("prioritize() con lista vacía retorna lista vacía")
    void prioritize_EmptyList_ReturnsEmpty() {
        assertThat(service.prioritize(List.of())).isEmpty();
    }

    @Test
    @DisplayName("getHighPriorityThreats() filtra solo CRITICAL y HIGH")
    void getHighPriorityThreats_ReturnsOnlyCriticalAndHigh() {
        List<MirakuruSubject> subjects = List.of(
                buildSubject(1L, ThreatLevel.LOW,      1.0),
                buildSubject(2L, ThreatLevel.CRITICAL, 9.5),
                buildSubject(3L, ThreatLevel.MEDIUM,   3.0),
                buildSubject(4L, ThreatLevel.HIGH,     7.0)
        );

        List<MirakuruSubject> result = service.getHighPriorityThreats(subjects);

        assertThat(result).extracting(MirakuruSubject::getThreatLevel)
                .containsExactly(ThreatLevel.CRITICAL, ThreatLevel.HIGH);
    }

    @Test
    @DisplayName("getHighPriorityThreats() con solo amenazas bajas retorna lista vacía")
    void getHighPriorityThreats_NoHighThreats_ReturnsEmpty() {
        List<MirakuruSubject> subjects = List.of(
                buildSubject(1L, ThreatLevel.LOW,    1.0),
                buildSubject(2L, ThreatLevel.MEDIUM, 3.0)
        );

        assertThat(service.getHighPriorityThreats(subjects)).isEmpty();
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private MirakuruSubject buildSubject(Long id, ThreatLevel level, double concentration) {
        MirakuruSubject s = new MirakuruSubject();
        s.setId(id);
        s.setThreatLevel(level);
        s.setMirakuruConcentration(concentration);
        s.setRegisteredAt(Instant.now());
        return s;
    }
}
