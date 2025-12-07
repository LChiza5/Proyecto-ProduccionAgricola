/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDate;

/**
 *
 * @author Luisk
 */
public class ProduccionDTO {
    private int id;
    private LocalDate fecha;
    private int cantProducto;
    private int calidad;
    private int productividad; // No va en BD
    private String destino;
    private String idCultivo;

    public ProduccionDTO() {
    }

    public ProduccionDTO(int id, LocalDate fecha, int cantProducto, int calidad, int productividad, String destino, String idCultivo) {
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

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantProducto() {
        return cantProducto;
    }

    public void setCantProducto(int cantProducto) {
        this.cantProducto = cantProducto;
    }

    public int getCalidad() {
        return calidad;
    }

    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }

    public int getProductividad() {
        return productividad;
    }

    public void setProductividad(int productividad) {
        this.productividad = productividad;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(String idCultivo) {
        this.idCultivo = idCultivo;
    }
}
