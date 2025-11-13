package ar.edu.um.programacion2.marcosibarra.repository;

import ar.edu.um.programacion2.marcosibarra.domain.EventoTipo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventoTipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventoTipoRepository extends JpaRepository<EventoTipo, Long> {}
