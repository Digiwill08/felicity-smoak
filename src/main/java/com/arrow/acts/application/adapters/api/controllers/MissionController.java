package com.arrow.acts.application.adapters.api.controllers;

import com.arrow.acts.application.adapters.api.request.MissionRequest;
import com.arrow.acts.application.adapters.api.response.MissionResponse;
import com.arrow.acts.domain.models.mission.Mission;
import com.arrow.acts.domain.ports.in.MissionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST: Gestión de misiones tácticas del equipo Arrow.
 *
 * <p>Expone los endpoints del caso de uso MissionUseCase.
 * Traduce entre los DTOs de la API y los modelos del dominio.</p>
 */
@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionUseCase missionUseCase;

    public MissionController(MissionUseCase missionUseCase) {
        this.missionUseCase = missionUseCase;
    }

    /**
     * POST /api/missions — Crea una nueva misión en estado PLANNED.
     */
    @PostMapping
    public ResponseEntity<Void> createMission(@Valid @RequestBody MissionRequest request) {
        missionUseCase.createMission(toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /api/missions — Lista todas las misiones registradas.
     */
    @GetMapping
    public ResponseEntity<List<MissionResponse>> getAllMissions() {
        List<MissionResponse> response = missionUseCase.getAllMissions()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/missions/{id} — Obtiene una misión por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MissionResponse> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(missionUseCase.getMissionById(id)));
    }

    /**
     * POST /api/missions/{id}/activate — Activa una misión planificada.
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateMission(@PathVariable Long id) {
        missionUseCase.activateMission(id);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/missions/{id}/agents/{agentId} — Asigna un agente a la misión.
     */
    @PostMapping("/{id}/agents/{agentId}")
    public ResponseEntity<Void> assignAgent(@PathVariable Long id,
                                             @PathVariable Long agentId) {
        missionUseCase.assignAgent(id, agentId);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/missions/{id}/complete — Completa una misión activa.
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeMission(@PathVariable Long id) {
        missionUseCase.completeMission(id);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/missions/{id}/abort — Aborta una misión activa o planificada.
     */
    @PostMapping("/{id}/abort")
    public ResponseEntity<Void> abortMission(@PathVariable Long id) {
        missionUseCase.abortMission(id);
        return ResponseEntity.ok().build();
    }

    // ── Mappers privados ───────────────────────────────────────────────────────

    private Mission toModel(MissionRequest request) {
        Mission mission = new Mission();
        mission.setObjective(request.getObjective());
        mission.setTargetSubjectIds(request.getTargetSubjectIds());
        mission.setPriority(request.getPriority());
        mission.setOperationalZone(request.getOperationalZone());
        mission.setScheduledStart(request.getScheduledStart());
        mission.setNotes(request.getNotes());
        return mission;
    }

    private MissionResponse toResponse(Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getObjective(),
                mission.getTargetSubjectIds(),
                mission.getAssignedAgentIds(),
                mission.getStatus(),
                mission.getPriority(),
                mission.getOperationalZone(),
                mission.getScheduledStart(),
                mission.getActualStart(),
                mission.getEndTime(),
                mission.getNotes()
        );
    }
}
