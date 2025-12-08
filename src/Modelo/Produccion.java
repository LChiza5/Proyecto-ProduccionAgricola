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
public class Produccion {
    private int id;
    private LocalDate fecha;
    private int cantProducto;
    private int calidad;
    private int productividad; // No va en BD
    private String destino;
    private int idCultivo;

    public Produccion(int id, LocalDate fecha, int cantProducto, int calidad, int productividad, String destino, int idCultivo) {
        this.id = id;
        this.fecha = fecha;
        this.cantProducto = cantProducto;
        this.calidad = calidad;
        this.productividad = productividad;
        this.destino = destino;
        this.idCultivo = idCultivo;
    }

    public int getId() { 
        return id; 
    }
    public LocalDate getFecha() { 
        return fecha; 
    }
    public int getCantProducto() { 
        return cantProducto; 
    }
    public int getCalidad() { 
        return calidad; 
    }
    public int getProductividad() { 
        return productividad; 
    }
    public String getDestino() { 
        return destino; 
    }
    public int getIdCultivo() { 
        return idCultivo; 
    }

    public void setDestino(String destino) { 
        this.destino = destino; 
    }
    public void setIdCultivo(int idCultivo) { 
        this.idCultivo = idCultivo; 
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setCantProducto(int cantProducto) {
        this.cantProducto = cantProducto;
    }

    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }

    public void setProductividad(int productividad) {
        this.productividad = productividad;
    }
    
}
