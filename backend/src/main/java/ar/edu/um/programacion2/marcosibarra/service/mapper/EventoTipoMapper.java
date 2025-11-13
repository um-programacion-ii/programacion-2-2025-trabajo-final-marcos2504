package ar.edu.um.programacion2.marcosibarra.service.mapper;

import ar.edu.um.programacion2.marcosibarra.domain.EventoTipo;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoTipoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventoTipo} and its DTO {@link EventoTipoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoTipoMapper extends EntityMapper<EventoTipoDTO, EventoTipo> {}
