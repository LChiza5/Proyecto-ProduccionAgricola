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
public class Almacenamiento {
    private int id;
    private String idProduccion; // o idCultivo, según el diseño
    private int cantidad;
    private LocalDate ingreso;
    private LocalDate egreso;

    public Almacenamiento(int id, String idProduccion, int cantidad, LocalDate ingreso, LocalDate egreso) {
        this.id = id;
        this.idProduccion = idProduccion;
        this.cantidad = cantidad;
        this.ingreso = ingreso;
        this.egreso = egreso;
    }

    public int getId() { 
        return id; 
    }
    public String getIdProduccion() { 
        return idProduccion; 
    }
    public int getCantidad() { 
        return cantidad; 
    }
    public LocalDate getIngreso() { 
        return ingreso; 
    }
    public LocalDate getEgreso() { 
        return egreso; 
    }

    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
    }
    public void setEgreso(LocalDate egreso) { 
        this.egreso = egreso; 
    }
    
}
