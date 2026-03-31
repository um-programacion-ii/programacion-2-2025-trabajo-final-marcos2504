package ar.edu.um.programacion2.marcosibarra.service.dto;

import ar.edu.um.programacion2.marcosibarra.domain.enumeration.EstadoSesion;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.programacion2.marcosibarra.domain.Sesion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SesionDTO implements Serializable {

    private Long id;

    @Lob
    private String tokenJWT;

    @NotNull
    private Instant fechaInicio;

    private Instant fechaExpiracion;

    @NotNull
    private Boolean activa;

    private Instant ultimoAcceso;

    private Long eventoSeleccionado;

    private EstadoSesion estadoSesion;

    @Lob
    private String asientosSeleccionados;

    private Integer cantidadAsientos;

    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenJWT() {
        return tokenJWT;
    }

    public void setTokenJWT(String tokenJWT) {
        this.tokenJWT = tokenJWT;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Instant fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Instant getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Instant ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Long getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    public void setEventoSeleccionado(Long eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public EstadoSesion getEstadoSesion() {
        return estadoSesion;
    }

    public void setEstadoSesion(EstadoSesion estadoSesion) {
        this.estadoSesion = estadoSesion;
    }

    public String getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public void setAsientosSeleccionados(String asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }

    public Integer getCantidadAsientos() {
        return cantidadAsientos;
    }

    public void setCantidadAsientos(Integer cantidadAsientos) {
        this.cantidadAsientos = cantidadAsientos;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SesionDTO)) {
            return false;
        }

        SesionDTO sesionDTO = (SesionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sesionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SesionDTO{" +
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
            ", usuario=" + getUsuario() +
            "}";
    }
}
