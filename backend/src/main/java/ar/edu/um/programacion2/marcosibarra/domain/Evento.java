package ar.edu.um.programacion2.marcosibarra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Evento.
 */
@Entity
@Table(name = "evento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_catedra", nullable = false)
    private Long idCatedra;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "resumen")
    private String resumen;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "fila_asientos")
    private Integer filaAsientos;

    @Column(name = "columna_asientos")
    private Integer columnaAsientos;

    @NotNull
    @Column(name = "precio_entrada", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioEntrada;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evento" }, allowSetters = true)
    private Set<Integrante> integrantes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evento", "venta" }, allowSetters = true)
    private Set<Asiento> asientos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asientos", "usuario", "evento" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private EventoTipo tipo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCatedra() {
        return this.idCatedra;
    }

    public Evento idCatedra(Long idCatedra) {
        this.setIdCatedra(idCatedra);
        return this;
    }

    public void setIdCatedra(Long idCatedra) {
        this.idCatedra = idCatedra;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Evento titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return this.resumen;
    }

    public Evento resumen(String resumen) {
        this.setResumen(resumen);
        return this;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Evento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Evento fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Evento direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Evento imagen(String imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getFilaAsientos() {
        return this.filaAsientos;
    }

    public Evento filaAsientos(Integer filaAsientos) {
        this.setFilaAsientos(filaAsientos);
        return this;
    }

    public void setFilaAsientos(Integer filaAsientos) {
        this.filaAsientos = filaAsientos;
    }

    public Integer getColumnaAsientos() {
        return this.columnaAsientos;
    }

    public Evento columnaAsientos(Integer columnaAsientos) {
        this.setColumnaAsientos(columnaAsientos);
        return this;
    }

    public void setColumnaAsientos(Integer columnaAsientos) {
        this.columnaAsientos = columnaAsientos;
    }

    public BigDecimal getPrecioEntrada() {
        return this.precioEntrada;
    }

    public Evento precioEntrada(BigDecimal precioEntrada) {
        this.setPrecioEntrada(precioEntrada);
        return this;
    }

    public void setPrecioEntrada(BigDecimal precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public Set<Integrante> getIntegrantes() {
        return this.integrantes;
    }

    public void setIntegrantes(Set<Integrante> integrantes) {
        if (this.integrantes != null) {
            this.integrantes.forEach(i -> i.setEvento(null));
        }
        if (integrantes != null) {
            integrantes.forEach(i -> i.setEvento(this));
        }
        this.integrantes = integrantes;
    }

    public Evento integrantes(Set<Integrante> integrantes) {
        this.setIntegrantes(integrantes);
        return this;
    }

    public Evento addIntegrante(Integrante integrante) {
        this.integrantes.add(integrante);
        integrante.setEvento(this);
        return this;
    }

    public Evento removeIntegrante(Integrante integrante) {
        this.integrantes.remove(integrante);
        integrante.setEvento(null);
        return this;
    }

    public Set<Asiento> getAsientos() {
        return this.asientos;
    }

    public void setAsientos(Set<Asiento> asientos) {
        if (this.asientos != null) {
            this.asientos.forEach(i -> i.setEvento(null));
        }
        if (asientos != null) {
            asientos.forEach(i -> i.setEvento(this));
        }
        this.asientos = asientos;
    }

    public Evento asientos(Set<Asiento> asientos) {
        this.setAsientos(asientos);
        return this;
    }

    public Evento addAsiento(Asiento asiento) {
        this.asientos.add(asiento);
        asiento.setEvento(this);
        return this;
    }

    public Evento removeAsiento(Asiento asiento) {
        this.asientos.remove(asiento);
        asiento.setEvento(null);
        return this;
    }

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        if (this.ventas != null) {
            this.ventas.forEach(i -> i.setEvento(null));
        }
        if (ventas != null) {
            ventas.forEach(i -> i.setEvento(this));
        }
        this.ventas = ventas;
    }

    public Evento ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Evento addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.setEvento(this);
        return this;
    }

    public Evento removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.setEvento(null);
        return this;
    }

    public EventoTipo getTipo() {
        return this.tipo;
    }

    public void setTipo(EventoTipo eventoTipo) {
        this.tipo = eventoTipo;
    }

    public Evento tipo(EventoTipo eventoTipo) {
        this.setTipo(eventoTipo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evento)) {
            return false;
        }
        return getId() != null && getId().equals(((Evento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evento{" +
            "id=" + getId() +
            ", idCatedra=" + getIdCatedra() +
            ", titulo='" + getTitulo() + "'" +
            ", resumen='" + getResumen() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", filaAsientos=" + getFilaAsientos() +
            ", columnaAsientos=" + getColumnaAsientos() +
            ", precioEntrada=" + getPrecioEntrada() +
            "}";
    }
}
