package Modelo;

public class ModeloOpcionesProducto {


    private String accion;
    private int foto;

    public ModeloOpcionesProducto(){

    }

    public ModeloOpcionesProducto(String accion,  int foto) {
        this.accion = accion;
        this.foto = foto;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
