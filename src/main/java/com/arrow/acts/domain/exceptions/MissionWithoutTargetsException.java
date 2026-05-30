package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando se intenta activar una misión sin objetivos definidos.
 *
 * <p>Invariante del dominio: ninguna misión puede pasar a estado ACTIVE
 * si no tiene al menos un ID de sujeto objetivo registrado.</p>
 */
public class MissionWithoutTargetsException extends BusinessException {

    public MissionWithoutTargetsException(Long missionId) {
        super(String.format(
            "La misión [%d] no puede activarse sin objetivos definidos.", missionId
        ));
    }
}
