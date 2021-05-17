package Modelo;

import java.io.Serializable;

public class ModeloVistaDireccion implements Serializable {

    private Direccion direccion;
    private boolean eliminar = false;
    private boolean vereporte = true;
    private boolean eliminar2 = false;

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

    public boolean isVereporte() {
        return vereporte;
    }

    public void setVereporte(boolean vereporte) {
        this.vereporte = vereporte;
    }

    public boolean isEliminar2() {
        return eliminar2;
    }

    public void setEliminar2(boolean eliminar2) {
        this.eliminar2 = eliminar2;
    }
}
