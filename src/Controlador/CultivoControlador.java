/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import dto.CultivoDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Vista.DlgCultivosBusqueda;
import servicio.CultivoServicio;
import Vista.FrmCultivos;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;;
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
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardarCultivo());
        vista.getBtnActualizar().addActionListener(e -> actualizarCultivo());
        vista.getBtnEliminar().addActionListener(e -> eliminarCultivo());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
    }

    private void abrirDialogoBusqueda() {
        DlgCultivosBusqueda dialogo = new DlgCultivosBusqueda(vista, true);
        CultivoBusquedaControlador ctrlBusqueda =
                new CultivoBusquedaControlador(servicio, dialogo, this);
        ctrlBusqueda.mostrar();
    }

    public void iniciar() {
        vista.setVisible(true);
        
    }

    // ======================= Acciones de los botones =======================

    private void guardarCultivo() {
        try {
            CultivoDTO dto = leerCultivoDesdeFormulario();
            servicio.crearCultivo(dto);
            mostrarMensaje("Cultivo registrado correctamente.");
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
                limpiarFormulario();
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el cultivo: " + ex.getMessage());
            }
        }
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

        // Área sembrada
        String areaStr = vista.getFtxtAreaSembrada().getText().trim();
        double area = 0;
        if (!areaStr.isBlank()) {
            areaStr = areaStr.replace(",", ".");
            area = Double.parseDouble(areaStr);
        }

        // Fecha siembra
        String fechaSiembraStr = vista.getFtxtFechaSiembra().getText().trim();
        LocalDate fechaSiembra = null;
        if (!fechaSiembraStr.isBlank() && !fechaSiembraStr.contains("_")) {
            fechaSiembra = LocalDate.parse(fechaSiembraStr);
        }

        // Fecha cosecha (si la usas)
        String fechaCosechaStr = vista.getFtxtFechaCosecha().getText().trim();
        LocalDate fechaCosecha = null;
        if (!fechaCosechaStr.isBlank() && !fechaCosechaStr.contains("_")) {
            fechaCosecha = LocalDate.parse(fechaCosechaStr);
        }

        return new CultivoDTO(
                id,
                nombre,
                tipo,
                area,
                estado,
                fechaSiembra,
                fechaCosecha
        );
    }

    public void cargarCultivoEnFormulario(CultivoDTO dto) {
        vista.getTxtId().setText(dto.getId());
        vista.getTxtNombre().setText(dto.getNombre());
        vista.getCmbTipo().setSelectedItem(dto.getTipo());

        vista.getFtxtAreaSembrada().setText(
                dto.getAreaSembrada() == 0 ? "" : String.valueOf(dto.getAreaSembrada())
        );

        vista.getCmbEstado().setSelectedItem(dto.getEstado());

        if (dto.getFechaSiembra() != null) {
            vista.getFtxtFechaSiembra().setText(dto.getFechaSiembra().toString());
        } else {
            vista.getFtxtFechaSiembra().setText("");
        }

        if (dto.getFechaCosecha() != null) {
            vista.getFtxtFechaCosecha().setText(dto.getFechaCosecha().toString());
        } else {
            vista.getFtxtFechaCosecha().setText("");
        }
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getCmbTipo().setSelectedIndex(-1);
        vista.getFtxtAreaSembrada().setValue(null);
        vista.getCmbEstado().setSelectedIndex(-1);
        vista.getFtxtFechaSiembra().setValue(null);
        vista.getFtxtFechaCosecha().setValue(null);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}