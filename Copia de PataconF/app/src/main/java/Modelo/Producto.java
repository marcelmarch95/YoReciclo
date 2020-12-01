package Modelo;

import java.io.Serializable;

public class Producto implements Serializable {

    private String nombre;
    private String codigo;
    private String categoria;
    private String descripcion;
    private String foto;
    private int precio;
    private int stock;
    private String comerciante;

    public Producto(){

    }

    public Producto(String nombre, String codigo, String categoria, String descripcion, String foto, int precio, int stock, String comerciante) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.foto = foto;
        this.precio = precio;
        this.stock = stock;
        this.comerciante = comerciante;
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

    public String getComerciante() {
        return comerciante;
    }

    public void setComerciante(String comerciante) {
        this.comerciante = comerciante;
    }
}
