package com.arrow.acts.domain.ports.in;

import com.arrow.acts.domain.exceptions.BusinessException;
import com.arrow.acts.domain.models.mission.Mission;

import java.util.List;

/**
 * Puerto de entrada: operaciones sobre misiones tácticas.
 *
 * <p>Define los casos de uso disponibles para crear, activar,
 * asignar agentes y gestionar el ciclo de vida de las misiones.</p>
 */
public interface MissionUseCase {

    /**
     * Crea una nueva misión en estado PLANNED.
     *
     * @param mission Datos de la misión a crear
     */
    void createMission(Mission mission);

    /**
     * Activa una misión planificada, validando todas las invariantes del dominio.
     *
     * @param missionId ID de la misión a activar
     * @throws BusinessException si no cumple los requisitos para activarse
     */
    void activateMission(Long missionId) throws BusinessException;

    /**
     * Asigna un agente disponible a una misión existente.
     *
     * @param missionId ID de la misión
     * @param agentId ID del agente a asignar
     * @throws BusinessException si el agente no está disponible
     */
    void assignAgent(Long missionId, Long agentId) throws BusinessException;

    /**
     * Completa una misión activa.
     *
     * @param missionId ID de la misión a completar
     */
    void completeMission(Long missionId);

    /**
     * Aborta una misión activa o planificada.
     *
     * @param missionId ID de la misión a abortar
     */
    void abortMission(Long missionId);

    /**
     * Obtiene todas las misiones registradas.
     *
     * @return Lista de todas las misiones
     */
    List<Mission> getAllMissions();

    /**
     * Obtiene una misión por su ID.
     *
     * @param missionId ID de la misión
     * @return La misión encontrada
     */
    Mission getMissionById(Long missionId);
}
