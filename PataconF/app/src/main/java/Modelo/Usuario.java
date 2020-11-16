package Modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String uid;
    private String nombre;
    private String correo;
    private String telefono;
    private String perfil;
    private String key;

    public Usuario(String uid, String nombre, String correo, String telefono, String perfil, String key) {
        this.uid = uid;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.perfil = perfil;
        this.key = key;
    }

    public Usuario(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
