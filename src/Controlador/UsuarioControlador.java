/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Util.HashUtil;
import Vista.DlgUsuariosBusqueda;
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
    }

    private void inicializarEventos() {
        vista.getBtnAgregar().addActionListener(e -> agregar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());
  
    }

    

    private void agregar() {
        try {
            UsuarioDTO dto = leerUsuarioDesdeFormulario();
            servicio.agregarUsuario(dto);

            mostrarMensaje("Usuario registrado correctamente.");
            
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al guardar el usuario: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            UsuarioDTO dto = leerUsuarioDesdeFormulario();
            servicio.actualizarUsuario(dto);

            mostrarMensaje("Usuario actualizado correctamente.");
            
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al actualizar usuario: " + ex.getMessage());
        }
    }

    private void eliminar() {
        String id = vista.getTxtId().getText().trim();

        if (id.isEmpty()) {
            mostrarError("Debe indicar el ID/cédula del usuario a eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el usuario con ID: " + id + " ?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarUsuario(id);
                mostrarMensaje("Usuario eliminado correctamente.");
                
                limpiar();

            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el usuario: " + ex.getMessage());
            }
        }
    }

    private void limpiar() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtCorreo().setText("");
        vista.getTxtContrasena().setText("");
        if (vista.getCmbRol().getItemCount() > 0) {
            vista.getCmbRol().setSelectedIndex(0);
        }
    }

    public void iniciar() {
        vista.setVisible(true);
        
    }

    

    private void abrirDialogoBusqueda() {
        DlgUsuariosBusqueda dialogo = new DlgUsuariosBusqueda(vista, true);
        UsuarioBusquedaControlador ctrlBusqueda =
                new UsuarioBusquedaControlador(servicio, dialogo, this);
        ctrlBusqueda.mostrar();
    }

    
    public void cargarUsuarioDesdeBusqueda(UsuarioDTO dto) {
        vista.getTxtId().setText(dto.getId());
        vista.getTxtNombre().setText(dto.getNombre());
        vista.getTxtTelefono().setText(dto.getTelefono());
        vista.getTxtCorreo().setText(dto.getCorreo());
        vista.getCmbRol().setSelectedItem(dto.getRol());
        vista.getTxtContrasena().setText("");
    }

    // ======================= Métodos de apoyo =======================

    private UsuarioDTO leerUsuarioDesdeFormulario() throws ValidacionException {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(vista.getTxtId().getText().trim());
        dto.setNombre(vista.getTxtNombre().getText().trim());
        dto.setTelefono(vista.getTxtTelefono().getText().trim());
        dto.setCorreo(vista.getTxtCorreo().getText().trim());

        Object rolSeleccionado = vista.getCmbRol().getSelectedItem();
        if (rolSeleccionado == null || rolSeleccionado.toString().isBlank()) {
            throw new ValidacionException("Debe seleccionar un rol para el usuario.");
        }
        dto.setRol(rolSeleccionado.toString());

        String contrasena = vista.getTxtContrasena().getText();
        if (contrasena != null && !contrasena.isBlank()) {
            dto.setContrasenaHash(HashUtil.sha256(contrasena));
        }

        return dto;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}