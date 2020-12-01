package Modelo;

import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Notificacion implements Serializable, Comparable<Notificacion> {
    private int imagen;
    private String idViaje;
    private String estado;
    private String idChofer;
    private String idProductor;
    private String origen;
    private String destino;
    private String origenTexto;
    private String destinoTexto;
    private String ubicacionActual;
    private String patente;
    private String titulo;
    private String detalle;
    private String fecha;
    private String hora;
    private String carro;
    private int planned;
    private boolean not;
    private Calendar fechaD;


    public Notificacion(String idViaje, int imagen, String titulo, String detalle, String fecha, String hora, boolean not)

    {
        this.idViaje = idViaje;
        this.imagen = imagen;
        this.titulo = titulo;
        this.detalle = detalle;
        this.fecha = fecha;
        this.hora = hora;
        this.not = not;
    }

    public Notificacion(String idViaje, int imagen,  String estado, String idChofer, String idProductor, String origen, String destino, String origenTexto, String destinoTexto, String ubicacionActual, String patente, int planned, String fecha, String hora, String carro) {
        this.idViaje = idViaje;
        this.imagen = imagen;
        this.idViaje = idViaje;
        this.estado = estado;
        this.idChofer = idChofer;
        this.idProductor = idProductor;
        this.origen = origen;
        this.destino = destino;
        this.origenTexto = origenTexto;
        this.destinoTexto = destinoTexto;
        this.ubicacionActual = ubicacionActual;
        this.patente = patente;
        this.planned = planned;
        this.fecha = fecha;
        this.hora = hora;
        this.carro = carro;
        this.not = false;
    }

    public String getId() {
        return idViaje;
    }

    public void setId(String id) {
        this.idViaje = id;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(String idViaje) {
        this.idViaje = idViaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdChofer() {
        return idChofer;
    }

    public void setIdChofer(String idChofer) {
        this.idChofer = idChofer;
    }

    public String getIdProductor() {
        return idProductor;
    }

    public void setIdProductor(String idProductor) {
        this.idProductor = idProductor;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getOrigenTexto() {
        return origenTexto;
    }

    public void setOrigenTexto(String origenTexto) {
        this.origenTexto = origenTexto;
    }

    public String getDestinoTexto() {
        return destinoTexto;
    }

    public void setDestinoTexto(String destinoTexto) {
        this.destinoTexto = destinoTexto;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public int getPlanned() {
        return planned;
    }

    public void setPlanned(int planned) {
        this.planned = planned;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Calendar getFechaD() {
        return fechaD;
    }

    public void setFechaD(Calendar fechaD) {
        this.fechaD = fechaD;
    }

    @Override
    public int compareTo(Notificacion o) {
        try {
            if (this.fechaD.after(o.getFechaD())){
                return -1;
            }
            else if (this.fechaD.before(o.getFechaD())) {
                return 1;
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setearFecha(){
        Calendar cal1 = Calendar.getInstance();

        StringTokenizer tokens1 = new StringTokenizer(this.getFecha(), "/");
        int contador1 = 0;
        while(tokens1.hasMoreTokens()){
            if (contador1==0){
                cal1.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tokens1.nextToken()));
            }
            if (contador1==1){
                cal1.set(Calendar.MONTH, Integer.valueOf(tokens1.nextToken()));
            }
            if (contador1==2){
                cal1.set(Calendar.YEAR, Integer.valueOf(tokens1.nextToken()));
            }
            contador1++;
        }

        StringTokenizer tokens2 = new StringTokenizer(this.getHora(), ":");
        int contador2 = 0;
        while(tokens2.hasMoreTokens()){
            if (contador2==0){
                cal1.set(Calendar.HOUR, Integer.valueOf(tokens2.nextToken()));
            }
            if (contador2==1){
                cal1.set(Calendar.MINUTE, Integer.valueOf(tokens2.nextToken()));
            }
            contador2++;
        }

        this.fechaD = cal1;
        System.out.println("Setie fecha: " + fechaD.getTime().toLocaleString());
    }
}