package Modelo;

public class Reporte {

    private String foto;
    private String idPunto;
    private String comentarios;
    private String motivo;
    private String fecha;
    private String hora;
    private String idReporte;
    private String idGenerador;

    public Reporte(){

    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(String idPunto) {
        this.idPunto = idPunto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getIdGenerador() {
        return idGenerador;
    }

    public void setIdGenerador(String idGenerador) {
        this.idGenerador = idGenerador;
    }
}
