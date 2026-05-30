package com.arrow.acts.application.adapters.api.controllers;

import com.arrow.acts.application.adapters.api.response.ThreatSummaryResponse;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.in.ThreatUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST: Análisis y priorización de amenazas Mirakuru.
 *
 * <p>Proporciona vistas tácticas priorizadas de todos los sujetos
 * activos, ordenados por nivel de urgencia para el equipo Arrow.</p>
 */
@RestController
@RequestMapping("/api/threats")
public class ThreatController {

    private final ThreatUseCase threatUseCase;

    public ThreatController(ThreatUseCase threatUseCase) {
        this.threatUseCase = threatUseCase;
    }

    /**
     * GET /api/threats — Lista todos los sujetos ordenados por prioridad táctica.
     */
    @GetMapping
    public ResponseEntity<List<ThreatSummaryResponse>> getPrioritizedThreats() {
        List<ThreatSummaryResponse> response = threatUseCase.getPrioritizedThreats()
                .stream()
                .map(this::toSummary)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/threats/critical — Lista únicamente amenazas CRITICAL y HIGH.
     */
    @GetMapping("/critical")
    public ResponseEntity<List<ThreatSummaryResponse>> getHighPriorityThreats() {
        List<ThreatSummaryResponse> response = threatUseCase.getHighPriorityThreats()
                .stream()
                .map(this::toSummary)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/threats/{subjectId}/reclassify — Reclasifica la amenaza de un sujeto.
     */
    @PostMapping("/{subjectId}/reclassify")
    public ResponseEntity<Void> reclassify(@PathVariable Long subjectId) {
        threatUseCase.reclassifyThreat(subjectId);
        return ResponseEntity.ok().build();
    }

    // ── Mapper privado ─────────────────────────────────────────────────────────

    private ThreatSummaryResponse toSummary(MirakuruSubject subject) {
        return new ThreatSummaryResponse(
                subject.getId(),
                subject.getAlias(),
                subject.getThreatLevel(),
                subject.getMirakuruConcentration(),
                subject.getLastKnownLocation(),
                subject.getBehavioralStatus() != null
                        ? subject.getBehavioralStatus().name()
                        : "UNKNOWN"
        );
    }
}
