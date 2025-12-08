/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author sebas
 */
public class Usuario extends Persona{
    private String contrasena;

        public Usuario(String contrasena, String id, String nombre,String telefono, String correo, EnuRol rol) {
        super(id, nombre, telefono, correo, rol);
        this.contrasena = contrasena;
    }


    public String getContrasena() { 
        return contrasena; 
    }
    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena; 
    }
}
