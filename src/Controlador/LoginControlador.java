/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Modelo.Usuario;
import Vista.FrmLogin;
import Vista.FrmPrincipal;
import javax.swing.JOptionPane;
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
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnIngresar().addActionListener(e -> intentarLogin());
        
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    private void intentarLogin() {
        String id = vista.getTxtId().getText().trim();
        String password = new String(vista.getTxtPassword().getText());

        try {
            Usuario usuario = servicio.iniciarSesion(id, password);

            JOptionPane.showMessageDialog(vista,
                    "Bienvenido " + usuario.getNombre() + " (" + usuario.getRol() + ")");

            FrmPrincipal menu = new FrmPrincipal(usuario);
            menu.setVisible(true);
            vista.dispose();

        } catch (ValidacionException | DAOException ex) {
            JOptionPane.showMessageDialog(vista,
                    ex.getMessage(),
                    "Error de login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
}
