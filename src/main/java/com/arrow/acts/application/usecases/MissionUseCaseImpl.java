package com.arrow.acts.application.usecases;

import com.arrow.acts.domain.exceptions.NotFoundException;
import com.arrow.acts.domain.models.enums.MissionStatus;
import com.arrow.acts.domain.models.mission.Mission;
import com.arrow.acts.domain.models.team.TeamMember;
import com.arrow.acts.domain.ports.in.MissionUseCase;
import com.arrow.acts.domain.ports.out.AlertNotificationPort;
import com.arrow.acts.domain.ports.out.MissionPort;
import com.arrow.acts.domain.ports.out.TeamMemberPort;
import com.arrow.acts.domain.services.MissionCoordinatorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del caso de uso de gestión de misiones tácticas.
 *
 * <p>Orquesta el servicio MissionCoordinatorService con los puertos
 * de persistencia y notificación, garantizando la integridad del dominio.</p>
 */
@Service
public class MissionUseCaseImpl implements MissionUseCase {

    private final MissionPort missionPort;
    private final TeamMemberPort teamMemberPort;
    private final MissionCoordinatorService coordinatorService;
    private final AlertNotificationPort alertPort;

    public MissionUseCaseImpl(MissionPort missionPort,
                               TeamMemberPort teamMemberPort,
                               MissionCoordinatorService coordinatorService,
                               AlertNotificationPort alertPort) {
        this.missionPort = missionPort;
        this.teamMemberPort = teamMemberPort;
        this.coordinatorService = coordinatorService;
        this.alertPort = alertPort;
    }

    @Override
    public void createMission(Mission mission) {
        mission.setStatus(MissionStatus.PLANNED);
        missionPort.save(mission);
    }

    @Override
    public void activateMission(Long missionId) {
        Mission mission = getMissionById(missionId);
        coordinatorService.activateMission(mission);
        missionPort.save(mission);
        alertPort.notifyMissionActivated(mission);
    }

    @Override
    public void assignAgent(Long missionId, Long agentId) {
        Mission mission = getMissionById(missionId);
        TeamMember agent = teamMemberPort.findById(agentId)
                .orElseThrow(() -> new NotFoundException(
                    "Agente con ID [" + agentId + "] no encontrado."));
        coordinatorService.assignAgent(mission, agent);
        missionPort.save(mission);
    }

    @Override
    public void completeMission(Long missionId) {
        Mission mission = getMissionById(missionId);
        coordinatorService.completeMission(mission);
        missionPort.save(mission);
        alertPort.notifyMissionEnded(mission);
    }

    @Override
    public void abortMission(Long missionId) {
        Mission mission = getMissionById(missionId);
        coordinatorService.abortMission(mission);
        missionPort.save(mission);
        alertPort.notifyMissionEnded(mission);
    }

    @Override
    public List<Mission> getAllMissions() {
        return missionPort.findAll();
    }

    @Override
    public Mission getMissionById(Long missionId) {
        return missionPort.findById(missionId)
                .orElseThrow(() -> new NotFoundException(
                    "Misión con ID [" + missionId + "] no encontrada."));
    }
}
