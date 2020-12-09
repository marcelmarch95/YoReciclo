package Modelo;

import java.io.Serializable;

public class ModeloVistaReporte implements Serializable {

    private Reporte reporte;
    private Punto punto;

    public ModeloVistaReporte(){

    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public Punto getPunto() {
        return punto;
    }

    public void setPunto(Punto punto) {
        this.punto = punto;
    }
}
