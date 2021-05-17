package Modelo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TramoRetiro implements Serializable, Comparable<TramoRetiro> {
    private String horainicio;
    private String horafinal;
    private String fecha;
    private Integer cupos;
    private Integer cuposdisponibles;
    private String uid;
    private String id;
    private String idCobertura;
    private boolean estado;

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public TramoRetiro() {
    }

    public Integer getCuposdisponibles() {
        return cuposdisponibles;
    }

    public void setCuposdisponibles(Integer cuposdisponibles) {
        this.cuposdisponibles = cuposdisponibles;
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
                ", idCobertura='" + idCobertura + '\'' +
                ", estado=" + estado +
                '}';
    }

    @Override
    public int compareTo(TramoRetiro tramoRetiro) {

        final String NEW_FORMAT = " hh:mm";
        String formatDate;
        SimpleDateFormat sdf = new SimpleDateFormat(NEW_FORMAT);
        Date d1= null;
        Date d2=null;
        try {
            d1 = sdf.parse(this.horainicio);
            d2 = sdf.parse(tramoRetiro.getHorainicio());

            return d1.compareTo(d2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }
}
