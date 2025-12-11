/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import dto.UsuarioDTO;
import Excepciones.DAOException;
import servicio.UsuarioServicio;
import Vista.DlgUsuariosBusqueda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
/**
 *
 * @author ilope
 */
public class UsuarioBusquedaControlador {
    private final UsuarioServicio servicio;
    private final DlgUsuariosBusqueda vista;
    private final UsuarioControlador controladorPrincipal;

    public UsuarioBusquedaControlador(UsuarioServicio servicio,
                                      DlgUsuariosBusqueda vista,
                                      UsuarioControlador controladorPrincipal) {
        this.servicio = servicio;
        this.vista = vista;
        this.controladorPrincipal = controladorPrincipal;

        configurarTabla();
        inicializarEventos();
        cargarTodos();
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Nombre", "Teléfono", "Correo", "Rol"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        vista.getTblUsuarios().setModel(modelo);
    }

    private void inicializarEventos() {
        vista.getBtnBuscar().addActionListener(e -> buscarPorFiltros());
        vista.getBtnSeleccionar().addActionListener(e -> seleccionarUsuario());
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }

    public void mostrar() {
        vista.setVisible(true);
    }

    private void cargarTodos() {
        try {
            List<UsuarioDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar usuarios: " + ex.getMessage());
        }
    }

    private void buscarPorFiltros() {
        String id = vista.getTxtFiltroId().getText().trim();
        String nombre = vista.getTxtFiltroNombre().getText().trim();
        String rol = (String) vista.getCmbFiltroRol().getSelectedItem();

        try {
            List<UsuarioDTO> lista = servicio.buscarConFiltros(
                    id.isEmpty() ? null : id,
                    nombre.isEmpty() ? null : nombre,
                    (rol == null || rol.equalsIgnoreCase("Todos")) ? null : rol
            );
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al buscar usuarios: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<UsuarioDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblUsuarios().getModel();
        modelo.setRowCount(0);

        for (UsuarioDTO u : lista) {
            modelo.addRow(new Object[]{
                    u.getId(),
                    u.getNombre(),
                    u.getTelefono(),
                    u.getCorreo(),
                    u.getRol()
            });
        }
    }

    private void seleccionarUsuario() {
        int fila = vista.getTblUsuarios().getSelectedRow();
        if (fila == -1) {
            mostrarError("Debe seleccionar un usuario de la tabla.");
            return;
        }

        String idSeleccionado = vista.getTblUsuarios()
                                     .getValueAt(fila, 0).toString();

        try {
            
            List<UsuarioDTO> lista = servicio.listarTodos();
            UsuarioDTO seleccionado = lista.stream()
                    .filter(u -> idSeleccionado.equals(u.getId()))
                    .findFirst()
                    .orElse(null);

            if (seleccionado == null) {
                mostrarError("No se pudo encontrar el usuario seleccionado.");
                return;
            }

            controladorPrincipal.cargarUsuarioDesdeBusqueda(seleccionado);
            vista.dispose();

        } catch (DAOException ex) {
            mostrarError("Error al obtener el usuario seleccionado: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}