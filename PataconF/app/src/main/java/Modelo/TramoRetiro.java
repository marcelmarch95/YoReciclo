package Modelo;

import java.io.Serializable;

public class TramoRetiro implements Serializable {
    private String horainicio;
    private String horafinal;
    private String fecha;
    private Integer cupos;
    private Integer cuposdisponibles;
    private String uid;
    private String id;
    private boolean estado;
    private String idCobertura;

    public TramoRetiro() {
        this.estado = true;
    }

    public Integer getCuposdisponibles() {
        return cuposdisponibles;
    }

    public void setCuposdisponibles(Integer cuposdisponibles) {
        this.cuposdisponibles = cuposdisponibles;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(String horafinal) {
        this.horafinal = horafinal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCobertura() {
        return idCobertura;
    }

    public void setIdCobertura(String idCobertura) {
        this.idCobertura = idCobertura;
    }

    @Override
    public String toString() {
        return "TramoRetiro{" +
                "horainicio='" + horainicio + '\'' +
                ", horafinal='" + horafinal + '\'' +
                ", fecha='" + fecha + '\'' +
                ", cupos=" + cupos +
                ", cuposdisponibles=" + cuposdisponibles +
                ", uid='" + uid + '\'' +
                ", id='" + id + '\'' +
                ", estado=" + estado +
                ", idCobertura='" + idCobertura + '\'' +
                '}';
    }
}
