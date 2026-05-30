package com.arrow.acts.domain.ports.in;

import com.arrow.acts.domain.exceptions.BusinessException;
import com.arrow.acts.domain.exceptions.DuplicateSubjectException;
import com.arrow.acts.domain.models.subject.MirakuruSubject;

import java.util.List;

/**
 * Puerto de entrada: operaciones sobre sujetos Mirakuru.
 *
 * <p>Define los casos de uso disponibles para registrar, consultar
 * y gestionar individuos afectados por el suero Mirakuru.</p>
 */
public interface SubjectUseCase {

    /**
     * Registra un nuevo sujeto Mirakuru en el sistema.
     *
     * @param subject Datos del sujeto a registrar
     * @throws DuplicateSubjectException si ya existe un sujeto con el mismo alias
     * @throws BusinessException si los datos son inválidos
     */
    void registerSubject(MirakuruSubject subject) throws BusinessException;

    /**
     * Obtiene todos los sujetos registrados en el sistema.
     *
     * @return Lista de todos los sujetos Mirakuru
     */
    List<MirakuruSubject> getAllSubjects();

    /**
     * Obtiene un sujeto por su ID.
     *
     * @param id ID del sujeto
     * @return El sujeto encontrado
     * @throws com.arrow.acts.domain.exceptions.SubjectNotFoundException si no existe
     */
    MirakuruSubject getSubjectById(Long id);

    /**
     * Actualiza la información de un sujeto existente.
     *
     * @param subject Datos actualizados del sujeto (debe tener ID)
     */
    void updateSubject(MirakuruSubject subject);

    /**
     * Elimina un sujeto del sistema por su ID.
     *
     * @param id ID del sujeto a eliminar
     */
    void deleteSubject(Long id);
}
