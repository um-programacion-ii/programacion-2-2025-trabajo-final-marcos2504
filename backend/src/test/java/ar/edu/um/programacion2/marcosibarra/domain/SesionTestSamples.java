package ar.edu.um.programacion2.marcosibarra.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SesionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Sesion getSesionSample1() {
        return new Sesion().id(1L).eventoSeleccionado(1L).cantidadAsientos(1);
    }

    public static Sesion getSesionSample2() {
        return new Sesion().id(2L).eventoSeleccionado(2L).cantidadAsientos(2);
    }

    public static Sesion getSesionRandomSampleGenerator() {
        return new Sesion()
            .id(longCount.incrementAndGet())
            .eventoSeleccionado(longCount.incrementAndGet())
            .cantidadAsientos(intCount.incrementAndGet());
    }
}
