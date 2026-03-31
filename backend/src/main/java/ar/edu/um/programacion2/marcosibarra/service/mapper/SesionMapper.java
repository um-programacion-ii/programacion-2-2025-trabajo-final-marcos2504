package ar.edu.um.programacion2.marcosibarra.service.mapper;

import ar.edu.um.programacion2.marcosibarra.domain.Sesion;
import ar.edu.um.programacion2.marcosibarra.domain.User;
import ar.edu.um.programacion2.marcosibarra.service.dto.SesionDTO;
import ar.edu.um.programacion2.marcosibarra.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sesion} and its DTO {@link SesionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SesionMapper extends EntityMapper<SesionDTO, Sesion> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    SesionDTO toDto(Sesion s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
