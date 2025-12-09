/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Dao.UsuarioDAO;
import Dao.impl.UsuarioDAOImpl;
import Vista.FrmLogin;
import servicio.ValidarLoginServicio;

/**
 *
 * @author ilope
 */
public class LoginControladorFactory {
    public void mostrarLogin() {
        UsuarioDAO dao = new UsuarioDAOImpl();
        ValidarLoginServicio servicio = new ValidarLoginServicio(dao);
        FrmLogin vista = new FrmLogin();
        LoginControlador controlador = new LoginControlador(servicio, vista);
        controlador.iniciar();
    }
}
