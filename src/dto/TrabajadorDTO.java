/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import Modelo.EnuRol;

/**
 *
 * @author Luisk
 */
public class TrabajadorDTO {
    private String id;
    private String nombre;
    private String telefono;
    private String correo;
    private EnuRol rol;
    private String puesto;
    private String horarios;
    private double salario;

    public TrabajadorDTO() {
    }

    public TrabajadorDTO(String id, String nombre, String telefono, String correo, EnuRol rol, 
                         String puesto, String horarios, double salario) {

        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.rol = rol;
        this.puesto = puesto;
        this.horarios = horarios;
        this.salario = salario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public EnuRol getRol() {
        return rol;
    }

    public void setRol(EnuRol rol) {
        this.rol = rol;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
