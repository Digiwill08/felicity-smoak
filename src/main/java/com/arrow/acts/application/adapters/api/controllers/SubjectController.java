package com.arrow.acts.application.adapters.api.controllers;

import com.arrow.acts.application.adapters.api.request.MirakuruSubjectRequest;
import com.arrow.acts.application.adapters.api.response.MirakuruSubjectResponse;
import com.arrow.acts.domain.models.enums.BehavioralStatus;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.in.SubjectUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST: Gestión de sujetos Mirakuru.
 *
 * <p>Expone los endpoints del caso de uso SubjectUseCase.
 * Traduce entre los DTOs de la API (Request/Response) y los modelos del dominio.</p>
 */
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectUseCase subjectUseCase;

    public SubjectController(SubjectUseCase subjectUseCase) {
        this.subjectUseCase = subjectUseCase;
    }

    /**
     * POST /api/subjects — Registra un nuevo sujeto Mirakuru.
     */
    @PostMapping
    public ResponseEntity<Void> registerSubject(@Valid @RequestBody MirakuruSubjectRequest request) {
        subjectUseCase.registerSubject(toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /api/subjects — Lista todos los sujetos registrados.
     */
    @GetMapping
    public ResponseEntity<List<MirakuruSubjectResponse>> getAllSubjects() {
        List<MirakuruSubjectResponse> response = subjectUseCase.getAllSubjects()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/subjects/{id} — Obtiene un sujeto por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MirakuruSubjectResponse> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(subjectUseCase.getSubjectById(id)));
    }

    /**
     * PUT /api/subjects/{id} — Actualiza los datos de un sujeto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubject(@PathVariable Long id,
                                               @Valid @RequestBody MirakuruSubjectRequest request) {
        MirakuruSubject subject = toModel(request);
        subject.setId(id);
        subjectUseCase.updateSubject(subject);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/subjects/{id} — Elimina un sujeto del sistema.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectUseCase.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    // ── Mappers privados ───────────────────────────────────────────────────────

    private MirakuruSubject toModel(MirakuruSubjectRequest request) {
        MirakuruSubject subject = new MirakuruSubject();
        subject.setAlias(request.getAlias());
        subject.setMirakuruConcentration(request.getMirakuruConcentration());
        subject.setLastKnownLocation(request.getLastKnownLocation());
        subject.setBehavioralStatus(request.getBehavioralStatus());
        subject.setLastSightingSource(request.getLastSightingSource());
        return subject;
    }

    private MirakuruSubjectResponse toResponse(MirakuruSubject subject) {
        return new MirakuruSubjectResponse(
                subject.getId(),
                subject.getAlias(),
                subject.getMirakuruConcentration(),
                subject.getLastKnownLocation(),
                subject.getBehavioralStatus(),
                subject.getThreatLevel(),
                subject.getLastSightingSource(),
                subject.getRegisteredAt(),
                subject.getUpdatedAt()
        );
    }
}
