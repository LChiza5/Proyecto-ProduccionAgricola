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
    private int productividad; // No va en BD
    private String destino;
    private String idCultivo;

    public Produccion(String id, LocalDate fecha, int cantProducto, int calidad, int productividad, String destino, String idCultivo) {
        this.id = id;
        this.fecha = fecha;
        this.cantProducto = cantProducto;
        this.calidad = calidad;
        this.productividad = productividad;
        this.destino = destino;
        this.idCultivo = idCultivo;
    }

    public String getId() { 
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
    public String getIdCultivo() { 
        return idCultivo; 
    }

    public void setDestino(String destino) { 
        this.destino = destino; 
    }
    public void setIdCultivo(String idCultivo) { 
        this.idCultivo = idCultivo; 
    }
}
