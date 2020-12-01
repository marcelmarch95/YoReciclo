package Modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Viaje implements Serializable, Comparable<Viaje> {
    private String id;
    private String estado;
    private String idChofer;
    private String idProductor;
    private String origen;
    private String destino;
    private String origenTexto;
    private String destinoTexto;
    private String ubicacionActual;
    private String patente;
    private String carro;
    private String fecha;
    private String hora;
    private String kg;
    private String envase;
    private Calendar fechaD;

    public Viaje(String id, String estado, String idChofer, String idProductor, String origen, String destino, String origenTexto, String destinoTexto, String ubicacionActual, String patente, String fecha, String hora, String carro, String envase, String kg) {
        this.id = id;
        this.estado = estado;
        this.idChofer = idChofer;
        this.idProductor = idProductor;
        this.origen = origen;
        this.destino = destino;
        this.origenTexto = origenTexto;
        this.destinoTexto = destinoTexto;
        this.ubicacionActual = ubicacionActual;
        this.patente = patente;
        this.fecha = fecha;
        this.hora = hora;
        this.carro = carro;
        this.envase = envase;
        this.kg = kg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
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

    public Calendar getFechaD() {
        return fechaD;
    }

    public void setFechaD(Calendar fechaD) {
        this.fechaD = fechaD;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
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

    public String getCarro() {
        return carro;
    }

    public void setCarro(String carro) {
        this.carro = carro;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getEnvase() {
        return envase;
    }

    public void setEnvase(String envase) {
        this.envase = envase;
    }

    @Override
    public int compareTo(Viaje o) {
        try {
            if (this.fechaD.after(o.getFechaD())){
                return 1;
            }
            else if (this.fechaD.before(o.getFechaD())) {
                return -1;
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

    @Override
    public String toString() {
        return "Viaje{" +
                "id='" + id + '\'' +
                ", estado='" + estado + '\'' +
                ", idChofer='" + idChofer + '\'' +
                ", idProductor='" + idProductor + '\'' +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", origenTexto='" + origenTexto + '\'' +
                ", destinoTexto='" + destinoTexto + '\'' +
                ", ubicacionActual='" + ubicacionActual + '\'' +
                ", patente='" + patente + '\'' +
                ", carro='" + carro + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", fechaD=" + fechaD +
                '}';
    }
}
