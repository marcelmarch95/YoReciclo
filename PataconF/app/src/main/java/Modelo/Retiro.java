package Modelo;

import java.io.Serializable;

public class Retiro implements Serializable {
    public String id;
    public String uid;
    public String idTramo;
    public String idDireccion;
    public String foto1;
    public String foto2;
    public String foto3;
    public String totplastico;
    public String totlata;
    public String totcarton;
    public String totvidrio;
    public String comentarios;
    public String idrecicladora;
    public String estado;

    public Retiro() {
        estado = "solicitado";
    }

    @Override
    public String toString() {
        return "Retiro{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", idTramo='" + idTramo + '\'' +
                ", idDireccion='" + idDireccion + '\'' +
                ", foto1='" + foto1 + '\'' +
                ", foto2='" + foto2 + '\'' +
                ", foto3='" + foto3 + '\'' +
                ", totplastico='" + totplastico + '\'' +
                ", totlata='" + totlata + '\'' +
                ", totcarton='" + totcarton + '\'' +
                ", totvidrio='" + totvidrio + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", estado='" + estado + '\'' +
                ", idrecicladora='" + idrecicladora + '\'' +
                '}';
    }

    public String getIdrecicladora() {
        return idrecicladora;
    }

    public void setIdrecicladora(String idrecicladora) {
        this.idrecicladora = idrecicladora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdTramo() {
        return idTramo;
    }

    public void setIdTramo(String idTramo) {
        this.idTramo = idTramo;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }

    public String getTotplastico() {
        return totplastico;
    }

    public void setTotplastico(String totplastico) {
        this.totplastico = totplastico;
    }

    public String getTotlata() {
        return totlata;
    }

    public void setTotlata(String totlata) {
        this.totlata = totlata;
    }

    public String getTotcarton() {
        return totcarton;
    }

    public void setTotcarton(String totcarton) {
        this.totcarton = totcarton;
    }

    public String getTotvidrio() {
        return totvidrio;
    }

    public void setTotvidrio(String totvidrio) {
        this.totvidrio = totvidrio;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
