package com.arrow.acts.application.adapters.notifications;

import com.arrow.acts.domain.models.mission.Mission;
import com.arrow.acts.domain.models.subject.MirakuruSubject;
import com.arrow.acts.domain.ports.out.AlertNotificationPort;
import org.springframework.stereotype.Service;

/**
 * Adaptador de notificaciones del sistema ACTS.
 *
 * <p>Implementación inicial de {@link AlertNotificationPort} que registra
 * alertas en consola. En producción, esta implementación puede reemplazarse
 * por una que envíe notificaciones push, emails cifrados, mensajes de radio,
 * o integración con sistemas tácticos externos, sin modificar el dominio.</p>
 *
 * <p>Principio SOLID aplicado: OCP — el dominio no cambia si se reemplaza
 * esta implementación por otra más sofisticada.</p>
 */
@Service
public class ConsoleAlertAdapter implements AlertNotificationPort {

    @Override
    public void notifyCriticalThreat(MirakuruSubject subject) {
        System.out.printf(
            "[ACTS ⚠️ CRITICAL] Sujeto '%s' clasificado como AMENAZA CRÍTICA | " +
            "Concentración: %.1f | Ubicación: %s%n",
            subject.getAlias(),
            subject.getMirakuruConcentration(),
            subject.getLastKnownLocation()
        );
    }

    @Override
    public void notifyMissionActivated(Mission mission) {
        System.out.printf(
            "[ACTS 🏹 MISIÓN ACTIVA] '%s' | Prioridad: %s | Zona: %s%n",
            mission.getObjective(),
            mission.getPriority(),
            mission.getOperationalZone()
        );
    }

    @Override
    public void notifyMissionEnded(Mission mission) {
        System.out.printf(
            "[ACTS ✅ MISIÓN %s] '%s' | Zona: %s%n",
            mission.getStatus(),
            mission.getObjective(),
            mission.getOperationalZone()
        );
    }

    @Override
    public void sendEmergencyAlert(String message) {
        System.out.printf("[ACTS 🚨 EMERGENCIA] %s%n", message);
    }
}
