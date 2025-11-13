package ar.edu.um.programacion2.marcosibarra.domain;

import static ar.edu.um.programacion2.marcosibarra.domain.AsientoTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.EventoTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.EventoTipoTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.IntegranteTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.marcosibarra.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evento.class);
        Evento evento1 = getEventoSample1();
        Evento evento2 = new Evento();
        assertThat(evento1).isNotEqualTo(evento2);

        evento2.setId(evento1.getId());
        assertThat(evento1).isEqualTo(evento2);

        evento2 = getEventoSample2();
        assertThat(evento1).isNotEqualTo(evento2);
    }

    @Test
    void integranteTest() {
        Evento evento = getEventoRandomSampleGenerator();
        Integrante integranteBack = getIntegranteRandomSampleGenerator();

        evento.addIntegrante(integranteBack);
        assertThat(evento.getIntegrantes()).containsOnly(integranteBack);
        assertThat(integranteBack.getEvento()).isEqualTo(evento);

        evento.removeIntegrante(integranteBack);
        assertThat(evento.getIntegrantes()).doesNotContain(integranteBack);
        assertThat(integranteBack.getEvento()).isNull();

        evento.integrantes(new HashSet<>(Set.of(integranteBack)));
        assertThat(evento.getIntegrantes()).containsOnly(integranteBack);
        assertThat(integranteBack.getEvento()).isEqualTo(evento);

        evento.setIntegrantes(new HashSet<>());
        assertThat(evento.getIntegrantes()).doesNotContain(integranteBack);
        assertThat(integranteBack.getEvento()).isNull();
    }

    @Test
    void asientoTest() {
        Evento evento = getEventoRandomSampleGenerator();
        Asiento asientoBack = getAsientoRandomSampleGenerator();

        evento.addAsiento(asientoBack);
        assertThat(evento.getAsientos()).containsOnly(asientoBack);
        assertThat(asientoBack.getEvento()).isEqualTo(evento);

        evento.removeAsiento(asientoBack);
        assertThat(evento.getAsientos()).doesNotContain(asientoBack);
        assertThat(asientoBack.getEvento()).isNull();

        evento.asientos(new HashSet<>(Set.of(asientoBack)));
        assertThat(evento.getAsientos()).containsOnly(asientoBack);
        assertThat(asientoBack.getEvento()).isEqualTo(evento);

        evento.setAsientos(new HashSet<>());
        assertThat(evento.getAsientos()).doesNotContain(asientoBack);
        assertThat(asientoBack.getEvento()).isNull();
    }

    @Test
    void ventaTest() {
        Evento evento = getEventoRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        evento.addVenta(ventaBack);
        assertThat(evento.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getEvento()).isEqualTo(evento);

        evento.removeVenta(ventaBack);
        assertThat(evento.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getEvento()).isNull();

        evento.ventas(new HashSet<>(Set.of(ventaBack)));
        assertThat(evento.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getEvento()).isEqualTo(evento);

        evento.setVentas(new HashSet<>());
        assertThat(evento.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getEvento()).isNull();
    }

    @Test
    void tipoTest() {
        Evento evento = getEventoRandomSampleGenerator();
        EventoTipo eventoTipoBack = getEventoTipoRandomSampleGenerator();

        evento.setTipo(eventoTipoBack);
        assertThat(evento.getTipo()).isEqualTo(eventoTipoBack);

        evento.tipo(null);
        assertThat(evento.getTipo()).isNull();
    }
}
