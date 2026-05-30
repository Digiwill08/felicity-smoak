package com.arrow.acts.application.adapters.tracking;

import com.arrow.acts.domain.ports.out.MirakuruSubjectPort;
import com.arrow.acts.domain.ports.out.TrackingPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Adaptador de rastreo de sujetos Mirakuru.
 *
 * <p>Implementación inicial de {@link TrackingPort} basada en los datos
 * de última ubicación ya registrados en el sistema (MySQL). En producción,
 * esta implementación puede reemplazarse por integración directa con:
 * <ul>
 *   <li>Sistema ARGUS (satélites y cámaras de la ciudad)</li>
 *   <li>GPS de dispositivos rastreados</li>
 *   <li>Red de cámaras de seguridad de Starling City</li>
 * </ul>
 * Sin necesidad de modificar el dominio (OCP — Open/Closed Principle).</p>
 */
@Service
public class LocalTrackingAdapter implements TrackingPort {

    private final MirakuruSubjectPort subjectPort;

    public LocalTrackingAdapter(MirakuruSubjectPort subjectPort) {
        this.subjectPort = subjectPort;
    }

    /**
     * Obtiene la última ubicación conocida del sujeto desde el sistema ACTS.
     *
     * <p>Futura integración: reemplazar por llamada a API de ARGUS.</p>
     */
    @Override
    public Optional<String> getLastKnownLocation(Long subjectId) {
        return subjectPort.findById(subjectId)
                .map(subject -> subject.getLastKnownLocation());
    }

    /**
     * Verifica si el sujeto fue visto en la zona operacional dada.
     *
     * <p>Implementación basada en coincidencia de texto en la ubicación
     * registrada. Futura integración: reemplazar por coordenadas GPS.</p>
     */
    @Override
    public boolean isSubjectInZone(Long subjectId, String zone) {
        return subjectPort.findById(subjectId)
                .map(subject -> {
                    String location = subject.getLastKnownLocation();
                    return location != null
                            && location.toLowerCase().contains(zone.toLowerCase());
                })
                .orElse(false);
    }
}
