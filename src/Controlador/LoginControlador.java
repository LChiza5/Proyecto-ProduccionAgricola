/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.FrmLogin;
import servicio.ValidarLoginServicio;

/**
 *
 * @author sebas
 */
public class LoginControlador {
    private final ValidarLoginServicio servicio;
    private final FrmLogin vista;

    public LoginControlador(ValidarLoginServicio servicio, FrmLogin vista) {
        this.servicio = servicio;
        this.vista = vista;
    }
    
    public boolean verificarLogin(String usuario, String contra){
        return servicio.validarLogIn(usuario, contra);
    }
    
    
    
}
