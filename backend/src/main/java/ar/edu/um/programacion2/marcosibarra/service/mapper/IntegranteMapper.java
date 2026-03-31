package ar.edu.um.programacion2.marcosibarra.service.mapper;

import ar.edu.um.programacion2.marcosibarra.domain.Evento;
import ar.edu.um.programacion2.marcosibarra.domain.Integrante;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.IntegranteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Integrante} and its DTO {@link IntegranteDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntegranteMapper extends EntityMapper<IntegranteDTO, Integrante> {
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoId")
    IntegranteDTO toDto(Integrante s);

    @Named("eventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoDTO toDtoEventoId(Evento evento);
}
