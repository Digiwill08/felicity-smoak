package com.arrow.acts.domain.ports.in;

import com.arrow.acts.domain.models.enums.AgentStatus;
import com.arrow.acts.domain.models.team.TeamMember;

import java.util.List;

/**
 * Puerto de entrada: operaciones sobre miembros del equipo Arrow.
 *
 * <p>Define los casos de uso disponibles para registrar, consultar
 * y gestionar el estado operativo de los agentes del equipo.</p>
 */
public interface TeamMemberUseCase {

    /**
     * Registra un nuevo miembro en el equipo Arrow.
     *
     * @param member Datos del miembro a registrar
     */
    void registerMember(TeamMember member);

    /**
     * Obtiene todos los miembros del equipo.
     *
     * @return Lista completa del equipo Arrow
     */
    List<TeamMember> getAllMembers();

    /**
     * Obtiene un miembro por su ID.
     *
     * @param id ID del miembro
     * @return El miembro encontrado
     * @throws com.arrow.acts.domain.exceptions.NotFoundException si no existe
     */
    TeamMember getMemberById(Long id);

    /**
     * Obtiene los miembros disponibles para ser asignados a una misión.
     *
     * @return Lista de agentes con estado AVAILABLE
     */
    List<TeamMember> getAvailableMembers();

    /**
     * Actualiza los datos de un miembro del equipo.
     *
     * @param member Datos actualizados (debe tener ID)
     */
    void updateMember(TeamMember member);

    /**
     * Actualiza el estado operativo de un agente.
     *
     * @param memberId ID del agente
     * @param newStatus Nuevo estado a asignar
     */
    void updateStatus(Long memberId, AgentStatus newStatus);

    /**
     * Elimina un miembro del equipo por su ID.
     *
     * @param id ID del miembro a eliminar
     */
    void deleteMember(Long id);
}
