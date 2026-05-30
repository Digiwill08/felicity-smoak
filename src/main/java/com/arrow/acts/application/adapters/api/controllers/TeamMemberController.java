package com.arrow.acts.application.adapters.api.controllers;

import com.arrow.acts.application.adapters.api.request.AgentStatusRequest;
import com.arrow.acts.application.adapters.api.request.TeamMemberRequest;
import com.arrow.acts.application.adapters.api.response.TeamMemberResponse;
import com.arrow.acts.domain.models.team.TeamMember;
import com.arrow.acts.domain.ports.in.TeamMemberUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST: Gestión del equipo Arrow.
 *
 * <p>Expone los endpoints del caso de uso {@link TeamMemberUseCase}.
 * Traduce entre los DTOs de la API y los modelos del dominio.</p>
 */
@RestController
@RequestMapping("/api/team")
public class TeamMemberController {

    private final TeamMemberUseCase teamMemberUseCase;

    public TeamMemberController(TeamMemberUseCase teamMemberUseCase) {
        this.teamMemberUseCase = teamMemberUseCase;
    }

    /**
     * POST /api/team — Registra un nuevo miembro en el equipo Arrow.
     */
    @PostMapping
    public ResponseEntity<Void> registerMember(@Valid @RequestBody TeamMemberRequest request) {
        teamMemberUseCase.registerMember(toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /api/team — Lista todos los miembros del equipo.
     */
    @GetMapping
    public ResponseEntity<List<TeamMemberResponse>> getAllMembers() {
        List<TeamMemberResponse> response = teamMemberUseCase.getAllMembers()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/team/{id} — Obtiene un miembro por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamMemberResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(teamMemberUseCase.getMemberById(id)));
    }

    /**
     * GET /api/team/available — Lista los agentes disponibles para misiones.
     */
    @GetMapping("/available")
    public ResponseEntity<List<TeamMemberResponse>> getAvailableMembers() {
        List<TeamMemberResponse> response = teamMemberUseCase.getAvailableMembers()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/team/{id} — Actualiza los datos de un miembro.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id,
                                              @Valid @RequestBody TeamMemberRequest request) {
        TeamMember member = toModel(request);
        member.setId(id);
        teamMemberUseCase.updateMember(member);
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/team/{id}/status — Actualiza únicamente el estado operativo.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id,
                                              @Valid @RequestBody AgentStatusRequest request) {
        teamMemberUseCase.updateStatus(id, request.getNewStatus());
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/team/{id} — Elimina un miembro del equipo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        teamMemberUseCase.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // ── Mappers privados ───────────────────────────────────────────────────────

    private TeamMember toModel(TeamMemberRequest request) {
        TeamMember member = new TeamMember();
        member.setCodename(request.getCodename());
        member.setRealName(request.getRealName());
        member.setRole(request.getRole());
        member.setCapabilities(request.getCapabilities() != null
                ? request.getCapabilities()
                : new java.util.HashSet<>());
        member.setCurrentStatus(request.getCurrentStatus());
        return member;
    }

    private TeamMemberResponse toResponse(TeamMember member) {
        return new TeamMemberResponse(
                member.getId(),
                member.getCodename(),
                member.getRealName(),
                member.getRole(),
                member.getCapabilities(),
                member.getCurrentStatus()
        );
    }
}
