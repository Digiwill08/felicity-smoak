package com.arrow.acts.domain.services;

import com.arrow.acts.domain.exceptions.SubjectNotFoundException;
import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.IntelligenceConfidenceLevel;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.intelligence.IntelligenceReport;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para IntelligenceAggregatorService.
 *
 * <p>Valida la consolidación de reportes de inteligencia
 * y la actualización del estado de los sujetos.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IntelligenceAggregatorService — Agregación de inteligencia externa")
class IntelligenceAggregatorServiceTest {

    @Mock
    private MirakuruSubjectPort subjectPort;

    @Mock
    private ThreatClassificationService classificationService;

    @InjectMocks
    private IntelligenceAggregatorService service;

    private MirakuruSubject subject;

    @BeforeEach
    void setUp() {
        subject = new MirakuruSubject();
        subject.setId(1L);
        subject.setAlias("Slade Wilson");
        subject.setMirakuruConcentration(5.0);
        subject.setBehavioralStatus(BehavioralStatus.AGGRESSIVE);
        subject.setLastKnownLocation("The Glades");
    }

    @Test
    @DisplayName("processReports() con sujeto inexistente → SubjectNotFoundException")
    void processReports_SubjectNotFound_ThrowsException() {
        when(subjectPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.processReports(99L, List.of()))
                .isInstanceOf(SubjectNotFoundException.class);
    }

    @Test
    @DisplayName("Reporte CONFIRMED sobreescribe la concentración directamente")
    void processReports_ConfirmedReport_OverwritesConcentration() {
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        IntelligenceReport confirmed = buildReport(
                IntelligenceConfidenceLevel.CONFIRMED, 9.0, "The Glades - Sector 3");
        service.processReports(1L, List.of(confirmed));

        ArgumentCaptor<MirakuruSubject> captor = ArgumentCaptor.forClass(MirakuruSubject.class);
        verify(subjectPort).save(captor.capture());

        assertThat(captor.getValue().getMirakuruConcentration()).isEqualTo(9.0);
        assertThat(captor.getValue().getLastKnownLocation()).isEqualTo("The Glades - Sector 3");
    }

    @Test
    @DisplayName("Reporte HIGH promedia la concentración con el valor actual")
    void processReports_HighReport_AveragesConcentration() {
        subject.setMirakuruConcentration(4.0);
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        IntelligenceReport high = buildReport(IntelligenceConfidenceLevel.HIGH, 8.0, "Harbor District");
        service.processReports(1L, List.of(high));

        ArgumentCaptor<MirakuruSubject> captor = ArgumentCaptor.forClass(MirakuruSubject.class);
        verify(subjectPort).save(captor.capture());

        // Promedio: (4.0 + 8.0) / 2 = 6.0
        assertThat(captor.getValue().getMirakuruConcentration()).isEqualTo(6.0);
    }

    @Test
    @DisplayName("Reporte UNVERIFIED es ignorado (no modifica el sujeto)")
    void processReports_UnverifiedReport_IsIgnored() {
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        IntelligenceReport unverified = buildReport(
                IntelligenceConfidenceLevel.UNVERIFIED, 9.9, "Unknown Location");
        service.processReports(1L, List.of(unverified));

        ArgumentCaptor<MirakuruSubject> captor = ArgumentCaptor.forClass(MirakuruSubject.class);
        verify(subjectPort).save(captor.capture());

        // La concentración no debe cambiar
        assertThat(captor.getValue().getMirakuruConcentration()).isEqualTo(5.0);
        assertThat(captor.getValue().getLastKnownLocation()).isEqualTo("The Glades");
    }

    @Test
    @DisplayName("processReports() llama a classificationService.classify() al final")
    void processReports_AlwaysReclassifiesSubject() {
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        service.processReports(1L, List.of());

        verify(classificationService).classify(subject);
    }

    @Test
    @DisplayName("processReports() siempre persiste el sujeto actualizado")
    void processReports_AlwaysSavesSubject() {
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        service.processReports(1L, List.of());

        verify(subjectPort, times(1)).save(any(MirakuruSubject.class));
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private IntelligenceReport buildReport(IntelligenceConfidenceLevel level,
                                            double concentration,
                                            String location) {
        IntelligenceReport report = new IntelligenceReport();
        report.setSubjectId(1L);
        report.setSource("ARGUS-TEST");
        report.setObservation("Sujeto observado en zona táctica.");
        report.setLocation(location);
        report.setEstimatedConcentration(concentration);
        report.setConfidenceLevel(level);
        report.setReportedAt(Instant.now());
        return report;
    }
}
