package Modelo;

import java.io.Serializable;

public class ModeloVistaRetiro implements Serializable {

    private Retiro retiro;
    private TramoRetiro tramo;
    private Direccion direccion;
    private Generador generador;

    public ModeloVistaRetiro() {

    }

    public Retiro getRetiro() {
        return retiro;
    }

    public void setRetiro(Retiro retiro) {
        this.retiro = retiro;
    }

    public TramoRetiro getTramo() {
        return tramo;
    }

    public void setTramo(TramoRetiro tramo) {
        this.tramo = tramo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Generador getGenerador() {
        return generador;
    }

    public void setGenerador(Generador generador) {
        this.generador = generador;
    }
}
