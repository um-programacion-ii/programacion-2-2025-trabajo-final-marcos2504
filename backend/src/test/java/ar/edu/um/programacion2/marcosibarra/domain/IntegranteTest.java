package ar.edu.um.programacion2.marcosibarra.domain;

import static ar.edu.um.programacion2.marcosibarra.domain.EventoTestSamples.*;
import static ar.edu.um.programacion2.marcosibarra.domain.IntegranteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.marcosibarra.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegranteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Integrante.class);
        Integrante integrante1 = getIntegranteSample1();
        Integrante integrante2 = new Integrante();
        assertThat(integrante1).isNotEqualTo(integrante2);

        integrante2.setId(integrante1.getId());
        assertThat(integrante1).isEqualTo(integrante2);

        integrante2 = getIntegranteSample2();
        assertThat(integrante1).isNotEqualTo(integrante2);
    }

    @Test
    void eventoTest() {
        Integrante integrante = getIntegranteRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        integrante.setEvento(eventoBack);
        assertThat(integrante.getEvento()).isEqualTo(eventoBack);

        integrante.evento(null);
        assertThat(integrante.getEvento()).isNull();
    }
}
