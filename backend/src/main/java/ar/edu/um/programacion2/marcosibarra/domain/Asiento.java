package ar.edu.um.programacion2.marcosibarra.domain;

import ar.edu.um.programacion2.marcosibarra.domain.enumeration.EstadoAsiento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Asiento.
 */
@Entity
@Table(name = "asiento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Asiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fila", nullable = false)
    private Integer fila;

    @NotNull
    @Column(name = "columna", nullable = false)
    private Integer columna;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asiento", nullable = false)
    private EstadoAsiento estadoAsiento;

    @Column(name = "persona")
    private String persona;

    @Column(name = "bloqueado_hasta")
    private Instant bloqueadoHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "integrantes", "asientos", "ventas", "tipo" }, allowSetters = true)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "asientos", "usuario", "evento" }, allowSetters = true)
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Asiento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFila() {
        return this.fila;
    }

    public Asiento fila(Integer fila) {
        this.setFila(fila);
        return this;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getColumna() {
        return this.columna;
    }

    public Asiento columna(Integer columna) {
        this.setColumna(columna);
        return this;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    public EstadoAsiento getEstadoAsiento() {
        return this.estadoAsiento;
    }

    public Asiento estadoAsiento(EstadoAsiento estadoAsiento) {
        this.setEstadoAsiento(estadoAsiento);
        return this;
    }

    public void setEstadoAsiento(EstadoAsiento estadoAsiento) {
        this.estadoAsiento = estadoAsiento;
    }

    public String getPersona() {
        return this.persona;
    }

    public Asiento persona(String persona) {
        this.setPersona(persona);
        return this;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public Instant getBloqueadoHasta() {
        return this.bloqueadoHasta;
    }

    public Asiento bloqueadoHasta(Instant bloqueadoHasta) {
        this.setBloqueadoHasta(bloqueadoHasta);
        return this;
    }

    public void setBloqueadoHasta(Instant bloqueadoHasta) {
        this.bloqueadoHasta = bloqueadoHasta;
    }

    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Asiento evento(Evento evento) {
        this.setEvento(evento);
        return this;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Asiento venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asiento)) {
            return false;
        }
        return getId() != null && getId().equals(((Asiento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asiento{" +
            "id=" + getId() +
            ", fila=" + getFila() +
            ", columna=" + getColumna() +
            ", estadoAsiento='" + getEstadoAsiento() + "'" +
            ", persona='" + getPersona() + "'" +
            ", bloqueadoHasta='" + getBloqueadoHasta() + "'" +
            "}";
    }
}
