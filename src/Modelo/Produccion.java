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
    private String id;
    private LocalDate fecha;
    private int cantProducto;
    private int calidad;
    private int productividad;
    private String destino;

    public Produccion(String id, LocalDate fecha, int cantProducto, int calidad, int productividad, String destino) {
        this.id = id;
        this.fecha = fecha;
        this.cantProducto = cantProducto;
        this.calidad = calidad;
        this.productividad = productividad;
        this.destino = destino;
    }

    public String getId() {return id;}
    public LocalDate getFecha() {return fecha;}
    public int getCantProducto() {return cantProducto;}
    public int getCalidad() {return calidad;}
    public int getProductividad() {return productividad;}
    public String getDestino() {return destino;}
    
    public void setDestino(String destino) {this.destino = destino;}
}
