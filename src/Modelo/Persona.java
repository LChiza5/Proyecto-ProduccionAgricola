/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author sebas
 */
public class Persona {
    private String id;
    private String nombre;
    private String telefono;
    private String correo;
    private EnuRol rol;

    public Persona(String id, String nombre, String telefono, String correo, EnuRol rol) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.rol = rol;
    }

    public String getId() {return id;}
    public String getNombre() {return nombre;}
    public String getTelefono() {return telefono;}
    public String getCorreo() {return correo;}public EnuRol getRol() {return rol;}

    public void setTelefono(String telefono) {this.telefono = telefono;}
    public void setCorreo(String correo) {this.correo = correo;}
}
