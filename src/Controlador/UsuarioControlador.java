/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Util.HashUtil;
import Vista.FrmUsuarios;
import dto.UsuarioDTO;
import java.util.List;
import javax.swing.JOptionPane;
import servicio.UsuarioServicio;

/**
 *
 * @author sebas
 */
public class UsuarioControlador {
    private final UsuarioServicio servicio;
    private final FrmUsuarios vista;

    public UsuarioControlador(UsuarioServicio servicio, FrmUsuarios vista) {
        this.servicio = servicio;
        this.vista = vista;
        
        inicializarEventos();
        listarTodos();
    }
    
    private void inicializarEventos() {
        vista.getBtnAgregar().addActionListener(e -> agregar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());
        
    }
    
    private void agregar(){
        try {
            UsuarioDTO dto = leerUsuarioDesdeFormulario();
            servicio.agregarUsuario(dto);

            mostrarMensaje("Trabajador registrado correctamente.");
            listarTodos();
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al guardar el Usuario: " + ex.getMessage());
        }
    }
    
    
    private void actualizar(){
        try {
        UsuarioDTO dto = leerUsuarioDesdeFormulario();
        servicio.actualizarUsuario(dto);

        mostrarMensaje("Usuario actualizado correctamente");
        listarTodos();
        limpiar();
        } catch (ValidacionException | DAOException ex) {
        mostrarError("Error al actualizar usuario: " + ex.getMessage());
        }
    }
    
    private void eliminar(){
        String id = vista.getTxtId().getText();
            int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el Usuario llamado: " + id + " ?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarUsuario(id);
                mostrarMensaje("Trabajador eliminado correctamente.");
                listarTodos();
                limpiar();
                
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el trabajador: " + ex.getMessage());
            }
        }
    }
    
    private void abrirDialogoBusqueda(){
    
    }
    
    private void limpiar(){
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtCorreo().setText("");
        vista.getTxtContrasena().setText("");
        vista.getCmbRol().setSelectedIndex(1);
    }
    
       public void iniciar() {
        vista.setVisible(true);
        listarTodos();
        
    }
    
    // ======================= Métodos de apoyo =======================
    
    private UsuarioDTO leerUsuarioDesdeFormulario(){
            UsuarioDTO dto = new UsuarioDTO();
        
        dto.setId(vista.getTxtId().getText());
        dto.setNombre(vista.getTxtNombre().getText());
        dto.setTelefono(vista.getTxtTelefono().getText());
        dto.setCorreo(vista.getTxtCorreo().getText());
        dto.setContrasenaHash(HashUtil.sha256(vista.getTxtContrasena().getText()));
            if (vista.getCmbRol().getSelectedItem() == null) {
                mostrarError("Debe seleccionar un rol");
            }
        dto.setRol(vista.getCmbRol().getSelectedItem().toString());
        return dto;
    }
    
        private void listarTodos() {
        try {
            List<UsuarioDTO> lista = servicio.listarTodos();
            vista.actualizarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar los trabajadores: " + ex.getMessage());
        }
    }
    
        private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
