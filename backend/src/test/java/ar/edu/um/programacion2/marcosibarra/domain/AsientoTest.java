package ar.edu.um.programacion2.marcosibarra.domain;

import static ar.edu.um.programacion2.marcosibarra.domain.AsientoTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.EventoTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.marcosibarra.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AsientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Asiento.class);
        Asiento asiento1 = getAsientoSample1();
        Asiento asiento2 = new Asiento();
        assertThat(asiento1).isNotEqualTo(asiento2);

        asiento2.setId(asiento1.getId());
        assertThat(asiento1).isEqualTo(asiento2);

        asiento2 = getAsientoSample2();
        assertThat(asiento1).isNotEqualTo(asiento2);
    }

    @Test
    void eventoTest() {
        Asiento asiento = getAsientoRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        asiento.setEvento(eventoBack);
        assertThat(asiento.getEvento()).isEqualTo(eventoBack);

        asiento.evento(null);
        assertThat(asiento.getEvento()).isNull();
    }

    @Test
    void ventaTest() {
        Asiento asiento = getAsientoRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        asiento.setVenta(ventaBack);
        assertThat(asiento.getVenta()).isEqualTo(ventaBack);

        asiento.venta(null);
        assertThat(asiento.getVenta()).isNull();
    }
}
