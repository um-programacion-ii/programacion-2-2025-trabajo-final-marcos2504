package ar.edu.um.programacion2.marcosibarra.service.mapper;

import static ar.edu.um.programacion2.marcosibarra.domain.EventoAsserts.*;
import static ar.edu.um.programacion2.marcosibarra.domain.EventoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventoMapperTest {

    private EventoMapper eventoMapper;

    @BeforeEach
    void setUp() {
        eventoMapper = new EventoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventoSample1();
        var actual = eventoMapper.toEntity(eventoMapper.toDto(expected));
        assertEventoAllPropertiesEquals(expected, actual);
    }
}
