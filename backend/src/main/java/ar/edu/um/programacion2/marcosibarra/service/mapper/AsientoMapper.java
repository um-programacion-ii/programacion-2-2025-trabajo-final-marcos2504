package ar.edu.um.programacion2.marcosibarra.service.mapper;

import ar.edu.um.programacion2.marcosibarra.domain.Asiento;
import ar.edu.um.programacion2.marcosibarra.domain.Evento;
import ar.edu.um.programacion2.marcosibarra.domain.Venta;
import ar.edu.um.programacion2.marcosibarra.service.dto.AsientoDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asiento} and its DTO {@link AsientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AsientoMapper extends EntityMapper<AsientoDTO, Asiento> {
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoId")
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    AsientoDTO toDto(Asiento s);

    @Named("eventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoDTO toDtoEventoId(Evento evento);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
