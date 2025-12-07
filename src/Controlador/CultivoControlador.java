/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import dto.CultivoDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import servicio.CultivoServicio;
import Vista.FrmCultivos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 *
 * @author ilope
 */
public class CultivoControlador {
    private final CultivoServicio servicio;
    private final FrmCultivos vista;

    public CultivoControlador(CultivoServicio servicio, FrmCultivos vista) {
        this.servicio = servicio;
        this.vista = vista;
        inicializarEventos();
        configurarTabla();
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardarCultivo());
        vista.getBtnActualizar().addActionListener(e -> actualizarCultivo());
        vista.getBtnEliminar().addActionListener(e -> eliminarCultivo());
        vista.getBtnBuscar().addActionListener(e -> buscarCultivoPorId());
        vista.getBtnListar().addActionListener(e -> listarTodos());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        // Cuando seleccionas una fila en la tabla, cargar los datos al formulario
        vista.getTblCultivos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCultivoDesdeTabla();
            }
        });
    }

    private void configurarTabla() {
        // Forzamos el modelo con las columnas correctas
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Tipo", "Área", "Estado", "Siembra"}, 0
        );
        vista.getTblCultivos().setModel(modelo);
    }

    public void iniciar() {
        vista.setVisible(true);
        listarTodos();
    }

    // ======================= Acciones de los botones =======================

    private void guardarCultivo() {
        try {
            CultivoDTO dto = leerCultivoDesdeFormulario();
            servicio.crearCultivo(dto);
            mostrarMensaje("Cultivo registrado correctamente.");
            listarTodos();
            limpiarFormulario();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al guardar el cultivo: " + ex.getMessage());
        } catch (NumberFormatException | DateTimeParseException ex) {
            mostrarError("Formato de número o fecha inválido: " + ex.getMessage());
        }
    }

    private void actualizarCultivo() {
        try {
            CultivoDTO dto = leerCultivoDesdeFormulario();
            servicio.actualizarCultivo(dto);
            mostrarMensaje("Cultivo actualizado correctamente.");
            listarTodos();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al actualizar el cultivo: " + ex.getMessage());
        } catch (NumberFormatException | DateTimeParseException ex) {
            mostrarError("Formato de número o fecha inválido: " + ex.getMessage());
        }
    }

    private void eliminarCultivo() {
        String id = vista.getTxtId().getText().trim();
        if (id.isBlank()) {
            mostrarError("Debe ingresar el ID del cultivo a eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el cultivo con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarCultivo(id);
                mostrarMensaje("Cultivo eliminado correctamente.");
                listarTodos();
                limpiarFormulario();
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el cultivo: " + ex.getMessage());
            }
        }
    }

    private void buscarCultivoPorId() {
        String id = vista.getTxtId().getText().trim();
        if (id.isBlank()) {
            mostrarError("Debe ingresar un ID para buscar.");
            return;
        }

        try {
            CultivoDTO dto = servicio.buscarPorId(id);
            if (dto == null) {
                mostrarMensaje("No se encontró un cultivo con ese ID.");
                return;
            }
            cargarCultivoEnFormulario(dto);
        } catch (DAOException ex) {
            mostrarError("Error al buscar el cultivo: " + ex.getMessage());
        }
    }

    private void listarTodos() {
        try {
            List<CultivoDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar los cultivos: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getCmbTipo().setSelectedIndex(-1);
        vista.getFtxtAreaSembrada().setValue(null);
        vista.getCmbEstado().setSelectedIndex(-1);
        vista.getFtxtFechaSiembra().setValue(null);
        vista.getTblCultivos().clearSelection();
    }

    // ======================= Métodos de apoyo =======================

    private CultivoDTO leerCultivoDesdeFormulario() {
        String id = vista.getTxtId().getText().trim();
        String nombre = vista.getTxtNombre().getText().trim();

        String tipo = null;
        if (vista.getCmbTipo().getSelectedItem() != null) {
            tipo = vista.getCmbTipo().getSelectedItem().toString().trim();
        }

        String estado = null;
        if (vista.getCmbEstado().getSelectedItem() != null) {
            estado = vista.getCmbEstado().getSelectedItem().toString().trim();
        }

        // Área sembrada: viene del JFormattedTextField
        String areaStr = vista.getFtxtAreaSembrada().getText().trim();
        double area = 0;
        if (!areaStr.isBlank()) {
            // Reemplazar coma por punto si el usuario la usa
            areaStr = areaStr.replace(",", ".");
            area = Double.parseDouble(areaStr);
        }

        // Fecha siembra
        String fechaSiembraStr = vista.getFtxtFechaSiembra().getText().trim();
        LocalDate fechaSiembra = null;
        if (!fechaSiembraStr.isBlank() && !fechaSiembraStr.contains("_")) {
            fechaSiembra = LocalDate.parse(fechaSiembraStr);
        }

        // De momento no capturamos fecha de cosecha desde la vista (puede ser null)
        return new CultivoDTO(
                id,
                nombre,
                tipo,
                area,
                estado,
                fechaSiembra,
                null // fechaCosecha
        );
    }

    private void cargarCultivoEnFormulario(CultivoDTO dto) {
        vista.getTxtId().setText(dto.getId());
        vista.getTxtNombre().setText(dto.getNombre());

        if (dto.getTipo() != null) {
            vista.getCmbTipo().setSelectedItem(dto.getTipo());
        } else {
            vista.getCmbTipo().setSelectedIndex(-1);
        }

        vista.getFtxtAreaSembrada().setText(
                dto.getAreaSembrada() == 0 ? "" : String.valueOf(dto.getAreaSembrada())
        );

        if (dto.getEstado() != null) {
            vista.getCmbEstado().setSelectedItem(dto.getEstado());
        } else {
            vista.getCmbEstado().setSelectedIndex(-1);
        }

        if (dto.getFechaSiembra() != null) {
            vista.getFtxtFechaSiembra().setText(dto.getFechaSiembra().toString());
        } else {
            vista.getFtxtFechaSiembra().setText("");
        }
    }

    private void cargarCultivoDesdeTabla() {
        int fila = vista.getTblCultivos().getSelectedRow();
        if (fila < 0) return;

        DefaultTableModel modelo = (DefaultTableModel) vista.getTblCultivos().getModel();

        String id = modelo.getValueAt(fila, 0).toString();
        String nombre = modelo.getValueAt(fila, 1).toString();
        String tipo = modelo.getValueAt(fila, 2).toString();
        String area = modelo.getValueAt(fila, 3).toString();
        String estado = modelo.getValueAt(fila, 4).toString();
        String fechaSiembra = modelo.getValueAt(fila, 5) != null
                ? modelo.getValueAt(fila, 5).toString()
                : "";

        vista.getTxtId().setText(id);
        vista.getTxtNombre().setText(nombre);
        vista.getCmbTipo().setSelectedItem(tipo);
        vista.getFtxtAreaSembrada().setText(area);
        vista.getCmbEstado().setSelectedItem(estado);
        vista.getFtxtFechaSiembra().setText(fechaSiembra);
    }

    private void cargarTabla(List<CultivoDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblCultivos().getModel();
        modelo.setRowCount(0); // limpiar

        for (CultivoDTO dto : lista) {
            modelo.addRow(new Object[]{
                    dto.getId(),
                    dto.getNombre(),
                    dto.getTipo(),
                    dto.getAreaSembrada(),
                    dto.getEstado(),
                    dto.getFechaSiembra() != null ? dto.getFechaSiembra().toString() : ""
            });
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
