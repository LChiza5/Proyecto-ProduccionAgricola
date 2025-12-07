/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import dto.TrabajadorDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
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
        configurarTabla();
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardarTrabajador());
        vista.getBtnActualizar().addActionListener(e -> actualizarTrabajador());
        vista.getBtnEliminar().addActionListener(e -> eliminarTrabajador());
        vista.getBtnBuscar().addActionListener(e -> buscarTrabajadorPorId());
        vista.getBtnBuscarFiltros().addActionListener(e -> buscarPorFiltroPuesto());
        vista.getBtnListar().addActionListener(e -> listarTodos());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
        vista.getCmbFiltroPuesto().addActionListener(e -> buscarPorFiltroPuesto());

        vista.getTblTrabajadores().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarTrabajadorDesdeTabla();
            }
        });
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Teléfono", "Correo", "Puesto", "Horarios", "Salario"}, 0
        );
        vista.getTblTrabajadores().setModel(modelo);
    }

    public void iniciar() {
        vista.setVisible(true);
        listarTodos();
        actualizarComboFiltroPuestos(); 
    }

    // ======================= Acciones de botones =======================

    private void guardarTrabajador() {
        try {
            TrabajadorDTO dto = leerTrabajadorDesdeFormulario();
            servicio.crearTrabajador(dto);

            actualizarComboFiltroPuestos(); 

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

            actualizarComboFiltroPuestos(); 

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
                actualizarComboFiltroPuestos(); 
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el trabajador: " + ex.getMessage());
            }
        }
    }

    private void buscarTrabajadorPorId() {
        String id = vista.getTxtId().getText().trim();
        if (id.isBlank()) {
            mostrarError("Debe ingresar la cédula/ID para buscar.");
            return;
        }

        try {
            TrabajadorDTO dto = servicio.buscarPorId(id);
            if (dto == null) {
                mostrarMensaje("No se encontró un trabajador con ese ID.");
                return;
            }
            cargarTrabajadorEnFormulario(dto);
        } catch (DAOException ex) {
            mostrarError("Error al buscar el trabajador: " + ex.getMessage());
        }
    }

    private void buscarPorFiltroPuesto() {
        Object sel = vista.getCmbFiltroPuesto().getSelectedItem();
        if (sel == null) {
            return;
        }
        String puestoSeleccionado = sel.toString();

        if (puestoSeleccionado.equals("Todos")) {
            listarTodos();
            return;
        }

        try {
            List<TrabajadorDTO> lista = servicio.buscarConFiltros(null, puestoSeleccionado, null);
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al buscar por puesto: " + ex.getMessage());
        }
    }

    private void listarTodos() {
        try {
            List<TrabajadorDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
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
        vista.getFtxtSalario().setValue(null);
        if (vista.getCmbFiltroPuesto().getItemCount() > 0) {
            vista.getCmbFiltroPuesto().setSelectedIndex(0);
        }

        vista.getTblTrabajadores().clearSelection();
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

    private void cargarTrabajadorEnFormulario(TrabajadorDTO dto) {
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

    private void cargarTrabajadorDesdeTabla() {
        int fila = vista.getTblTrabajadores().getSelectedRow();
        if (fila < 0) return;

        DefaultTableModel modelo = (DefaultTableModel) vista.getTblTrabajadores().getModel();

        String id = modelo.getValueAt(fila, 0).toString();
        String nombre = modelo.getValueAt(fila, 1).toString();
        String telefono = modelo.getValueAt(fila, 2).toString();
        String correo = modelo.getValueAt(fila, 3).toString();
        String puesto = modelo.getValueAt(fila, 4).toString();
        String horarios = modelo.getValueAt(fila, 5).toString();
        String salario = modelo.getValueAt(fila, 6).toString();

        vista.getTxtId().setText(id);
        vista.getTxtNombre().setText(nombre);
        vista.getTxtTelefono().setText(telefono);
        vista.getTxtCorreo().setText(correo);
        vista.getCmbPuesto().setSelectedItem(puesto);
        vista.getTxtHorarios().setText(horarios);
        vista.getFtxtSalario().setText(salario);
    }

    private void cargarTabla(List<TrabajadorDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblTrabajadores().getModel();
        modelo.setRowCount(0);

        for (TrabajadorDTO dto : lista) {
            modelo.addRow(new Object[]{
                    dto.getId(),
                    dto.getNombre(),
                    dto.getTelefono(),
                    dto.getCorreo(),
                    dto.getPuesto(),
                    dto.getHorarios(),
                    dto.getSalario()
            });
        }
    }

    /**
     * Carga en el combo de filtro todos los puestos únicos de BD.
     * Primer item: "Todos".
     */
    private void actualizarComboFiltroPuestos() {
        JComboBox<String> combo = vista.getCmbFiltroPuesto();
        combo.removeAllItems();
        combo.addItem("Todos");

        try {
            List<String> puestos = servicio.obtenerPuestosUnicos();
            for (String p : puestos) {
                combo.addItem(p);
            }
            combo.setSelectedIndex(0); 
        } catch (DAOException ex) {
            mostrarError("Error al cargar los puestos para el filtro: " + ex.getMessage());
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}