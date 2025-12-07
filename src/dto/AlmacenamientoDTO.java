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
public class AlmacenamientoDTO {
    private int id;
    private String idProduccion;
    private int cantidad;
    private LocalDate ingreso;
    private LocalDate egreso;

    public AlmacenamientoDTO() {
    }

    public AlmacenamientoDTO(int id, String idProduccion, int cantidad, LocalDate ingreso, LocalDate egreso) {
        this.id = id;
        this.idProduccion = idProduccion;
        this.cantidad = cantidad;
        this.ingreso = ingreso;
        this.egreso = egreso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(String idProduccion) {
        this.idProduccion = idProduccion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getIngreso() {
        return ingreso;
    }

    public void setIngreso(LocalDate ingreso) {
        this.ingreso = ingreso;
    }

    public LocalDate getEgreso() {
        return egreso;
    }

    public void setEgreso(LocalDate egreso) {
        this.egreso = egreso;
    }
}
