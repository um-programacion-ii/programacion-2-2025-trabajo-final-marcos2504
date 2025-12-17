package ar.edu.um.programacion2.marcosibarra.repository;

import ar.edu.um.programacion2.marcosibarra.domain.Evento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Evento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    Optional<Evento> findByIdCatedra(Long idCatedra);
}
