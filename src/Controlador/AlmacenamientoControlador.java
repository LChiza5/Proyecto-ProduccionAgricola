/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Vista.DlgAlmacenamientoBusqueda;
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
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());
    }

    // =================== Acciones CRUD ===================

    private void guardar() {
        try {
            AlmacenamientoDTO dto = leerDesdeFormulario();
            servicio.registrarAlmacenamiento(dto);

            mostrarMensaje("Registro de almacenamiento guardado correctamente.");
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al guardar el registro de almacenamiento: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            AlmacenamientoDTO dto = leerDesdeFormulario();
            servicio.actualizarAlmacenamiento(dto);

            mostrarMensaje("Registro de almacenamiento actualizado correctamente.");
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al actualizar el registro de almacenamiento: " + ex.getMessage());
        }
    }

    private void eliminar() {
        String idStr = vista.getTxtId().getText().trim();
        if (idStr.isEmpty()) {
            mostrarError("Debe indicar el ID del registro de almacenamiento a eliminar.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            mostrarError("El ID debe ser un número entero.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el registro de almacenamiento con ID: " + id + " ?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarAlmacenamiento(id);
                mostrarMensaje("Registro de almacenamiento eliminado correctamente.");
                limpiar();
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el registro de almacenamiento: " + ex.getMessage());
            }
        }
    }

    private void limpiar() {
        vista.getTxtId().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtFechaIngreso().setText("");
        vista.getTxtFechaEgreso().setText("");

        // Limpiar combo de producción (elige la convención que uses)
        JComboBox<?> combo = vista.getCmbIdProduccion();
        if (combo.getItemCount() > 0) {
            combo.setSelectedIndex(0); // por ejemplo, "Seleccione..." o el primero
        }
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    // =================== Búsqueda con diálogo ===================

    private void abrirDialogoBusqueda() {
        DlgAlmacenamientoBusqueda dialogo = new DlgAlmacenamientoBusqueda(vista, true);
        AlmacenamientoBusquedaControlador ctrlBusqueda =
                new AlmacenamientoBusquedaControlador(servicio, dialogo, this);
        ctrlBusqueda.mostrar();
    }

    /**
     * Método llamado desde AlmacenamientoBusquedaControlador
     * cuando el usuario selecciona un registro en el diálogo.
     */
    public void cargarDesdeSeleccion(AlmacenamientoDTO dto) {
        vista.getTxtId().setText(String.valueOf(dto.getId()));
        vista.getTxtCantidad().setText(String.valueOf(dto.getCantidad()));

        // Seleccionar el idProduccion en el combo
        // Esto funcionará si llenas el combo con Integer o String que corresponda al ID
        vista.getCmbIdProduccion().setSelectedItem(dto.getIdProduccion());

        if (dto.getIngreso() != null) {
            vista.getTxtFechaIngreso().setText(dto.getIngreso().toString()); // yyyy-MM-dd
        } else {
            vista.getTxtFechaIngreso().setText("");
        }

        if (dto.getEgreso() != null) {
            vista.getTxtFechaEgreso().setText(dto.getEgreso().toString());
        } else {
            vista.getTxtFechaEgreso().setText("");
        }
    }

    // =================== Métodos de apoyo ===================

    private AlmacenamientoDTO leerDesdeFormulario() throws ValidacionException {
        AlmacenamientoDTO dto = new AlmacenamientoDTO();

        // ID (puede estar vacío en "guardar")
        String idStr = vista.getTxtId().getText().trim();
        if (!idStr.isEmpty()) {
            try {
                dto.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException ex) {
                throw new ValidacionException("El ID debe ser un número entero.");
            }
        }

        // ID Producción leído desde el combo
        Object selProd = vista.getCmbIdProduccion().getSelectedItem();
        if (selProd == null) {
            throw new ValidacionException("Debe seleccionar una producción.");
        }
        try {
            int idProduccion = (selProd instanceof Integer)
                    ? (Integer) selProd
                    : Integer.parseInt(selProd.toString());
            dto.setIdProduccion(idProduccion);
        } catch (NumberFormatException ex) {
            throw new ValidacionException("El ID de producción seleccionado no es válido.");
        }

        // Cantidad
        String cantStr = vista.getTxtCantidad().getText().trim();
        if (cantStr.isEmpty()) {
            throw new ValidacionException("Debe indicar la cantidad almacenada.");
        }
        try {
            dto.setCantidad(Integer.parseInt(cantStr));
        } catch (NumberFormatException ex) {
            throw new ValidacionException("La cantidad debe ser un número entero.");
        }

        // Fecha ingreso
        String ingresoStr = vista.getTxtFechaIngreso().getText().trim();
        if (ingresoStr.isEmpty()) {
            throw new ValidacionException("Debe indicar la fecha de ingreso.");
        }
        try {
            dto.setIngreso(LocalDate.parse(ingresoStr)); // yyyy-MM-dd
        } catch (DateTimeParseException ex) {
            throw new ValidacionException("Formato de fecha de ingreso inválido. Use yyyy-MM-dd.");
        }

        // Fecha egreso (opcional)
        String egresoStr = vista.getTxtFechaEgreso().getText().trim();
        if (!egresoStr.isEmpty()) {
            try {
                dto.setEgreso(LocalDate.parse(egresoStr));
            } catch (DateTimeParseException ex) {
                throw new ValidacionException("Formato de fecha de egreso inválido. Use yyyy-MM-dd.");
            }
        } else {
            dto.setEgreso(null);
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