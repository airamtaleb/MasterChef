package com.politecnico.masterchef;

public class Evento {
    String idEvento, nombre, fecha, hora, estado, descripcion, lugar;

    public Evento() {
    }

    public Evento(String idEvento, String nombre, String fecha, String hora, String estado, String descripcion, String lugar) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.descripcion = descripcion;
        this.lugar = lugar;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
