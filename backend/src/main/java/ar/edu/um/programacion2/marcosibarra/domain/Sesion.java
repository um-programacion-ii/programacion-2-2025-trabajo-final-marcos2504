package ar.edu.um.programacion2.marcosibarra.domain;

import ar.edu.um.programacion2.marcosibarra.domain.enumeration.EstadoSesion;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sesion.
 */
@Entity
@Table(name = "sesion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "token_jwt", nullable = false)
    private String tokenJWT;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private Instant fechaInicio;

    @Column(name = "fecha_expiracion")
    private Instant fechaExpiracion;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "ultimo_acceso")
    private Instant ultimoAcceso;

    @Column(name = "evento_seleccionado")
    private Long eventoSeleccionado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_sesion")
    private EstadoSesion estadoSesion;

    @Lob
    @Column(name = "asientos_seleccionados")
    private String asientosSeleccionados;

    @Column(name = "cantidad_asientos")
    private Integer cantidadAsientos;

    @ManyToOne(fetch = FetchType.LAZY)
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sesion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenJWT() {
        return this.tokenJWT;
    }

    public Sesion tokenJWT(String tokenJWT) {
        this.setTokenJWT(tokenJWT);
        return this;
    }

    public void setTokenJWT(String tokenJWT) {
        this.tokenJWT = tokenJWT;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Sesion fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaExpiracion() {
        return this.fechaExpiracion;
    }

    public Sesion fechaExpiracion(Instant fechaExpiracion) {
        this.setFechaExpiracion(fechaExpiracion);
        return this;
    }

    public void setFechaExpiracion(Instant fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public Sesion activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Instant getUltimoAcceso() {
        return this.ultimoAcceso;
    }

    public Sesion ultimoAcceso(Instant ultimoAcceso) {
        this.setUltimoAcceso(ultimoAcceso);
        return this;
    }

    public void setUltimoAcceso(Instant ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Long getEventoSeleccionado() {
        return this.eventoSeleccionado;
    }

    public Sesion eventoSeleccionado(Long eventoSeleccionado) {
        this.setEventoSeleccionado(eventoSeleccionado);
        return this;
    }

    public void setEventoSeleccionado(Long eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public EstadoSesion getEstadoSesion() {
        return this.estadoSesion;
    }

    public Sesion estadoSesion(EstadoSesion estadoSesion) {
        this.setEstadoSesion(estadoSesion);
        return this;
    }

    public void setEstadoSesion(EstadoSesion estadoSesion) {
        this.estadoSesion = estadoSesion;
    }

    public String getAsientosSeleccionados() {
        return this.asientosSeleccionados;
    }

    public Sesion asientosSeleccionados(String asientosSeleccionados) {
        this.setAsientosSeleccionados(asientosSeleccionados);
        return this;
    }

    public void setAsientosSeleccionados(String asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }

    public Integer getCantidadAsientos() {
        return this.cantidadAsientos;
    }

    public Sesion cantidadAsientos(Integer cantidadAsientos) {
        this.setCantidadAsientos(cantidadAsientos);
        return this;
    }

    public void setCantidadAsientos(Integer cantidadAsientos) {
        this.cantidadAsientos = cantidadAsientos;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Sesion usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sesion)) {
            return false;
        }
        return getId() != null && getId().equals(((Sesion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sesion{" +
            "id=" + getId() +
            ", tokenJWT='" + getTokenJWT() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaExpiracion='" + getFechaExpiracion() + "'" +
            ", activa='" + getActiva() + "'" +
            ", ultimoAcceso='" + getUltimoAcceso() + "'" +
            ", eventoSeleccionado=" + getEventoSeleccionado() +
            ", estadoSesion='" + getEstadoSesion() + "'" +
            ", asientosSeleccionados='" + getAsientosSeleccionados() + "'" +
            ", cantidadAsientos=" + getCantidadAsientos() +
            "}";
    }
}
