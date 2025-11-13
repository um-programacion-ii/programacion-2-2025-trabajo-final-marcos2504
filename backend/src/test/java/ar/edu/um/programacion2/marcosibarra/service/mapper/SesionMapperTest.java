package ar.edu.um.programacion2.marcosibarra.service.mapper;

import static ar.edu.um.programacion2.marcosibarra.domain.SesionAsserts.*;
import static ar.edu.um.programacion2.marcosibarra.domain.SesionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SesionMapperTest {

    private SesionMapper sesionMapper;

    @BeforeEach
    void setUp() {
        sesionMapper = new SesionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSesionSample1();
        var actual = sesionMapper.toEntity(sesionMapper.toDto(expected));
        assertSesionAllPropertiesEquals(expected, actual);
    }
}
