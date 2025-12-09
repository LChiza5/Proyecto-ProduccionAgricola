/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author sebas
 */
public class Usuario extends Persona {
    private String contrasenaHash;

    public Usuario(String id, String nombre,
                   String telefono, String correo,
                   String contrasenaHash, EnuRol rol) {

        super(id, nombre, telefono, correo, rol);
        this.contrasenaHash = contrasenaHash;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }
}
