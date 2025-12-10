/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Vista.FrmAlmacenamiento;
import dto.AlmacenamientoDTO;
import servicio.AlmacenamientoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 *
 * @author ilope
 */
public class AlmacenamientoControlador {

    private final AlmacenamientoServicio servicio;
    private final FrmAlmacenamiento vista;

    public AlmacenamientoControlador(AlmacenamientoServicio servicio,
                                     FrmAlmacenamiento vista) {
        this.servicio = servicio;
        this.vista = vista;
        inicializarEventos();
        configurarTabla();
    }

    // Se llamará desde el MenuControlador
    public void iniciar() {
        vista.setVisible(true);
        listarTodos();
        // Más adelante cargaremos combo de idProduccion
    }

    // ================== Inicialización ==================

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardarAlmacenamiento());
        vista.getBtnActualizar().addActionListener(e -> actualizarAlmacenamiento());
        vista.getBtnEliminar().addActionListener(e -> eliminarAlmacenamiento());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        vista.getTblAlmacenamientos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDesdeSeleccionTabla();
            }
        });
    }

    private void configurarTabla() {
        String[] columnas = { "ID", "ID Producción", "Cantidad", "Ingreso", "Egreso" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vista.getTblAlmacenamientos().setModel(modelo);
    }

    // ================== Acciones principales ==================

    private void listarTodos() {
        try {
            List<AlmacenamientoDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar almacenamiento: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<AlmacenamientoDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblAlmacenamientos().getModel();
        modelo.setRowCount(0);

        for (AlmacenamientoDTO dto : lista) {
            modelo.addRow(new Object[] {
                    dto.getId(),
                    dto.getIdProduccion(),
                    dto.getCantidad(),
                    dto.getIngreso(),
                    dto.getEgreso()
            });
        }
    }

    private void cargarDesdeSeleccionTabla() {
        int fila = vista.getTblAlmacenamientos().getSelectedRow();
        if (fila == -1) return;

        JTable tabla = vista.getTblAlmacenamientos();
        vista.getTxtId().setText(String.valueOf(tabla.getValueAt(fila, 0)));
        vista.getCmbIdProduccion().setSelectedItem(tabla.getValueAt(fila, 1));
        vista.getTxtCantidad().setText(String.valueOf(tabla.getValueAt(fila, 2)));

        Object ingreso = tabla.getValueAt(fila, 3);
        Object egreso = tabla.getValueAt(fila, 4);

        vista.getTxtFechaIngreso().setText(ingreso != null ? ingreso.toString() : "");
        vista.getTxtFechaEgreso().setText(egreso != null ? egreso.toString() : "");
    }

    private void guardarAlmacenamiento() {
        try {
            AlmacenamientoDTO dto = leerFormulario(true);
            servicio.registrarAlmacenamiento(dto);
            mostrarInfo("Almacenamiento registrado correctamente. ID generado: " + dto.getId());
            listarTodos();
            limpiarFormulario();
        } catch (ValidacionException | DAOException ex) {
            mostrarError(ex.getMessage());
        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd.");
        }
    }

    private void actualizarAlmacenamiento() {
        try {
            AlmacenamientoDTO dto = leerFormulario(false);
            servicio.actualizarAlmacenamiento(dto);
            mostrarInfo("Almacenamiento actualizado correctamente.");
            listarTodos();
            limpiarFormulario();
        } catch (ValidacionException | DAOException ex) {
            mostrarError(ex.getMessage());
        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd.");
        }
    }

    private void eliminarAlmacenamiento() {
        String idTexto = vista.getTxtId().getText().trim();

        if (idTexto.isEmpty()) {
            mostrarError("Seleccione un registro de almacenamiento para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar este registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(idTexto);
            servicio.eliminarAlmacenamiento(id);
            mostrarInfo("Registro eliminado correctamente.");
            listarTodos();
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            mostrarError("ID de almacenamiento inválido.");
        } catch (ValidacionException | DAOException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void abrirDialogoBusqueda() {
        java.awt.Window parent = SwingUtilities.getWindowAncestor(vista);

    Vista.DlgAlmacenamientoBusqueda dlg =
            new Vista.DlgAlmacenamientoBusqueda(parent, true);

    AlmacenamientoBusquedaControlador ctrlBusqueda =
            new AlmacenamientoBusquedaControlador(servicio, dlg, this);
    ctrlBusqueda.mostrar();
}

    public void cargarDesdeSeleccion(AlmacenamientoDTO dto) {
        // Método llamado desde el dialogo de búsqueda.
        if (dto == null) return;

        vista.getTxtId().setText(String.valueOf(dto.getId()));
        vista.getCmbIdProduccion().setSelectedItem(dto.getIdProduccion());
        vista.getTxtCantidad().setText(String.valueOf(dto.getCantidad()));
        vista.getTxtFechaIngreso().setText(dto.getIngreso() != null ? dto.getIngreso().toString() : "");
        vista.getTxtFechaEgreso().setText(dto.getEgreso() != null ? dto.getEgreso().toString() : "");
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getCmbIdProduccion().setSelectedIndex(-1);
        vista.getTxtCantidad().setText("");
        vista.getTxtFechaIngreso().setText("");
        vista.getTxtFechaEgreso().setText("");
        vista.getTblAlmacenamientos().clearSelection();
    }

    // ================== Utilidades internas ==================

    private AlmacenamientoDTO leerFormulario(boolean esNuevo) {
        AlmacenamientoDTO dto = new AlmacenamientoDTO();

        if (!esNuevo) {
            String idTexto = vista.getTxtId().getText().trim();
            dto.setId(idTexto.isEmpty() ? 0 : Integer.parseInt(idTexto));
        }

        Integer idProd = (Integer) vista.getCmbIdProduccion().getSelectedItem();
        String cantidadTexto = vista.getTxtCantidad().getText().trim();
        String fechaIngresoTexto = vista.getTxtFechaIngreso().getText().trim();
        String fechaEgresoTexto = vista.getTxtFechaEgreso().getText().trim();

        if (idProd != null) {
            dto.setIdProduccion(idProd);
        }

        if (!cantidadTexto.isEmpty()) {
            dto.setCantidad(Integer.parseInt(cantidadTexto));
        }

        if (!fechaIngresoTexto.isEmpty()) {
            dto.setIngreso(LocalDate.parse(fechaIngresoTexto)); // yyyy-MM-dd
        }

        if (!fechaEgresoTexto.isEmpty()) {
            dto.setEgreso(LocalDate.parse(fechaEgresoTexto));
        }

        return dto;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}