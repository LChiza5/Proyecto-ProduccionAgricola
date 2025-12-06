/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author sebas
 */
public class Admin extends Persona{
    private String contraseña;

    public Admin(String contraseña, String id, String nombre, String telefono, String correo, EnuRol rol) {
        super(id, nombre, telefono, correo, rol);
        this.contraseña = contraseña;
    }
}
