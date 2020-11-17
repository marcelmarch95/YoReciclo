package Modelo;

import java.io.Serializable;

public class Punto implements Serializable {
    private String direccion;
    private String pid;
    private String area;
    private String propiedad;
    private String recinto;
    private String observacion;
    private String lat;
    private String lng;
    private String foto;

    public Punto() {
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getRecinto() {
        return recinto;
    }

    public void setRecinto(String recinto) {
        this.recinto = recinto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Punto{" +
                "direccion='" + direccion + '\'' +
                ", pid='" + pid + '\'' +
                ", area='" + area + '\'' +
                ", propiedad='" + propiedad + '\'' +
                ", recinto='" + recinto + '\'' +
                ", observacion='" + observacion + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
