package com.arrow.acts.application.usecases;

import com.arrow.acts.domain.exceptions.DuplicateSubjectException;
import com.arrow.acts.domain.exceptions.SubjectNotFoundException;
import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.enums.ThreatLevel;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.out.AlertNotificationPort;
import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import com.arrow.acts.domain.services.ThreatClassificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para SubjectUseCaseImpl.
 *
 * <p>Valida la orquestación del caso de uso de gestión de sujetos.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SubjectUseCaseImpl — Gestión de sujetos Mirakuru")
class SubjectUseCaseImplTest {

    @Mock
    private MirakuruSubjectPort subjectPort;

    @Mock
    private ThreatClassificationService classificationService;

    @Mock
    private AlertNotificationPort alertPort;

    @InjectMocks
    private SubjectUseCaseImpl useCase;

    private MirakuruSubject subject;

    @BeforeEach
    void setUp() {
        subject = new MirakuruSubject();
        subject.setId(1L);
        subject.setAlias("Slade Wilson");
        subject.setMirakuruConcentration(9.5);
        subject.setBehavioralStatus(BehavioralStatus.BERSERK);
    }

    // ── registerSubject ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar sujeto nuevo → se persiste y se clasifica")
    void registerSubject_NewSubject_SavesAndClassifies() {
        when(subjectPort.existsByAlias("Slade Wilson")).thenReturn(false);

        useCase.registerSubject(subject);

        verify(classificationService).classify(subject);
        verify(subjectPort).save(subject);
        assertThat(subject.getRegisteredAt()).isNotNull();
    }

    @Test
    @DisplayName("Registrar sujeto con alias duplicado → DuplicateSubjectException")
    void registerSubject_DuplicateAlias_ThrowsException() {
        when(subjectPort.existsByAlias("Slade Wilson")).thenReturn(true);

        assertThatThrownBy(() -> useCase.registerSubject(subject))
                .isInstanceOf(DuplicateSubjectException.class)
                .hasMessageContaining("Slade Wilson");

        verify(subjectPort, never()).save(any());
    }

    @Test
    @DisplayName("Registrar sujeto CRITICAL → dispara alerta")
    void registerSubject_CriticalThreat_FiresAlert() {
        when(subjectPort.existsByAlias("Slade Wilson")).thenReturn(false);
        doAnswer(inv -> {
            subject.setThreatLevel(ThreatLevel.CRITICAL);
            return null;
        }).when(classificationService).classify(subject);

        useCase.registerSubject(subject);

        verify(alertPort).notifyCriticalThreat(subject);
    }

    @Test
    @DisplayName("Registrar sujeto no CRITICAL → no dispara alerta")
    void registerSubject_NonCriticalThreat_NoAlert() {
        when(subjectPort.existsByAlias("Slade Wilson")).thenReturn(false);
        doAnswer(inv -> {
            subject.setThreatLevel(ThreatLevel.MEDIUM);
            return null;
        }).when(classificationService).classify(subject);

        useCase.registerSubject(subject);

        verify(alertPort, never()).notifyCriticalThreat(any());
    }

    // ── getSubjectById ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Obtener sujeto por ID existente → retorna el sujeto")
    void getSubjectById_Exists_ReturnsSubject() {
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        MirakuruSubject result = useCase.getSubjectById(1L);

        assertThat(result.getAlias()).isEqualTo("Slade Wilson");
    }

    @Test
    @DisplayName("Obtener sujeto por ID inexistente → SubjectNotFoundException")
    void getSubjectById_NotFound_ThrowsException() {
        when(subjectPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getSubjectById(99L))
                .isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── getAllSubjects ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllSubjects() retorna todos los sujetos del puerto")
    void getAllSubjects_ReturnsList() {
        when(subjectPort.findAll()).thenReturn(List.of(subject));

        List<MirakuruSubject> result = useCase.getAllSubjects();

        assertThat(result).hasSize(1);
        verify(subjectPort).findAll();
    }

    // ── deleteSubject ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Eliminar sujeto existente → se elimina del repositorio")
    void deleteSubject_Exists_DeletesSuccessfully() {
        when(subjectPort.findById(1L)).thenReturn(Optional.of(subject));

        useCase.deleteSubject(1L);

        verify(subjectPort).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar sujeto inexistente → SubjectNotFoundException")
    void deleteSubject_NotFound_ThrowsException() {
        when(subjectPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.deleteSubject(99L))
                .isInstanceOf(SubjectNotFoundException.class);

        verify(subjectPort, never()).deleteById(any());
    }
}
