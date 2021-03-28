package Modelo;

import java.io.Serializable;

public class ModeloVistaDireccion implements Serializable {

    private Direccion direccion;
    private boolean eliminar = false;

    public ModeloVistaDireccion(){

    }

    public boolean isEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
