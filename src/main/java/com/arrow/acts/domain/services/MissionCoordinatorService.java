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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Servicio de dominio: Coordinación y validación de misiones tácticas.
 *
 * <p>Responsabilidad única: validar las reglas de negocio para activar
 * misiones y coordinar la asignación de agentes, garantizando que se
 * cumplan las invariantes del dominio antes de cualquier cambio de estado.</p>
 *
 * <p>Reglas de negocio:
 * <ul>
 *   <li>Una misión no puede activarse sin objetivos definidos.</li>
 *   <li>Una misión CRITICAL requiere al menos un agente FIELD_AGENT disponible.</li>
 *   <li>Un agente solo puede asignarse si su estado es AVAILABLE.</li>
 * </ul>
 * </p>
 *
 * <p>Principio SOLID aplicado: DIP — depende de TeamMemberPort (interfaz), no de implementación.</p>
 */
@Service
public class MissionCoordinatorService {

    private final TeamMemberPort teamMemberPort;

    public MissionCoordinatorService(TeamMemberPort teamMemberPort) {
        this.teamMemberPort = teamMemberPort;
    }

    /**
     * Activa una misión planificada, validando todas las invariantes del dominio.
     *
     * @param mission La misión a activar
     * @throws MissionWithoutTargetsException si la misión no tiene objetivos
     * @throws InsufficientAgentsException si es CRITICAL y no hay agentes de campo
     */
    public void activateMission(Mission mission) {
        validateTargets(mission);
        if (mission.getPriority() == MissionPriority.CRITICAL) {
            validateFieldAgentAssigned(mission);
        }
        mission.setStatus(MissionStatus.ACTIVE);
        mission.setActualStart(Instant.now());
    }

    /**
     * Completa una misión activa.
     *
     * @param mission La misión a completar
     */
    public void completeMission(Mission mission) {
        mission.setStatus(MissionStatus.COMPLETED);
        mission.setEndTime(Instant.now());
    }

    /**
     * Aborta una misión activa o planificada.
     *
     * @param mission La misión a abortar
     */
    public void abortMission(Mission mission) {
        mission.setStatus(MissionStatus.ABORTED);
        mission.setEndTime(Instant.now());
    }

    /**
     * Asigna un agente disponible a una misión.
     *
     * @param mission La misión objetivo
     * @param agent El agente a asignar
     * @throws BusinessException si el agente no está disponible
     */
    public void assignAgent(Mission mission, TeamMember agent) {
        if (agent.getCurrentStatus() != AgentStatus.AVAILABLE) {
            throw new com.arrow.acts.domain.exceptions.BusinessException(
                String.format("El agente '%s' no está disponible (estado: %s).",
                    agent.getCodename(), agent.getCurrentStatus())
            );
        }
        mission.getAssignedAgentIds().add(agent.getId());
    }

    // ── Validaciones privadas ──────────────────────────────────────────────────

    private void validateTargets(Mission mission) {
        if (mission.getTargetSubjectIds() == null || mission.getTargetSubjectIds().isEmpty()) {
            throw new MissionWithoutTargetsException(mission.getId());
        }
    }

    private void validateFieldAgentAssigned(Mission mission) {
        List<Long> agentIds = mission.getAssignedAgentIds();
        if (agentIds == null || agentIds.isEmpty()) {
            throw new InsufficientAgentsException(mission.getId());
        }
        boolean hasFieldAgent = agentIds.stream()
                .map(teamMemberPort::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .anyMatch(member -> member.getRole() == TeamRole.FIELD_AGENT);

        if (!hasFieldAgent) {
            throw new InsufficientAgentsException(mission.getId());
        }
    }
}
