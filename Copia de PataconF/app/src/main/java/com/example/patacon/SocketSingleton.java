
package com.example.patacon;


import Modelo.Generador;
import Modelo.Usuario;

public class SocketSingleton {

    private static Usuario usuario;
    private static Generador generador;


    private static SocketSingleton ourInstance = new SocketSingleton();

    public static SocketSingleton getInstance() {
        return ourInstance;
    }

    private SocketSingleton() {
    }

    public static SocketSingleton getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(SocketSingleton ourInstance) {
        SocketSingleton.ourInstance = ourInstance;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        SocketSingleton.usuario = usuario;
    }

    public static Generador getGenerador() {
        return generador;
    }

    public static void setGenerador(Generador generador) {
        SocketSingleton.generador = generador;
    }
}