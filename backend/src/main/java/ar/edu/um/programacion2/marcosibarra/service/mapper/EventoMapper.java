package ar.edu.um.programacion2.marcosibarra.service.mapper;

import ar.edu.um.programacion2.marcosibarra.domain.Evento;
import ar.edu.um.programacion2.marcosibarra.domain.EventoTipo;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoTipoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evento} and its DTO {@link EventoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoMapper extends EntityMapper<EventoDTO, Evento> {
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "eventoTipoId")
    EventoDTO toDto(Evento s);

    @Named("eventoTipoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoTipoDTO toDtoEventoTipoId(EventoTipo eventoTipo);
}
