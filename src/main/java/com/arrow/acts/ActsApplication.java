package com.arrow.acts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del sistema ACTS (Arrow Command & Tactical System).
 *
 * <p>"Esto no es solo código. Es lo que nos da ventaja." — Felicity Smoak</p>
 *
 * <p>Arquitectura: Hexagonal (Ports & Adapters) + Domain-Driven Design + SOLID.
 * El dominio está completamente aislado de la infraestructura mediante puertos.</p>
 */
@SpringBootApplication
public class ActsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActsApplication.class, args);
    }
}
