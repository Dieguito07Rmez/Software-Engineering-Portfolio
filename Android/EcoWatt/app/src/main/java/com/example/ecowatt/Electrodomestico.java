package com.example.ecowatt;

public class Electrodomestico {

    private String tipo;
    private String marca;
    private String voltaje;
    private String potencia;
    private String cantidad;
    private String horasUso;


    // ==========================================
    // CONSTRUCTOR VACÍO
    // ==========================================

    public Electrodomestico() {
        // Necesario para Firebase Realtime Database
    }


    // ==========================================
    // CONSTRUCTOR COMPLETO
    // ==========================================

    public Electrodomestico(
            String tipo,
            String marca,
            String voltaje,
            String potencia,
            String cantidad,
            String horasUso
    ) {

        this.tipo = tipo;
        this.marca = marca;
        this.voltaje = voltaje;
        this.potencia = potencia;
        this.cantidad = cantidad;
        this.horasUso = horasUso;
    }


    // ==========================================
    // GETTERS
    // ==========================================

    public String getTipo() {
        return tipo;
    }


    public String getMarca() {
        return marca;
    }


    public String getVoltaje() {
        return voltaje;
    }


    public String getPotencia() {
        return potencia;
    }


    public String getCantidad() {
        return cantidad;
    }


    public String getHorasUso() {
        return horasUso;
    }


    // ==========================================
    // SETTERS
    // ==========================================

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public void setMarca(String marca) {
        this.marca = marca;
    }


    public void setVoltaje(String voltaje) {
        this.voltaje = voltaje;
    }


    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }


    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }


    public void setHorasUso(String horasUso) {
        this.horasUso = horasUso;
    }
}