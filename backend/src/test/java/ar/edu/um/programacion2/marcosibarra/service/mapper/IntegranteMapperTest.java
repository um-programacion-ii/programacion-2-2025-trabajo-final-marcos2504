package ar.edu.um.programacion2.marcosibarra.service.mapper;

import static ar.edu.um.programacion2.marcosibarra.domain.IntegranteAsserts.*;
import static ar.edu.um.programacion2.marcosibarra.domain.IntegranteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegranteMapperTest {

    private IntegranteMapper integranteMapper;

    @BeforeEach
    void setUp() {
        integranteMapper = new IntegranteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIntegranteSample1();
        var actual = integranteMapper.toEntity(integranteMapper.toDto(expected));
        assertIntegranteAllPropertiesEquals(expected, actual);
    }
}
