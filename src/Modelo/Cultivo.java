/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;

/**
 *
 * @author sebas
 */
public class Cultivo {
    private String id;
    private String nombre;
    private String tipo;
    private double areaSembrada;
    private String estado;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosecha;

    public Cultivo(String id, String nombre, String tipo, double areaSembrada, String estado, LocalDate fechaSiembra, LocalDate fechaCosecha) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.areaSembrada = areaSembrada;
        this.estado = estado;
        this.fechaSiembra = fechaSiembra;
        this.fechaCosecha = fechaCosecha;
    }

    public String getId() { 
        return id; 
    }
    public String getNombre() { 
        return nombre; 
    }
    public String getTipo() { 
        return tipo; 
    }
    public double getAreaSembrada() { 
        return areaSembrada; 
    }
    public String getEstado() { 
        return estado; 
    }
    public LocalDate getFechaSiembra() { 
        return fechaSiembra; 
    }
    public LocalDate getFechaCosecha() { 
        return fechaCosecha; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
