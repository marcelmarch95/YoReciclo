package Modelo;

import java.io.Serializable;

public class ModeloVistaCobertura implements Serializable {

    private Cobertura cobertura;

    public ModeloVistaCobertura(){

    }

    public Cobertura getCobertura() {
        return cobertura;
    }

    public void setCobertura(Cobertura cobertura) {
        this.cobertura = cobertura;
    }
}
