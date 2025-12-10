/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import dto.TrabajadorDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Vista.DlgTrabajadoresBusqueda;
import servicio.TrabajadorServicio;
import Vista.FrmTrabajadores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
/**
 *
 * @author ilope
 */
public class TrabajadorControlador {
    private final TrabajadorServicio servicio;
    private final FrmTrabajadores vista;

    public TrabajadorControlador(TrabajadorServicio servicio, FrmTrabajadores vista) {
        this.servicio = servicio;
        this.vista = vista;
        inicializarEventos();
        
       
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardarTrabajador());
        vista.getBtnActualizar().addActionListener(e -> actualizarTrabajador());
        vista.getBtnEliminar().addActionListener(e -> eliminarTrabajador());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
        
    }

    public void iniciar() {
        vista.setVisible(true);
        listarTodos();
        
    }

    // ======================= Acciones de botones =======================

    private void guardarTrabajador() {
        try {
            TrabajadorDTO dto = leerTrabajadorDesdeFormulario();
            servicio.crearTrabajador(dto);

            

            mostrarMensaje("Trabajador registrado correctamente.");
            listarTodos();
            limpiarFormulario();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al guardar el trabajador: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            mostrarError("Formato de salario inválido: " + ex.getMessage());
        }
    }

    private void actualizarTrabajador() {
        try {
            TrabajadorDTO dto = leerTrabajadorDesdeFormulario();
            servicio.actualizarTrabajador(dto);

            

            mostrarMensaje("Trabajador actualizado correctamente.");
            listarTodos();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al actualizar el trabajador: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            mostrarError("Formato de salario inválido: " + ex.getMessage());
        }
    }

    private void eliminarTrabajador() {
        String id = vista.getTxtId().getText().trim();
        if (id.isBlank()) {
            mostrarError("Debe ingresar la cédula/ID del trabajador a eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el trabajador con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarTrabajador(id);
                mostrarMensaje("Trabajador eliminado correctamente.");
                listarTodos();
                limpiarFormulario();
                
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el trabajador: " + ex.getMessage());
            }
        }
    }

    private void abrirDialogoBusqueda() {
    // Obtener el Frame padre a partir del JInternalFrame
    java.awt.Frame parent = (java.awt.Frame) SwingUtilities.getWindowAncestor(vista);

    DlgTrabajadoresBusqueda dialogo = new DlgTrabajadoresBusqueda(parent, true);

    TrabajadorBusquedaControlador ctrlBusqueda =
            new TrabajadorBusquedaControlador(servicio, dialogo, this);

    ctrlBusqueda.mostrar();
}

    private void listarTodos() {
        try {
            List<TrabajadorDTO> lista = servicio.listarTodos();
            
        } catch (DAOException ex) {
            mostrarError("Error al listar los trabajadores: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtCorreo().setText("");
        vista.getCmbPuesto().setSelectedIndex(-1);
        vista.getTxtHorarios().setText("");
        vista.getFtxtSalario().setValue(-1);
        
       
    }

    // ======================= Métodos de apoyo =======================

    private TrabajadorDTO leerTrabajadorDesdeFormulario() {
        String id = vista.getTxtId().getText().trim();
        String nombre = vista.getTxtNombre().getText().trim();
        String telefono = vista.getTxtTelefono().getText().trim();
        String correo = vista.getTxtCorreo().getText().trim();

        String puesto = null;
        if (vista.getCmbPuesto().getSelectedItem() != null) {
            puesto = vista.getCmbPuesto().getSelectedItem().toString().trim();
        }

        String horarios = vista.getTxtHorarios().getText().trim();

        String salarioStr = vista.getFtxtSalario().getText().trim();
        double salario = 0;
        if (!salarioStr.isBlank()) {
            salarioStr = salarioStr.replace(",", ".");
            salario = Double.parseDouble(salarioStr);
        }

        TrabajadorDTO dto = new TrabajadorDTO();
        dto.setId(id);
        dto.setNombre(nombre);
        dto.setTelefono(telefono);
        dto.setCorreo(correo);
        dto.setPuesto(puesto);
        dto.setHorarios(horarios);
        dto.setSalario(salario);

        return dto;
    }

    public void cargarTrabajadorEnFormulario(TrabajadorDTO dto) {
        vista.getTxtId().setText(dto.getId());
        vista.getTxtNombre().setText(dto.getNombre());
        vista.getTxtTelefono().setText(dto.getTelefono());
        vista.getTxtCorreo().setText(dto.getCorreo());
        vista.getCmbPuesto().setSelectedItem(dto.getPuesto());
        vista.getTxtHorarios().setText(dto.getHorarios());
        vista.getFtxtSalario().setText(
                dto.getSalario() == 0 ? "" : String.valueOf(dto.getSalario())
        );
    }

    
    /**
     * Carga en el combo de filtro todos los puestos únicos de BD.
     * Primer item: "Todos".
     */
    

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}