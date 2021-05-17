package Modelo;

import java.io.Serializable;

public class ModeloVistaRecicladora implements Serializable {

    private Recicladora recicladora;

    public ModeloVistaRecicladora(){

    }

    public Recicladora getRecicladora() {
        return recicladora;
    }

    public void setRecicladora(Recicladora recicladora) {
        this.recicladora = recicladora;
    }
}
