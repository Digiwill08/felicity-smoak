package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando una misión CRITICAL intenta activarse
 * sin tener al menos un agente de campo (FIELD_AGENT) asignado.
 */
public class InsufficientAgentsException extends BusinessException {

    public InsufficientAgentsException(Long missionId) {
        super(String.format(
            "La misión CRITICAL [%d] requiere al menos un agente de campo (FIELD_AGENT) asignado.", missionId
        ));
    }
}
