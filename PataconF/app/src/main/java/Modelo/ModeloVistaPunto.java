package Modelo;

import java.io.Serializable;

public class ModeloVistaPunto implements Serializable {

    private String direccion;
    private String pid;
    private String area;
    private String sector;
    private String recinto;
    private String observacion;
    private String lat;
    private String lng;
    private String foto;


    public ModeloVistaPunto(){

    }

    public ModeloVistaPunto(String direccion, String sector, String pid, String area, String recinto, String observacion, String lat, String lng, String foto) {
        this.direccion = direccion;
        this.pid = pid;
        this.area = area;
        this.recinto = recinto;
        this.observacion = observacion;
        this.lat = lat;
        this.lng = lng;
        this.foto = foto;
        this.sector = sector;
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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
