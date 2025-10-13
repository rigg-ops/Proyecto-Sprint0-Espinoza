package com.espinoza.proyectobiometriaappbeacons;

// Clase que representa una medida obtenida del beacon (fake o real)
public class Medida {
    private String tipo;     // "CO2" o "Temperatura"
    private double valor;    // Valor medido o simulado
    private long timestamp;  // Momento de captura (en milisegundos)

    public Medida(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTipo() { return tipo; }
    public double getValor() { return valor; }
    public long getTimestamp() { return timestamp; }
}