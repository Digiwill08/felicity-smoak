package com.arrow.acts.domain.services;

import com.arrow.acts.domain.exceptions.InvalidMirakuruConcentrationException;
import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas unitarias para ThreatClassificationService.
 *
 * <p>Valida todas las reglas de clasificación del dominio:
 * concentración vs. estado conductual → ThreatLevel.</p>
 */
@DisplayName("ThreatClassificationService — Clasificación de amenazas Mirakuru")
class ThreatClassificationServiceTest {

    private ThreatClassificationService service;

    @BeforeEach
    void setUp() {
        service = new ThreatClassificationService();
    }

    // ── Reglas de clasificación ────────────────────────────────────────────────

    @Test
    @DisplayName("BERSERK + concentración > 8.0 → CRITICAL")
    void classify_BerserkHighConcentration_ReturnsCritical() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.BERSERK, 9.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.CRITICAL);
    }

    @Test
    @DisplayName("BERSERK + concentración exactamente 8.0 → HIGH (no supera el umbral)")
    void classify_BerserkAtThreshold_ReturnsHigh() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.BERSERK, 8.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
    }

    @Test
    @DisplayName("BERSERK + concentración baja → HIGH")
    void classify_BerserkLowConcentration_ReturnsHigh() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.BERSERK, 2.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
    }

    @Test
    @DisplayName("AGGRESSIVE + concentración > 5.0 → HIGH")
    void classify_AggressiveHighConcentration_ReturnsHigh() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.AGGRESSIVE, 7.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
    }

    @Test
    @DisplayName("AGGRESSIVE + concentración ≤ 5.0 → MEDIUM")
    void classify_AggressiveLowConcentration_ReturnsMedium() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.AGGRESSIVE, 3.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
    }

    @Test
    @DisplayName("UNKNOWN (sin info) → MEDIUM por precaución")
    void classify_UnknownStatus_ReturnsMedium() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.UNKNOWN, 1.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
    }

    @Test
    @DisplayName("STABLE + concentración > 3.0 → MEDIUM")
    void classify_StableElevatedConcentration_ReturnsMedium() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.STABLE, 5.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
    }

    @Test
    @DisplayName("STABLE + concentración baja → LOW")
    void classify_StableLowConcentration_ReturnsLow() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.STABLE, 1.0);
        service.classify(subject);
        assertThat(subject.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    // ── Validación de concentración ────────────────────────────────────────────

    @Test
    @DisplayName("Concentración negativa → InvalidMirakuruConcentrationException")
    void classify_NegativeConcentration_ThrowsException() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.STABLE, -1.0);
        assertThatThrownBy(() -> service.classify(subject))
                .isInstanceOf(InvalidMirakuruConcentrationException.class)
                .hasMessageContaining("-1");
    }

    @Test
    @DisplayName("Concentración > 10.0 → InvalidMirakuruConcentrationException")
    void classify_ConcentrationAboveMax_ThrowsException() {
        MirakuruSubject subject = subjectWith(BehavioralStatus.STABLE, 10.1);
        assertThatThrownBy(() -> service.classify(subject))
                .isInstanceOf(InvalidMirakuruConcentrationException.class)
                .hasMessageContaining("10.10");
    }

    @Test
    @DisplayName("Concentración en límites válidos (0.0 y 10.0) no lanza excepción")
    void classify_BoundaryConcentrations_DoNotThrow() {
        assertThatCode(() -> service.classify(subjectWith(BehavioralStatus.STABLE, 0.0))).doesNotThrowAnyException();
        assertThatCode(() -> service.classify(subjectWith(BehavioralStatus.STABLE, 10.0))).doesNotThrowAnyException();
    }

    // ── Método calculate (sin efecto sobre el sujeto) ─────────────────────────

    @ParameterizedTest(name = "Estado={0}, Concentración={1} → {2}")
    @CsvSource({
        "BERSERK,  9.5, CRITICAL",
        "BERSERK,  3.0, HIGH",
        "AGGRESSIVE, 7.0, HIGH",
        "AGGRESSIVE, 2.0, MEDIUM",
        "UNKNOWN,  0.5, MEDIUM",
        "STABLE,   5.0, MEDIUM",
        "STABLE,   1.0, LOW"
    })
    @DisplayName("calculate() retorna el nivel correcto para todas las combinaciones")
    void calculate_AllCombinations_ReturnExpectedLevel(
            BehavioralStatus status, double concentration, ThreatLevel expected) {
        assertThat(service.calculate(status, concentration)).isEqualTo(expected);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private MirakuruSubject subjectWith(BehavioralStatus status, double concentration) {
        MirakuruSubject subject = new MirakuruSubject();
        subject.setBehavioralStatus(status);
        subject.setMirakuruConcentration(concentration);
        return subject;
    }
}
