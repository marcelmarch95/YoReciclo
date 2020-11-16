package Modelo;

import java.io.Serializable;

public class ModeloVistaProducto implements Serializable {

    private String nombre;
    private String codigo;
    private String categoria;
    private String descripcion;
    private String foto;
    private int precio;
    private int stock;

    public ModeloVistaProducto(){

    }

    public ModeloVistaProducto(String nombre, String categoria, String foto, int precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.foto = foto;
        this.precio = precio;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
