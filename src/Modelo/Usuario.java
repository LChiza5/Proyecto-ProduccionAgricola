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
    private String contrasenaHash;

        public Usuario( String id, String nombre, String contrasenaHash,String telefono, String correo, EnuRol rol) {
        super(id, nombre, telefono, correo, rol);
        this.contrasenaHash = contrasenaHash;
    }


    public String getContrasenaHash() { 
        return contrasenaHash; 
    }
    public void setContrasena(String contrasena) { 
        this.contrasenaHash = contrasena; 
    }
}
