package ar.edu.um.programacion2.marcosibarra.domain;

import static ar.edu.um.programacion2.marcosibarra.domain.SesionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.marcosibarra.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SesionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sesion.class);
        Sesion sesion1 = getSesionSample1();
        Sesion sesion2 = new Sesion();
        assertThat(sesion1).isNotEqualTo(sesion2);

        sesion2.setId(sesion1.getId());
        assertThat(sesion1).isEqualTo(sesion2);

        sesion2 = getSesionSample2();
        assertThat(sesion1).isNotEqualTo(sesion2);
    }
}
