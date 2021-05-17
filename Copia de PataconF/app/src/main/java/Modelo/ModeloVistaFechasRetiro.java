package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ModeloVistaFechasRetiro implements Serializable, Comparable<ModeloVistaFechasRetiro> {

    private ArrayList<TramoRetiro> tramos;
    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ModeloVistaFechasRetiro(){
        this.tramos = new ArrayList<>();
    }

    public ArrayList<TramoRetiro> getTramos() {
        return tramos;
    }

    public void setTramos(ArrayList<TramoRetiro> tramos) {
        this.tramos = tramos;
    }

    @Override
    public int compareTo(ModeloVistaFechasRetiro modeloVistaFechasRetiro) {
        Date d1 = new Date(modeloVistaFechasRetiro.getFecha());
        Date d2 = new Date(this.getFecha());
        return d2.compareTo(d1);
    }
}
