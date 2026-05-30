package com.arrow.acts.domain.ports.out;

import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.models.mission.Mission;

/**
 * Puerto de salida: envío de alertas y notificaciones al equipo Arrow.
 *
 * <p>Abstrae el canal de comunicación con el equipo (mensajería, radio,
 * sistemas tácticos, etc.). Su implementación concreta puede enviar
 * notificaciones push, emails, mensajes de radio cifrados, etc.</p>
 */
public interface AlertNotificationPort {

    /**
     * Notifica al equipo sobre un nuevo sujeto de amenaza CRITICAL detectado.
     *
     * @param subject El sujeto que alcanzó nivel de amenaza crítico
     */
    void notifyCriticalThreat(MirakuruSubject subject);

    /**
     * Notifica al equipo sobre el inicio de una misión activa.
     *
     * @param mission La misión que acaba de activarse
     */
    void notifyMissionActivated(Mission mission);

    /**
     * Notifica al equipo sobre la finalización de una misión.
     *
     * @param mission La misión completada o abortada
     */
    void notifyMissionEnded(Mission mission);

    /**
     * Envía una alerta de emergencia general al equipo completo.
     *
     * @param message Texto del mensaje de alerta
     */
    void sendEmergencyAlert(String message);
}
