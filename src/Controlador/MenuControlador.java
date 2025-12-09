/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.Usuario;
import Vista.FrmPrincipal;
import Vista.FrmCultivos;
import Vista.FrmTrabajadores;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import servicio.CultivoServicio;
import servicio.TrabajadorServicio;
/**
 *
 * @author ilope
 */
public class MenuControlador {
    private final Usuario usuarioLogueado;
    private final FrmPrincipal vista;
    private final CultivoServicio cultivoServicio;
    private final TrabajadorServicio trabajadorServicio;

    public MenuControlador(Usuario usuarioLogueado,
                           FrmPrincipal vista,
                           CultivoServicio cultivoServicio,
                           TrabajadorServicio trabajadorServicio) {
                            this.usuarioLogueado = usuarioLogueado;
                            this.vista = vista;
                            this.cultivoServicio = cultivoServicio;
                            this.trabajadorServicio = trabajadorServicio;
                            inicializarEventos();
    }

    private void inicializarEventos() {

        // === CULTIVOS ===
        vista.getBtnCultivos().addActionListener(e -> {
            
            JInternalFrame abierto = buscarFrameAbierto(FrmCultivos.class);
            if (abierto != null) {
                try { abierto.setSelected(true); } catch (java.beans.PropertyVetoException ex) {}
                return;
            }

            FrmCultivos frm = new FrmCultivos();              // vista
            new CultivoControlador(cultivoServicio, frm);     // controlador
            abrirEnDesktop(frm);                              
        });

        // === TRABAJADORES ===
        vista.getBtnTrabajadores().addActionListener(e -> {
            JInternalFrame abierto = buscarFrameAbierto(FrmTrabajadores.class);
            if (abierto != null) {
                try { abierto.setSelected(true); } catch (java.beans.PropertyVetoException ex) {}
                return;
            }

            FrmTrabajadores frm = new FrmTrabajadores();              
            new TrabajadorControlador(trabajadorServicio, frm);       
            abrirEnDesktop(frm);
        });

        // === Producción, Almacenamiento, Usuarios los dejas para después ===

        vista.getBtnCerrarSesion().addActionListener(e -> {
            int opc = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Desea cerrar la sesión?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (opc == JOptionPane.YES_OPTION) {
                vista.dispose();
                new Controlador.LoginControladorFactory().mostrarLogin();
            }
        });
    }

    // ---- Métodos de apoyo ----

    private void abrirEnDesktop(JInternalFrame frame) {

        vista.getjDesktopPane1().add(frame);

        // Centrar dentro del desktop
        int x = (vista.getjDesktopPane1().getWidth() - frame.getWidth()) / 2;
        int y = (vista.getjDesktopPane1().getHeight() - frame.getHeight()) / 2;
        frame.setLocation(Math.max(x, 0), Math.max(y, 0));

        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ex) {
            // lo ignoramos
        }
    }
    private JInternalFrame buscarFrameAbierto(Class<?> tipo) {
        for (JInternalFrame frame : vista.getjDesktopPane1().getAllFrames()) {
            if (tipo.isInstance(frame)) {
                return frame;
            }
        }
        return null;
    }
}
