/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.Usuario;
import Vista.FrmAlmacenamiento;
import Vista.FrmPrincipal;
import Vista.FrmCultivos;
import Vista.FrmProduccion;
import Vista.FrmTrabajadores;
import Vista.FrmUsuarios;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import servicio.AlmacenamientoServicio;
import servicio.CultivoServicio;
import servicio.ProduccionServicio;
import servicio.TrabajadorServicio;
import servicio.UsuarioServicio;
/**
 *
 * @author ilope
 */
public class MenuControlador {

    private final Usuario usuarioLogueado;
    private final FrmPrincipal vista;
    private final CultivoServicio cultivoServicio;
    private final TrabajadorServicio trabajadorServicio;
    private final AlmacenamientoServicio almacenamientoServicio;
    private final UsuarioServicio usuarioServicio;
    
    private FrmAlmacenamiento frmAlmacenamiento;
    private final ProduccionServicio produccionServicio;
    private FrmProduccion frmProduccion;

    public MenuControlador(Usuario usuarioLogueado,
                       FrmPrincipal vista,
                       CultivoServicio cultivoServicio,
                       TrabajadorServicio trabajadorServicio,
                       AlmacenamientoServicio almacenamientoServicio,
                       ProduccionServicio produccionServicio,
                       UsuarioServicio usuarioServicio) {
                         this.usuarioLogueado = usuarioLogueado;
                         this.vista = vista;
                         this.cultivoServicio = cultivoServicio;
                         this.trabajadorServicio = trabajadorServicio;
                         this.almacenamientoServicio = almacenamientoServicio;
                         this.produccionServicio = produccionServicio;
                         this.usuarioServicio = usuarioServicio;
    inicializarEventos();
}

    

    private void inicializarEventos() {
        vista.getBtnAlmacen().addActionListener(e -> abrirAlmacenamiento());
        vista.getBtnProduccion().addActionListener(e -> abrirProduccion());
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


        // === Producción, Almacenamiento para después ===
        
        
        // === USUARIOS ===
        vista.getBtnUsuarios().addActionListener(e -> {
        JInternalFrame abierto = buscarFrameAbierto(FrmUsuarios.class);
            if (abierto != null) {
                try { abierto.setSelected(true); } catch (java.beans.PropertyVetoException ex) {}
                return;
            }

            FrmUsuarios frm = new FrmUsuarios();              
            UsuarioControlador cntrl = new UsuarioControlador(usuarioServicio, frm);       
            abrirEnDesktop(frm);
            cntrl.iniciar();
        });

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
    private void abrirAlmacenamiento() {
    if (frmAlmacenamiento == null || frmAlmacenamiento.isClosed()) {
        frmAlmacenamiento = new FrmAlmacenamiento();
        AlmacenamientoControlador ctrl =
                new AlmacenamientoControlador(almacenamientoServicio, frmAlmacenamiento);

        vista.getjDesktopPane1().add(frmAlmacenamiento); 
        ctrl.iniciar();
    } else {
        frmAlmacenamiento.toFront();
    }
}
     private void abrirProduccion() {
    if (frmProduccion == null || frmProduccion.isClosed()) {
        frmProduccion = new FrmProduccion();
        ProduccionControlador ctrl =
                new ProduccionControlador(produccionServicio, cultivoServicio, frmProduccion);

        vista.getjDesktopPane1().add(frmProduccion); 
        ctrl.iniciar();
    } else {
        frmProduccion.toFront();
    }
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
