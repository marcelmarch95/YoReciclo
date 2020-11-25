package Modelo;

import java.io.Serializable;

public class Punto implements Serializable {
    private String direccion;
    private String pid;
    private String id;
    private String area;
    private String sector;
    private String recinto;
    private String observacion;
    private String lat;
    private String lng;
    private String foto;
    private boolean isplastico;
    private boolean islatas;
    private boolean isvidrio;

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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public boolean isIsplastico() {
        return isplastico;
    }

    public void setIsplastico(boolean isplastico) {
        this.isplastico = isplastico;
    }

    public boolean isIslatas() {
        return islatas;
    }

    public void setIslatas(boolean islatas) {
        this.islatas = islatas;
    }

    public boolean isIsvidrio() {
        return isvidrio;
    }

    public void setIsvidrio(boolean isvidrio) {
        this.isvidrio = isvidrio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Punto{" +
                "direccion='" + direccion + '\'' +
                ", pid='" + pid + '\'' +
                ", area='" + area + '\'' +
                ", sector='" + sector + '\'' +
                ", recinto='" + recinto + '\'' +
                ", observacion='" + observacion + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", foto='" + foto + '\'' +
                ", isplastico=" + isplastico +
                ", islatas=" + islatas +
                ", isvidrio=" + isvidrio +
                ", id=" + id +
                '}';
    }
}
