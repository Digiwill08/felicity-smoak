package com.arrow.acts.domain.ports.out;

import java.util.Optional;

/**
 * Puerto de salida: rastreo de ubicación de sujetos Mirakuru en tiempo real.
 *
 * <p>Abstracción de la integración con sistemas externos de rastreo
 * (GPS, cámaras de seguridad, tecnología ARGUS, etc.).
 * Su implementación concreta vive en la capa de infraestructura.</p>
 */
public interface TrackingPort {

    /**
     * Obtiene la última ubicación conocida de un sujeto.
     *
     * @param subjectId ID del sujeto en el sistema ACTS
     * @return Descripción de la ubicación, o vacío si no hay señal
     */
    Optional<String> getLastKnownLocation(Long subjectId);

    /**
     * Verifica si el sujeto ha sido visto en la zona operacional especificada.
     *
     * @param subjectId ID del sujeto
     * @param zone Nombre de la zona operacional
     * @return true si hay registro del sujeto en esa zona
     */
    boolean isSubjectInZone(Long subjectId, String zone);
}
