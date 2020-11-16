package Modelo;

public class ModeloVistaDashboard {

    private String nombre;
    private String artista;
    private int numero;
    private int imagen;

    public ModeloVistaDashboard(){

    }

    public ModeloVistaDashboard(String nombre, String artista, int imagen, int numero) {
        this.nombre = nombre;
        this.artista = artista;
        this.imagen = imagen;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
