package Modelo;

import java.io.Serializable;

public class ModeloVistaTramo implements Serializable, Comparable<ModeloVistaTramo> {

    private TramoRetiro tramoRetiro;
    private Cobertura cobertura;


    public ModeloVistaTramo(){

    }

    public TramoRetiro getTramoRetiro() {
        return tramoRetiro;
    }

    public Cobertura getCobertura() {
        return cobertura;
    }

    public void setCobertura(Cobertura cobertura) {
        this.cobertura = cobertura;
    }

    public void setTramoRetiro(TramoRetiro tramoRetiro) {
        this.tramoRetiro = tramoRetiro;
    }

    @Override
    public String toString() {
        return "ModeloVistaTramo{" +
                "tramoRetiro=" + tramoRetiro.getCupos() + " dispo "  + tramoRetiro.getCuposdisponibles() +
                ", cobertura=" + cobertura +
                '}';
    }

    @Override
    public int compareTo(ModeloVistaTramo modeloVistaTramo) {
        return this.getTramoRetiro().getHorainicio().compareTo(modeloVistaTramo.getTramoRetiro().getHorainicio());
    }
}
