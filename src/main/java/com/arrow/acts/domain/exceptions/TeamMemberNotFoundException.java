package com.arrow.acts.domain.exceptions;

/**
 * Excepción lanzada cuando se busca un miembro del equipo que no existe en el sistema.
 */
public class TeamMemberNotFoundException extends NotFoundException {

    public TeamMemberNotFoundException(Long id) {
        super(String.format("Agente con ID [%d] no encontrado en el equipo Arrow.", id));
    }

    public TeamMemberNotFoundException(String codename) {
        super(String.format("Agente con nombre en clave '%s' no encontrado en el equipo Arrow.", codename));
    }
}
