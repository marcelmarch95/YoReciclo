package Modelo;

import java.io.Serializable;

public class Generador implements Serializable {
    private String uid;
    private String nombre;
    private String apellido;
    private String numeroTelefono;
    private String correo;
    private String contraseña;
    private String keyNot;

    public Generador(){

    }

    public Generador(String uid, String nombre, String apellido, String numeroTelefono, String correo, String contraseña, String keyNot) {
        this.uid = uid;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroTelefono = numeroTelefono;
        this.correo = correo;
        this.contraseña = contraseña;
        this.keyNot = keyNot;
    }

    @Override
    public String toString() {
        return "Generador{" +
                "uid='" + uid + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", keyNot='" + keyNot + '\'' +
                '}';
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getKeyNot() {
        return keyNot;
    }

    public void setKeyNot(String keyNot) {
        this.keyNot = keyNot;
    }
}
