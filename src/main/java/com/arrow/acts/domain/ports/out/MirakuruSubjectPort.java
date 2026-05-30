package com.arrow.acts.domain.ports.out;

import com.arrow.acts.domain.models.subject.MirakuruSubject;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia de sujetos Mirakuru.
 *
 * <p>Define las operaciones de almacenamiento y recuperación de sujetos
 * sin acoplarse a ninguna tecnología de base de datos concreta.
 * Su implementación concreta vive en la capa de infraestructura.</p>
 */
public interface MirakuruSubjectPort {

    boolean existsByAlias(String alias);

    void save(MirakuruSubject subject);

    Optional<MirakuruSubject> findById(Long id);

    List<MirakuruSubject> findAll();

    List<MirakuruSubject> findAllActive();

    void deleteById(Long id);
}
