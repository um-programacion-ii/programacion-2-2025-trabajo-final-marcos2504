package ar.edu.um.programacion2.marcosibarra.service.mapper;

import ar.edu.um.programacion2.marcosibarra.domain.Evento;
import ar.edu.um.programacion2.marcosibarra.domain.User;
import ar.edu.um.programacion2.marcosibarra.domain.Venta;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.UserDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoId")
    VentaDTO toDto(Venta s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("eventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoDTO toDtoEventoId(Evento evento);
}
