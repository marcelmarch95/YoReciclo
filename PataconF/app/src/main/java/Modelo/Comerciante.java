package Modelo;

import java.io.Serializable;

public class Comerciante implements Serializable {

    private String uid;
    private String nombreComercio;
    private String categoria;
    private String localidad;
    private String direccion;
    private String nombreEncargado;
    private String numeroTelefono;
    private String correo;
    private String contraseña;
    private String lat;
    private String keyNot;
    private String lng;
    private String foto;

    public Comerciante(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombreComercio() {
        return nombreComercio;
    }

    public void setNombreComercio(String nombreComercio) {
        this.nombreComercio = nombreComercio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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

    public String getKeyNot() {
        return keyNot;
    }

    public void setKeyNot(String keyNot) {
        this.keyNot = keyNot;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Comerciante{" +
                "uid='" + uid + '\'' +
                ", nombreComercio='" + nombreComercio + '\'' +
                ", categoria='" + categoria + '\'' +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", nombreEncargado='" + nombreEncargado + '\'' +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", lat='" + lat + '\'' +
                ", keyNot='" + keyNot + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}
