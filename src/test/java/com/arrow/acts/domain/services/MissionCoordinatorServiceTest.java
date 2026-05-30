package com.arrow.acts.domain.services;

import com.arrow.acts.domain.exceptions.InsufficientAgentsException;
import com.arrow.acts.domain.exceptions.MissionWithoutTargetsException;
import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.enums.MissionPriority;
import com.arrow.acts.domain.models.enums.MissionStatus;
import com.arrow.acts.domain.models.enums.TeamRole;
import com.arrow.acts.domain.models.mission.Mission;
import com.arrow.acts.domain.models.team.TeamMember;
import com.arrow.acts.domain.ports.out.TeamMemberPort;
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
 * Pruebas unitarias para MissionCoordinatorService.
 *
 * <p>Valida las invariantes de dominio para activación de misiones.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MissionCoordinatorService — Coordinación de misiones tácticas")
class MissionCoordinatorServiceTest {

    @Mock
    private TeamMemberPort teamMemberPort;

    @InjectMocks
    private MissionCoordinatorService service;

    private Mission mission;
    private TeamMember fieldAgent;
    private TeamMember analyst;

    @BeforeEach
    void setUp() {
        mission = new Mission();
        mission.setId(1L);
        mission.setPriority(MissionPriority.HIGH);
        mission.setTargetSubjectIds(List.of(10L, 11L));

        fieldAgent = new TeamMember();
        fieldAgent.setId(1L);
        fieldAgent.setCodename("Arrow");
        fieldAgent.setRole(TeamRole.FIELD_AGENT);
        fieldAgent.setCurrentStatus(AgentStatus.AVAILABLE);

        analyst = new TeamMember();
        analyst.setId(2L);
        analyst.setCodename("Felicity");
        analyst.setRole(TeamRole.TECH_SUPPORT);
        analyst.setCurrentStatus(AgentStatus.AVAILABLE);
    }

    // ── activateMission ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Activar misión con objetivos → estado ACTIVE y actualiza actualStart")
    void activateMission_WithTargets_SetsActiveStatus() {
        service.activateMission(mission);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.ACTIVE);
        assertThat(mission.getActualStart()).isNotNull();
    }

    @Test
    @DisplayName("Activar misión sin objetivos → MissionWithoutTargetsException")
    void activateMission_WithoutTargets_ThrowsException() {
        mission.setTargetSubjectIds(List.of());

        assertThatThrownBy(() -> service.activateMission(mission))
                .isInstanceOf(MissionWithoutTargetsException.class)
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Activar misión CRITICAL con FIELD_AGENT → exitoso")
    void activateMission_CriticalWithFieldAgent_Succeeds() {
        mission.setPriority(MissionPriority.CRITICAL);
        mission.setAssignedAgentIds(List.of(1L));
        when(teamMemberPort.findById(1L)).thenReturn(Optional.of(fieldAgent));

        assertThatCode(() -> service.activateMission(mission)).doesNotThrowAnyException();
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.ACTIVE);
    }

    @Test
    @DisplayName("Activar misión CRITICAL sin agentes → InsufficientAgentsException")
    void activateMission_CriticalWithoutAgents_ThrowsException() {
        mission.setPriority(MissionPriority.CRITICAL);
        mission.setAssignedAgentIds(List.of());

        assertThatThrownBy(() -> service.activateMission(mission))
                .isInstanceOf(InsufficientAgentsException.class)
                .hasMessageContaining("FIELD_AGENT");
    }

    @Test
    @DisplayName("Activar misión CRITICAL solo con TECH_SUPPORT → InsufficientAgentsException")
    void activateMission_CriticalWithTechSupportOnly_ThrowsException() {
        mission.setPriority(MissionPriority.CRITICAL);
        mission.setAssignedAgentIds(List.of(2L));
        when(teamMemberPort.findById(2L)).thenReturn(Optional.of(analyst));

        assertThatThrownBy(() -> service.activateMission(mission))
                .isInstanceOf(InsufficientAgentsException.class);
    }

    // ── completeMission ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Completar misión → estado COMPLETED y registra endTime")
    void completeMission_SetsCompletedStatusAndEndTime() {
        service.completeMission(mission);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        assertThat(mission.getEndTime()).isNotNull();
    }

    // ── abortMission ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Abortar misión → estado ABORTED y registra endTime")
    void abortMission_SetsAbortedStatusAndEndTime() {
        service.abortMission(mission);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.ABORTED);
        assertThat(mission.getEndTime()).isNotNull();
    }

    // ── assignAgent ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Asignar agente AVAILABLE → se agrega a la misión")
    void assignAgent_AvailableAgent_AddsToMission() {
        service.assignAgent(mission, fieldAgent);

        assertThat(mission.getAssignedAgentIds()).contains(1L);
    }

    @Test
    @DisplayName("Asignar agente ON_MISSION → BusinessException")
    void assignAgent_UnavailableAgent_ThrowsException() {
        fieldAgent.setCurrentStatus(AgentStatus.ON_MISSION);

        assertThatThrownBy(() -> service.assignAgent(mission, fieldAgent))
                .isInstanceOf(com.arrow.acts.domain.exceptions.BusinessException.class)
                .hasMessageContaining("Arrow");
    }
}
