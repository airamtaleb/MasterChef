package com.politecnico.masterchef;

import java.io.Serializable;

public class Votacion implements Serializable {

    private String nombre_equipo, id_juez, id_evento, presentacion, servicio, sabor, imagen, triptico;

    public Votacion() {
    }

    public Votacion(String nombre_equipo, String id_juez, String id_evento, String presentacion, String servicio, String sabor, String imagen, String triptico) {
        this.nombre_equipo = nombre_equipo;
        this.id_juez = id_juez;
        this.id_evento = id_evento;
        this.presentacion = presentacion;
        this.servicio = servicio;
        this.sabor = sabor;
        this.imagen = imagen;
        this.triptico = triptico;
    }

    public String getNombre_equipo() {
        return nombre_equipo;
    }

    public void setNombre_equipo(String nombre_equipo) {
        this.nombre_equipo = nombre_equipo;
    }

    public String getId_juez() {
        return id_juez;
    }

    public void setId_juez(String id_juez) {
        this.id_juez = id_juez;
    }

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTriptico() {
        return triptico;
    }

    public void setTriptico(String triptico) {
        this.triptico = triptico;
    }
}
