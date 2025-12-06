/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author sebas
 */
public class Trabajador extends Persona {
   private String puesto;
    private String horarios;
    private float salario;

    public Trabajador(String puesto, String horarios, float salario, String id, String nombre, String telefono, String correo, EnuRol rol) {
        super(id, nombre, telefono, correo, rol);
        this.puesto = puesto;
        this.horarios = horarios;
        this.salario = salario;
    }

    public String getPuesto() { 
        return puesto; 
    }
    public String getHorarios() { 
        return horarios; 
    }
    public float getSalario() { 
        return salario; 
    }

    public void setPuesto(String puesto) { 
        this.puesto = puesto; 
    }
    public void setHorarios(String horarios) { 
        this.horarios = horarios; 
    }
    public void setSalario(float salario) { 
        this.salario = salario; 
    }

    @Override
    public String toString() {
        return getNombre() + " - " + puesto;
    }
}
