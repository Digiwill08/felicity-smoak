package com.arrow.acts.application.usecases;

import com.arrow.acts.domain.exceptions.TeamMemberNotFoundException;
import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.team.TeamMember;
import com.arrow.acts.domain.ports.in.TeamMemberUseCase;
import com.arrow.acts.domain.ports.out.TeamMemberPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del caso de uso de gestión de miembros del equipo Arrow.
 *
 * <p>Orquesta las operaciones CRUD sobre el equipo Arrow a través del
 * puerto {@link TeamMemberPort}, garantizando las invariantes del dominio.</p>
 */
@Service
public class TeamMemberUseCaseImpl implements TeamMemberUseCase {

    private final TeamMemberPort teamMemberPort;

    public TeamMemberUseCaseImpl(TeamMemberPort teamMemberPort) {
        this.teamMemberPort = teamMemberPort;
    }

    @Override
    public void registerMember(TeamMember member) {
        if (member.getCurrentStatus() == null) {
            member.setCurrentStatus(AgentStatus.AVAILABLE);
        }
        teamMemberPort.save(member);
    }

    @Override
    public List<TeamMember> getAllMembers() {
        return teamMemberPort.findAll();
    }

    @Override
    public TeamMember getMemberById(Long id) {
        return teamMemberPort.findById(id)
                .orElseThrow(() -> new TeamMemberNotFoundException(id));
    }

    @Override
    public List<TeamMember> getAvailableMembers() {
        return teamMemberPort.findByStatus(AgentStatus.AVAILABLE);
    }

    @Override
    public void updateMember(TeamMember member) {
        teamMemberPort.findById(member.getId())
                .orElseThrow(() -> new TeamMemberNotFoundException(member.getId()));
        teamMemberPort.save(member);
    }

    @Override
    public void updateStatus(Long memberId, AgentStatus newStatus) {
        TeamMember member = teamMemberPort.findById(memberId)
                .orElseThrow(() -> new TeamMemberNotFoundException(memberId));
        member.setCurrentStatus(newStatus);
        teamMemberPort.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        teamMemberPort.findById(id)
                .orElseThrow(() -> new TeamMemberNotFoundException(id));
        teamMemberPort.deleteById(id);
    }
}
